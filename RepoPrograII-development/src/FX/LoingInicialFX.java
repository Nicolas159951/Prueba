
package FX;
import Excepciones.CamposVaciosException;
import Entidades.EmpresaCliente;
import Entidades.Enums.TipoCondiciondeIva;
import Manejodearchivos.JSONUtilesEmpresa;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONException;
import java.util.List;
import java.util.function.Consumer;
/// Logininicial sirve para mantener el control de las empresaclientes que son la clase principal con la que el programa funciona
/// tiene varios botones como el de cargar una empresa el cual pide que se ingrese un cuit o el nombre de fantasia de la empresa
/// para buscarlo por medio JsonEmpresas y devolver la empresa elegida
/// El segundo boton que es la cracion de empresas sirve para pedir los datos de la empresa y cargarlos por medio de JsonEmpresas y el metodo
/// correspondiente. Despues es el boton de modificar datos de la empresa el cual esta bloqueado siempre que se ingrese a la parte de creacion
/// de empresas y se habilita una vez que se selecciono una empresa y el cual llama al formulario de creacion para modificar los datos de la empresa
/// y porque tiene un metodo aparte ? nos dimos cuenta que era mas facil crear un metodo para la peticion de datos antes de que cada metodo tenga el suyo propio
/// pero no llegamos a modificar el resto de metodos . Los ultimos dos botones son un listado de empresas que solo posee el cuit y el nombre de fantasia
/// para saber que empresas existen en el archivo y volver al menu principal (mainfx) para cargar las facturas
public class LoingInicialFX extends Application {

    @Override
    public void start(Stage stage) {
        mostrarMenu(stage, empresa -> {
            System.out.println("Empresa cargada: " + empresa.getNombredefantasia());
        }, () -> {
            System.out.println("Volver al menú principal");
        });
    }

    //  Menú principal de login
    public static void mostrarMenu(Stage stage, Consumer<EmpresaCliente> onEmpresaCargada, Runnable volverAlMenuPrincipal) {
        Label titulo = new Label("Menú Inicial - Empresa");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnCargarExistente = new Button("Ingresar con empresa existente");
        Button btnCrearNueva = new Button("Crear nueva empresa");
        Button btnModificarEmpresa = new Button("Modificar empresa cargada");
        Button btnVerEmpresas = new Button("Ver empresas registradas"); // 👈 NUEVO BOTÓN
        Button btnVolver = new Button("Volver al menú principal");

        // Inicialmente deshabilitado
        btnModificarEmpresa.setDisable(true);

        //  Agregamos el botón al VBox (entre Modificar y Volver)
        VBox root = new VBox(15, titulo, btnCargarExistente, btnCrearNueva, btnModificarEmpresa, btnVerEmpresas, btnVolver);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Gestión de Empresa");
        stage.show();

        JSONUtilesEmpresa jsonUtil = new JSONUtilesEmpresa();
        final EmpresaCliente[] empresaCargada = {null};


        // 🔹 Cargar empresa existente
        btnCargarExistente.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Cargar Empresa");
            dialog.setHeaderText("Ingrese el CUIT o el nombre de fantasía:");
            dialog.setContentText("Dato:");

            dialog.showAndWait().ifPresent(nombreCuit -> {

                //  Validación: no permitir campo vacío
                if (nombreCuit.trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Campo vacío");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor ingrese un CUIT o nombre de fantasía antes de continuar.");
                    alert.showAndWait();
                    return; //  No continúa si está vacío
                }

                Task<EmpresaCliente> tarea = new Task<>() {
                    @Override
                    protected EmpresaCliente call() throws Exception {
                        return jsonUtil.leerPorDato(nombreCuit);
                    }
                };

                tarea.setOnSucceeded(ev -> {
                    EmpresaCliente empresa = tarea.getValue();
                    if (empresa != null) {
                        empresaCargada[0] = empresa;
                        btnModificarEmpresa.setDisable(false);
                        mostrarMensaje("Éxito", "Empresa cargada correctamente:\n" + empresa.getNombredefantasia());
                        onEmpresaCargada.accept(empresa);
                    } else {
                        mostrarMensaje("Error", "No se encontró una empresa con ese dato.");
                    }
                });

                tarea.setOnFailed(ev -> mostrarMensaje("Error", "Hubo un problema al leer el archivo JSON."));

                Thread hilo = new Thread(tarea);
                hilo.setDaemon(true);
                hilo.start();
            });
        });

        //  Crear nueva empresa
        btnCrearNueva.setOnAction(e -> mostrarFormularioCreacion(stage, empresa -> {
            empresaCargada[0] = empresa;
            btnModificarEmpresa.setDisable(false);
            onEmpresaCargada.accept(empresa);
        }, volverAlMenuPrincipal));

        //  Modificar empresa cargada
        btnModificarEmpresa.setOnAction(e -> {
            if (empresaCargada[0] != null) {
                mostrarFormularioEdicion(stage, empresaCargada[0], () -> {
                    mostrarMenu(stage, onEmpresaCargada, volverAlMenuPrincipal);
                });
            }
        });

        // Ver empresas registradas
        btnVerEmpresas.setOnAction(e -> mostrarListadoEmpresas(stage)); // 👈 LLAMA A TU MÉTODO EXISTENTE

        // Volver
        btnVolver.setOnAction(e -> volverAlMenuPrincipal.run());
    }

    //Formulario para crear empresa
    private static void mostrarFormularioCreacion(Stage stage, Consumer<EmpresaCliente> onEmpresaCargada, Runnable volverAlMenuPrincipal) {
        Label lblAlias = new Label("Alias:");
        TextField alias = new TextField();

        Label lblRazon = new Label("Razón social:");
        TextField razon = new TextField();

        Label lblFantasia = new Label("Nombre de fantasía:");
        TextField fantasia = new TextField();

        Label lblCuit = new Label("CUIT:");
        TextField cuit = new TextField();

        Label lblIva = new Label("Condición de IVA:");
        ComboBox<TipoCondiciondeIva> iva = new ComboBox<>();
        iva.getItems().addAll(TipoCondiciondeIva.values());

        Label lblIB = new Label("Ingreso Bruto:");
        TextField ib = new TextField();

        Label lblActividad = new Label("Actividad:");
        TextField actividad = new TextField();

        Label lblDireccion = new Label("Dirección física:");
        TextField direccion = new TextField();

        Label lblLocalidad = new Label("Localidad:");
        TextField localidad = new TextField();

        Label lblProvincia = new Label("Provincia:");
        TextField provincia = new TextField();

        Label lblTelefono = new Label("Teléfono (opcional):");
        TextField telefono = new TextField();

        Label lblCorreo = new Label("Correo electrónico (opcional):");
        TextField correo = new TextField();

        Label lblWeb = new Label("Página web (opcional):");
        TextField web = new TextField();

        Button guardar = new Button("Guardar empresa");
        Button volver = new Button("Volver al menú");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(lblAlias, 0, 0); grid.add(alias, 1, 0);
        grid.add(lblRazon, 0, 1); grid.add(razon, 1, 1);
        grid.add(lblFantasia, 0, 2); grid.add(fantasia, 1, 2);
        grid.add(lblCuit, 0, 3); grid.add(cuit, 1, 3);
        grid.add(lblIva, 0, 4); grid.add(iva, 1, 4);
        grid.add(lblIB, 0, 5); grid.add(ib, 1, 5);
        grid.add(lblActividad, 0, 6); grid.add(actividad, 1, 6);
        grid.add(lblDireccion, 0, 7); grid.add(direccion, 1, 7);
        grid.add(lblLocalidad, 0, 8); grid.add(localidad, 1, 8);
        grid.add(lblProvincia, 0, 9); grid.add(provincia, 1, 9);
        grid.add(lblTelefono, 0, 10); grid.add(telefono, 1, 10);
        grid.add(lblCorreo, 0, 11); grid.add(correo, 1, 11);
        grid.add(lblWeb, 0, 12); grid.add(web, 1, 12);

        HBox botones = new HBox(10, guardar, volver);
        botones.setAlignment(Pos.CENTER);
        grid.add(botones, 0, 13, 2, 1);

        Scene scene = new Scene(grid, 600, 650);
        stage.setScene(scene);
        stage.setTitle("Crear Nueva Empresa");

        JSONUtilesEmpresa jsonUtil = new JSONUtilesEmpresa();

        guardar.setOnAction(ev -> {
            try {
                //  Validar campos obligatorios vacíos
                if (alias.getText().trim().isEmpty() ||
                        razon.getText().trim().isEmpty() ||
                        fantasia.getText().trim().isEmpty() ||
                        cuit.getText().trim().isEmpty() ||
                        ib.getText().trim().isEmpty() ||
                        actividad.getText().trim().isEmpty() ||
                        direccion.getText().trim().isEmpty() ||
                        localidad.getText().trim().isEmpty() ||
                        provincia.getText().trim().isEmpty()) {

                    mostrarMensaje("Error", "Por favor complete todos los campos obligatorios antes de guardar.");
                    return;
                }

                // Validar selección de IVA
                TipoCondiciondeIva ivaSeleccionado = iva.getValue();
                if (ivaSeleccionado == null) {
                    mostrarMensaje("Error", "Seleccione una condición de IVA.");
                    return;
                }

                //  Crear la empresa
                EmpresaCliente empresa = new EmpresaCliente(
                        alias.getText(),
                        razon.getText(),
                        fantasia.getText(),
                        cuit.getText(),
                        ivaSeleccionado,
                        ib.getText(),
                        actividad.getText(),
                        direccion.getText(),
                        localidad.getText(),
                        provincia.getText(),
                        telefono.getText().isEmpty() ? "" : telefono.getText(),
                        correo.getText().isEmpty() ? "" : correo.getText(),
                        web.getText().isEmpty() ? "" : web.getText(),
                        null,
                        null
                );

                //  Guardar en JSON en un hilo
                Task<Void> tareaGuardar = new Task<>() {
                    @Override
                    protected Void call() throws JSONException {
                        jsonUtil.escribir(empresa);
                        return null;
                    }
                };

                tareaGuardar.setOnSucceeded(ev2 -> {
                    mostrarMensaje("Éxito", "Empresa creada y guardada correctamente.");
                    onEmpresaCargada.accept(empresa);
                });

                tareaGuardar.setOnFailed(ev2 ->
                        mostrarMensaje("Error", "No se pudo guardar la empresa:\n" + tareaGuardar.getException().getMessage())
                );

                Thread hilo = new Thread(tareaGuardar);
                hilo.setDaemon(true);
                hilo.start();

            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarMensaje("Error", "Ocurrió un problema al crear la empresa.");
            }
        });

        volver.setOnAction(ev -> mostrarMenu(stage, onEmpresaCargada, volverAlMenuPrincipal));
    }
    //  Formulario para editar empresa existente
    public static void mostrarFormularioEdicion(Stage stage, EmpresaCliente empresa, Runnable volverAlMenuInicial) {
        Label lblAlias = new Label("Alias:");
        TextField alias = new TextField(empresa.getAliasEmpresa());

        Label lblRazon = new Label("Razón social:");
        TextField razon = new TextField(empresa.getRazonSocial());

        Label lblFantasia = new Label("Nombre de fantasía:");
        TextField fantasia = new TextField(empresa.getNombredefantasia());

        Label lblCuit = new Label("CUIT:");
        TextField cuit = new TextField(empresa.getCuit());

        Label lblIva = new Label("Condición de IVA:");
        ComboBox<TipoCondiciondeIva> iva = new ComboBox<>();
        iva.getItems().addAll(TipoCondiciondeIva.values());
        iva.setValue(empresa.getCondiciondeiva());

        Label lblIB = new Label("Ingreso Bruto:");
        TextField ib = new TextField(empresa.getIB());

        Label lblActividad = new Label("Actividad:");
        TextField actividad = new TextField(empresa.getActividad());

        Label lblDireccion = new Label("Dirección física:");
        TextField direccion = new TextField(empresa.getDireccion());

        Label lblLocalidad = new Label("Localidad:");
        TextField localidad = new TextField(empresa.getLocalidad());

        Label lblProvincia = new Label("Provincia:");
        TextField provincia = new TextField(empresa.getProvincia());

        Label lblTelefono = new Label("Teléfono (opcional):");
        TextField telefono = new TextField(empresa.getTelefono());

        Label lblCorreo = new Label("Correo electrónico (opcional):");
        TextField correo = new TextField(empresa.getCorreoElectronico());

        Label lblWeb = new Label("Página web (opcional):");
        TextField web = new TextField(empresa.getPaginaweb());

        Button guardar = new Button("Guardar cambios");
        Button volver = new Button("Volver al menú");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(lblAlias, 0, 0); grid.add(alias, 1, 0);
        grid.add(lblRazon, 0, 1); grid.add(razon, 1, 1);
        grid.add(lblFantasia, 0, 2); grid.add(fantasia, 1, 2);
        grid.add(lblCuit, 0, 3); grid.add(cuit, 1, 3);
        grid.add(lblIva, 0, 4); grid.add(iva, 1, 4);
        grid.add(lblIB, 0, 5); grid.add(ib, 1, 5);
        grid.add(lblActividad, 0, 6); grid.add(actividad, 1, 6);
        grid.add(lblDireccion, 0, 7); grid.add(direccion, 1, 7);
        grid.add(lblLocalidad, 0, 8); grid.add(localidad, 1, 8);
        grid.add(lblProvincia, 0, 9); grid.add(provincia, 1, 9);
        grid.add(lblTelefono, 0, 10); grid.add(telefono, 1, 10);
        grid.add(lblCorreo, 0, 11); grid.add(correo, 1, 11);
        grid.add(lblWeb, 0, 12); grid.add(web, 1, 12);

        HBox botones = new HBox(10, guardar, volver);
        botones.setAlignment(Pos.CENTER);
        grid.add(botones, 0, 13, 2, 1);

        Scene scene = new Scene(grid, 600, 650);
        stage.setScene(scene);
        stage.setTitle("Editar Empresa: " + empresa.getNombredefantasia());

        JSONUtilesEmpresa jsonUtil = new JSONUtilesEmpresa();

        guardar.setOnAction(ev -> {
            try {
                TipoCondiciondeIva ivaSeleccionado = iva.getValue();
                if (ivaSeleccionado == null) {
                    throw new CamposVaciosException("Debe seleccionar una condición de IVA.");
                }


                if (alias.getText().trim().isEmpty() || razon.getText().trim().isEmpty() ||
                        fantasia.getText().trim().isEmpty() || cuit.getText().trim().isEmpty() ||
                        ib.getText().trim().isEmpty() || actividad.getText().trim().isEmpty() ||
                        direccion.getText().trim().isEmpty() || localidad.getText().trim().isEmpty() ||
                        provincia.getText().trim().isEmpty()) {
                    throw new CamposVaciosException("Todos los campos obligatorios deben estar completos.");
                }

                // Si todo está correcto, actualizamos los datos
                empresa.setAliasEmpresa(alias.getText());
                empresa.setRazonSocial(razon.getText());
                empresa.setNombredefantasia(fantasia.getText());
                empresa.setCuit(cuit.getText());
                empresa.setCondiciondeiva(ivaSeleccionado);
                empresa.setIB(ib.getText());
                empresa.setActividad(actividad.getText());
                empresa.setDireccion(direccion.getText());
                empresa.setLocalidad(localidad.getText());
                empresa.setProvincia(provincia.getText());
                empresa.setTelefono(telefono.getText());
                empresa.setCorreoElectronico(correo.getText());
                empresa.setPaginaweb(web.getText());

                jsonUtil.escribir(empresa);
                mostrarMensaje("Éxito", "Empresa actualizada correctamente ✅");
                volverAlMenuInicial.run();

            } catch (CamposVaciosException ex) {
                mostrarMensaje("Campos incompletos", ex.getMessage());
            } catch (Exception ex) {
                mostrarMensaje("Error", "No se pudo guardar los cambios:\n" + ex.getMessage());
            }
        });

        volver.setOnAction(ev -> volverAlMenuInicial.run());
    }

    //  Mostrar alertas seguras desde cualquier hilo
    private static void mostrarMensaje(String titulo, String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch();
    }
    private static void mostrarListadoEmpresas(Stage ownerStage) {
        JSONUtilesEmpresa jsonUtil = new JSONUtilesEmpresa();
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.initOwner(ownerStage);
        ventana.setTitle("Empresas registradas");

        TableView<EmpresaCliente> tabla = new TableView<>();

        TableColumn<EmpresaCliente, String> colCuit = new TableColumn<>("CUIT");
        colCuit.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCuit()));
        colCuit.setPrefWidth(150);

        TableColumn<EmpresaCliente, String> colFantasia = new TableColumn<>("Nombre de Fantasía");
        colFantasia.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombredefantasia()));
        colFantasia.setPrefWidth(220);

        tabla.getColumns().addAll(colCuit, colFantasia);

        try {
            List<EmpresaCliente> empresas = jsonUtil.leerTodas();
            tabla.getItems().addAll(empresas);
        } catch (Exception ex) {
            mostrarMensaje("Error", "No se pudieron leer las empresas:\n" + ex.getMessage());
        }

        Button cerrar = new Button("Cerrar");
        cerrar.setOnAction(e -> ventana.close());

        VBox layout = new VBox(10, tabla, cerrar);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 400);
        ventana.setScene(scene);
        ventana.showAndWait();
    }
}