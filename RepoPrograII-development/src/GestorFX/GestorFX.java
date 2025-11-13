package GestorFX;

import Entidades.EmpresaCliente;
import Entidades.Entidad;
import Entidades.GestoraEntidades;
import Entidades.Proveedor;
import FX.CrearEntidadFX;
import Facturas.*;
import Facturas.Enums.Tipodecomprobante;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import Entidades.Cliente;
import Excepciones.CamposVaciosException;
import Excepciones.Cuiterroneo;
/// GestorFx es el que se encarga de manejar la mayoria del codigo ya que posee los metodos necesarios para cargar las facturas se divide en metodos
/// de compra y venta para manejar los correspondientes arreglos y tambien llama al CrearEntidadFx el cual sirve para crear a los provedores o clientes que
/// son manipulados por el gestorentidades
public class GestorFX {



    /**
     * Carga una factura de compra mediante formulario FX y la retorna
     */
    public Factura cargarFacturaCompraFX(Stage stage, EmpresaCliente empresa, Runnable volverAlMenu) {
        Label lblTitulo = new Label("Carga de Factura de Compra");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ChoiceBox<String> tipoFactura = new ChoiceBox<>();
        tipoFactura.getItems().addAll("A", "B", "C");
        tipoFactura.setValue("A");

        TextField txtCuit = new TextField();
        txtCuit.setPromptText("CUIT del proveedor");

        TextField txtSucursal = new TextField();
        txtSucursal.setPromptText("Número de sucursal");

        TextField txtNumFactura = new TextField();
        txtNumFactura.setPromptText("Número de factura");

        DatePicker dpFecha = new DatePicker(LocalDate.now());

        TextField txtNoGravado = new TextField();
        txtNoGravado.setPromptText("No gravado");

        TextField txtIVA21 = new TextField();
        txtIVA21.setPromptText("IVA 21%");

        TextField txtIVA105 = new TextField();
        txtIVA105.setPromptText("IVA 10.5%");

        TextField txtPercepcionIVA = new TextField();
        txtPercepcionIVA.setPromptText("Percepción de IVA");

        TextField txtIngresosBrutos = new TextField();
        txtIngresosBrutos.setPromptText("Percepción de Ingresos Brutos");

        TextField txtOtrosImp = new TextField();
        txtOtrosImp.setPromptText("Otros impuestos");

        // Bloquear campos de IVA si es factura C
        tipoFactura.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean esC = "C".equals(newVal);
            txtIVA21.setDisable(esC);
            txtIVA105.setDisable(esC);
            txtPercepcionIVA.setDisable(esC);
            txtIngresosBrutos.setDisable(esC);
            txtOtrosImp.setDisable(esC);

            if (esC) {
                txtIVA21.clear();
                txtIVA105.clear();
                txtPercepcionIVA.clear();
                txtIngresosBrutos.clear();
                txtOtrosImp.clear();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(new Label("Tipo de factura:"), 0, 1);
        grid.add(tipoFactura, 1, 1);
        grid.add(new Label("CUIT:"), 0, 2);
        grid.add(txtCuit, 1, 2);
        grid.add(new Label("Sucursal:"), 0, 3);
        grid.add(txtSucursal, 1, 3);
        grid.add(new Label("Número de Factura:"), 0, 4);
        grid.add(txtNumFactura, 1, 4);
        grid.add(new Label("Fecha:"), 0, 5);
        grid.add(dpFecha, 1, 5);
        grid.add(new Label("No Gravado:"), 0, 6);
        grid.add(txtNoGravado, 1, 6);
        grid.add(new Label("IVA 21%:"), 0, 7);
        grid.add(txtIVA21, 1, 7);
        grid.add(new Label("IVA 10.5%:"), 0, 8);
        grid.add(txtIVA105, 1, 8);
        grid.add(new Label("Percepción IVA:"), 0, 9);
        grid.add(txtPercepcionIVA, 1, 9);
        grid.add(new Label("Ingresos Brutos:"), 0, 10);
        grid.add(txtIngresosBrutos, 1, 10);
        grid.add(new Label("Otros Impuestos:"), 0, 11);
        grid.add(txtOtrosImp, 1, 11);

        Button btnGuardar = new Button("Guardar Factura");
        Button btnVolver = new Button("Volver");
        grid.add(btnGuardar, 0, 12);
        grid.add(btnVolver, 1, 12);

        Scene scene = new Scene(grid, 450, 600);
        stage.setScene(scene);

        final Factura[] facturaResultado = {null};

        btnGuardar.setOnAction(e -> {
            try {
                String cuit = txtCuit.getText().trim();
                if (cuit.isEmpty() || !cuit.matches("\\d{11}")) {
                    throw new Cuiterroneo("El CUIT debe tener 11 dígitos numéricos.");
                }

                GestoraEntidades<Entidad> gestora = empresa.getGestoraEntidades();
                Proveedor proveedor = (Proveedor) gestora.buscarPorCuit(cuit, Proveedor.class);

                // Si no existe el proveedor, abrir formulario para crearlo
                if (proveedor == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Proveedor no encontrado");
                    alert.setHeaderText("No se encontró un proveedor con ese CUIT.");
                    alert.setContentText("Se abrirá un formulario para crear un nuevo proveedor.");
                    alert.showAndWait();

                    proveedor = CrearEntidadFX.mostrarFormularioProveedor(stage, cuit);
                    if (proveedor == null) {
                        mostrarMensaje("Operación cancelada. No se cargó la factura.");
                        return;
                    }
                    gestora.agregarEntidad(proveedor); // Guardar proveedor en la lista y JSON
                }

                // Validaciones y parseos
                int sucursal = Integer.parseInt(txtSucursal.getText().trim());
                int numFactura = Integer.parseInt(txtNumFactura.getText().trim());
                LocalDate fecha = dpFecha.getValue();

                double noGravado = parseDoubleSafe(txtNoGravado);
                double iva21 = txtIVA21.isDisabled() ? 0 : parseDoubleSafe(txtIVA21);
                double iva105 = txtIVA105.isDisabled() ? 0 : parseDoubleSafe(txtIVA105);
                double perIva = txtPercepcionIVA.isDisabled() ? 0 : parseDoubleSafe(txtPercepcionIVA);
                double ingBrutos = txtIngresosBrutos.isDisabled() ? 0 : parseDoubleSafe(txtIngresosBrutos);
                double otros = txtOtrosImp.isDisabled() ? 0 : parseDoubleSafe(txtOtrosImp);

                String tipo = tipoFactura.getValue();
                Factura factura;
                switch (tipo) {
                    case "A" -> {
                        CargaFact<FacturaA> cargaA = new CargaFact<>(Tipodecomprobante.A);
                        factura = cargaA.crearFactura(noGravado, cuit, Tipodecomprobante.A, sucursal, numFactura, fecha, iva21, iva105, perIva, ingBrutos, otros);
                    }
                    case "B" -> {
                        CargaFact<FacturaB> cargaB = new CargaFact<>(Tipodecomprobante.B);
                        factura = cargaB.crearFactura(noGravado, cuit, Tipodecomprobante.B, sucursal, numFactura, fecha, iva21, iva105, perIva, ingBrutos, otros);
                    }
                    case "C" -> {
                        CargaFact<FacturaC> cargaC = new CargaFact<>(Tipodecomprobante.C);
                        factura = cargaC.crearFactura(noGravado, cuit, Tipodecomprobante.C, sucursal, numFactura, fecha, 0, 0, 0, 0, 0);
                    }
                    default -> {
                        mostrarMensaje("Tipo de factura inválido.");
                        return;
                    }
                }

                empresa.cargadecompra(factura);
                facturaResultado[0] = factura;
                mostrarMensaje("Factura cargada correctamente ✅");
                volverAlMenu.run();

            } catch (CamposVaciosException | Cuiterroneo ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            } catch (NumberFormatException nfe) {
                mostrarMensaje("Error en campos numéricos: " + nfe.getMessage());
            } catch (Exception ex) {
                mostrarMensaje("Error inesperado: " + ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> volverAlMenu.run());

        return facturaResultado[0];
    }


    /**
     *  Muestra las facturas cargadas (compra o venta)
     */
    public void verFacturasFX(Stage stage, ArrayList<Factura> lista, Runnable volver) {
        if (lista.isEmpty()) {
            mostrarMensaje("No hay facturas cargadas.");
            volver.run();
            return;
        }

        ListView<String> listaView = new ListView<>();
        int i = 1;
        for (Factura f : lista) {
            listaView.getItems().add(i++ + ") " + f.toString());
        }

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> volver.run());

        VBox layout = new VBox(15, new Label("Listado de Facturas"), listaView, btnVolver);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
    }

    private void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Carga una factura de venta mediante formulario FX y la retorna
     */
    public Factura cargarFacturaVentaFX(Stage stage, EmpresaCliente empresa, Runnable volverAlMenu) {
        Label lblTitulo = new Label("Carga de Factura de Venta");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ChoiceBox<String> tipoFactura = new ChoiceBox<>();
        tipoFactura.getItems().addAll("A", "B", "C");
        tipoFactura.setValue("A");

        TextField txtCuit = new TextField();
        txtCuit.setPromptText("CUIT del cliente o 1 si es consumidor final");

        TextField txtSucursal = new TextField();
        txtSucursal.setPromptText("Número de sucursal");

        TextField txtNumFactura = new TextField();
        txtNumFactura.setPromptText("Número de factura");

        DatePicker dpFecha = new DatePicker(LocalDate.now());

        TextField txtNoGravado = new TextField();
        txtNoGravado.setPromptText("No gravado");

        TextField txtIVA21 = new TextField();
        txtIVA21.setPromptText("IVA 21%");

        TextField txtIVA105 = new TextField();
        txtIVA105.setPromptText("IVA 10.5%");

        TextField txtPercepcionIVA = new TextField();
        txtPercepcionIVA.setPromptText("Percepción de IVA");

        TextField txtIngresosBrutos = new TextField();
        txtIngresosBrutos.setPromptText("Percepción de Ingresos Brutos");

        TextField txtOtrosImp = new TextField();
        txtOtrosImp.setPromptText("Otros impuestos");

        // Desactivar campos si se elige factura tipo C
        tipoFactura.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean esC = "C".equals(newVal);
            txtIVA21.setDisable(esC);
            txtIVA105.setDisable(esC);
            txtPercepcionIVA.setDisable(esC);
            txtIngresosBrutos.setDisable(esC);
            txtOtrosImp.setDisable(esC);

            if (esC) {
                txtIVA21.clear();
                txtIVA105.clear();
                txtPercepcionIVA.clear();
                txtIngresosBrutos.clear();
                txtOtrosImp.clear();
            }
        });

        Button btnGuardar = new Button("Guardar Factura");
        Button btnVolver = new Button("Volver");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(new Label("Tipo de factura:"), 0, 1);
        grid.add(tipoFactura, 1, 1);
        grid.add(new Label("CUIT:"), 0, 2);
        grid.add(txtCuit, 1, 2);
        grid.add(new Label("Sucursal:"), 0, 3);
        grid.add(txtSucursal, 1, 3);
        grid.add(new Label("Número de Factura:"), 0, 4);
        grid.add(txtNumFactura, 1, 4);
        grid.add(new Label("Fecha:"), 0, 5);
        grid.add(dpFecha, 1, 5);
        grid.add(new Label("No Gravado:"), 0, 6);
        grid.add(txtNoGravado, 1, 6);
        grid.add(new Label("IVA 21%:"), 0, 7);
        grid.add(txtIVA21, 1, 7);
        grid.add(new Label("IVA 10.5%:"), 0, 8);
        grid.add(txtIVA105, 1, 8);
        grid.add(new Label("Percepción IVA:"), 0, 9);
        grid.add(txtPercepcionIVA, 1, 9);
        grid.add(new Label("Ingresos Brutos:"), 0, 10);
        grid.add(txtIngresosBrutos, 1, 10);
        grid.add(new Label("Otros Impuestos:"), 0, 11);
        grid.add(txtOtrosImp, 1, 11);
        grid.add(btnGuardar, 0, 12);
        grid.add(btnVolver, 1, 12);

        Scene scene = new Scene(grid, 450, 600);
        stage.setScene(scene);

        final Factura[] facturaResultado = {null};

        btnGuardar.setOnAction(e -> {
            try {
                String tipo = tipoFactura.getValue();
                String cuit = txtCuit.getText().trim();
                String sucursalStr = txtSucursal.getText().trim();
                String numFactStr = txtNumFactura.getText().trim();
                LocalDate fecha = dpFecha.getValue();

                // Validación de campos obligatorios
                if (cuit.isEmpty() || sucursalStr.isEmpty() || numFactStr.isEmpty() ||
                        fecha == null || txtNoGravado.getText().isEmpty()) {
                    throw new CamposVaciosException("Debe completar los campos obligatorios: CUIT, Sucursal, Número de Factura y No Gravado");
                }

                if (!cuit.matches("\\d{11}") && !cuit.equals("1")) {
                    throw new Cuiterroneo("El CUIT debe tener 11 dígitos numéricos o ser '1' para consumidor final.");
                }

                int sucursal = Integer.parseInt(sucursalStr);
                int numFactura = Integer.parseInt(numFactStr);
                if (sucursal <= 0 || numFactura <= 0) {
                    mostrarMensaje("La sucursal y el número de factura deben ser mayores que 0.");
                    return;
                }

                double noGravado = parseDoubleSafe(txtNoGravado);
                double iva21 = txtIVA21.isDisabled() ? 0 : parseDoubleSafe(txtIVA21);
                double iva105 = txtIVA105.isDisabled() ? 0 : parseDoubleSafe(txtIVA105);
                double perIva = txtPercepcionIVA.isDisabled() ? 0 : parseDoubleSafe(txtPercepcionIVA);
                double ingBrutos = txtIngresosBrutos.isDisabled() ? 0 : parseDoubleSafe(txtIngresosBrutos);
                double otros = txtOtrosImp.isDisabled() ? 0 : parseDoubleSafe(txtOtrosImp);

                if (noGravado < 0 || iva21 < 0 || iva105 < 0 || perIva < 0 || ingBrutos < 0 || otros < 0) {
                    mostrarMensaje("Los importes no pueden ser negativos.");
                    return;
                }

                // Buscar cliente existente o crear uno nuevo
                GestoraEntidades gestora = empresa.getGestoraEntidades();
                Entidad personaEncontrada = gestora.buscarPorCuit(cuit, Cliente.class);
                Cliente cliente;

                if (personaEncontrada != null && personaEncontrada instanceof Cliente) {
                    cliente = (Cliente) personaEncontrada;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Cliente no encontrado");
                    alert.setHeaderText("No se encontró un cliente con ese CUIT.");
                    alert.setContentText("Se abrirá una ventana para crear un nuevo cliente.");
                    alert.showAndWait();

                    cliente = CrearEntidadFX.mostrarFormularioCliente(stage, cuit);
                    if (cliente == null) {
                        mostrarMensaje("Operación cancelada. No se cargó la factura.");
                        return;
                    } else {
                        gestora.agregarEntidad(cliente); // 🔹 Guardar cliente en lista y JSON
                    }
                }

                // Crear la factura según tipo
                Factura factura;
                switch (tipo) {
                    case "A" -> {
                        CargaFact<FacturaA> cargaA = new CargaFact<>(Tipodecomprobante.A);
                        factura = cargaA.crearFactura(noGravado, cuit, Tipodecomprobante.A, sucursal, numFactura, fecha, iva21, iva105, perIva, ingBrutos, otros);
                    }
                    case "B" -> {
                        CargaFact<FacturaB> cargaB = new CargaFact<>(Tipodecomprobante.B);
                        factura = cargaB.crearFactura(noGravado, cuit, Tipodecomprobante.B, sucursal, numFactura, fecha, iva21, iva105, perIva, ingBrutos, otros);
                    }
                    case "C" -> {
                        CargaFact<FacturaC> cargaC = new CargaFact<>(Tipodecomprobante.C);
                        factura = cargaC.crearFactura(noGravado, cuit, Tipodecomprobante.C, sucursal, numFactura, fecha, 0, 0, 0, 0, 0);
                    }
                    default -> {
                        mostrarMensaje("Tipo de factura inválido.");
                        return;
                    }
                }

                // Guardar la factura en empresa
                empresa.cargadeventa(factura);
                facturaResultado[0] = factura;
                mostrarMensaje("Factura cargada correctamente ");
                volverAlMenu.run();

            } catch (CamposVaciosException | Cuiterroneo ex) {
                mostrarMensaje(ex.getMessage());
            } catch (NumberFormatException nfe) {
                mostrarMensaje("Error en campos numéricos: " + nfe.getMessage());
            } catch (Exception ex) {
                mostrarMensaje("Error inesperado: " + ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> volverAlMenu.run());
        return facturaResultado[0];
    }




    //  Conversión segura
    private double parseDoubleSafe(TextField txt) {
        if (txt.getText().trim().isEmpty()) return 0;
        return Double.parseDouble(txt.getText().trim());
    }


    public void eliminarFacturaCompraFX(Stage stage, EmpresaCliente empresa, Runnable volverAlMenu) {
        TextField txtCuit = new TextField();
        txtCuit.setPromptText("CUIT del proveedor");

        TextField txtNumFactura = new TextField();
        txtNumFactura.setPromptText("Número de factura");

        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");

        VBox layout = new VBox(10,
                new Label("Eliminar Factura de Compra"),
                new Label("Ingrese CUIT y número de factura:"),
                txtCuit, txtNumFactura, btnEliminar, btnVolver
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);

        btnEliminar.setOnAction(e -> {
            try {
                //  Validar campos vacíos obligatorios
                if (txtCuit.getText().trim().isEmpty() || txtNumFactura.getText().trim().isEmpty()) {
                    throw new CamposVaciosException("Debe completar ambos campos: CUIT y Número de factura.");
                }

                String cuit = txtCuit.getText().trim();
                int numFactura;

                try {
                    numFactura = Integer.parseInt(txtNumFactura.getText().trim());
                } catch (NumberFormatException ex) {
                    throw new NumberFormatException("El número de factura debe ser un número entero válido.");
                }

                var lista = empresa.getListafacturadecompras();
                Factura facturaEncontrada = lista.stream()
                        .filter(f -> f.getCuit().equalsIgnoreCase(cuit) && f.getNumerodefactura() == numFactura)
                        .findFirst()
                        .orElse(null);

                if (facturaEncontrada == null) {
                    mostrarMensaje("No se encontró una factura con ese CUIT y número.");
                    return;
                }

                lista.remove(facturaEncontrada);
                mostrarMensaje(" Factura eliminada correctamente.");
                volverAlMenu.run();

            } catch (CamposVaciosException ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            } catch (Exception ex) {
                mostrarMensaje("Error inesperado: " + ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> volverAlMenu.run());
    }

    public void modificarFacturaCompraFX(Stage stage, EmpresaCliente empresa, Runnable volverAlMenu) {
        TextField txtCuit = new TextField();
        txtCuit.setPromptText("CUIT del proveedor");

        TextField txtNumFactura = new TextField();
        txtNumFactura.setPromptText("Número de factura");

        Button btnBuscar = new Button("Buscar");
        Button btnVolver = new Button("Volver");

        VBox layout = new VBox(10,
                new Label("Modificar Factura de Compra"),
                new Label("Ingrese CUIT y número de factura:"),
                txtCuit, txtNumFactura, btnBuscar, btnVolver
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);

        btnBuscar.setOnAction(e -> {
            try {
                // Validar campos vacíos
                if (txtCuit.getText().trim().isEmpty() || txtNumFactura.getText().trim().isEmpty()) {
                    throw new CamposVaciosException("Debe completar ambos campos: CUIT y Número de factura.");
                }

                String cuit = txtCuit.getText().trim();
                int numFactura;

                try {
                    numFactura = Integer.parseInt(txtNumFactura.getText().trim());
                } catch (NumberFormatException ex) {
                    throw new NumberFormatException("El número de factura debe ser un número entero válido.");
                }

                var lista = empresa.getListafacturadecompras();
                Factura factura = lista.stream()
                        .filter(f -> f.getCuit().equalsIgnoreCase(cuit) && f.getNumerodefactura() == numFactura)
                        .findFirst()
                        .orElse(null);

                if (factura == null) {
                    mostrarMensaje("️ No se encontró una factura con ese CUIT y número.");
                    return;
                }

                //  Abrir formulario de edición
                mostrarFormularioEdicionFactura(stage, factura, volverAlMenu);

            } catch (CamposVaciosException ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            } catch (Exception ex) {
                mostrarMensaje("Error inesperado: " + ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> volverAlMenu.run());
    }

    public void eliminarFacturaVentaFX(Stage stage, EmpresaCliente empresa, Runnable volverAlMenu) {
        TextField txtCuit = new TextField();
        txtCuit.setPromptText("CUIT del cliente");

        TextField txtNumFactura = new TextField();
        txtNumFactura.setPromptText("Número de factura");

        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");

        VBox layout = new VBox(10,
                new Label("Eliminar Factura de Venta"),
                new Label("Ingrese CUIT y número de factura:"),
                txtCuit, txtNumFactura, btnEliminar, btnVolver
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);

        btnEliminar.setOnAction(e -> {
            try {
                String cuit = txtCuit.getText().trim();
                String numeroFacturaTexto = txtNumFactura.getText().trim();

                //  Validación de campos vacíos
                if (cuit.isEmpty() || numeroFacturaTexto.isEmpty()) {
                    throw new CamposVaciosException(" Debe completar todos los campos antes de continuar.");
                }

                int numFactura = Integer.parseInt(numeroFacturaTexto);

                var lista = empresa.getListafacturadeventas();
                Factura facturaEncontrada = lista.stream()
                        .filter(f -> f.getCuit().equalsIgnoreCase(cuit) && f.getNumerodefactura() == numFactura)
                        .findFirst()
                        .orElse(null);

                if (facturaEncontrada == null) {
                    mostrarMensaje(" No se encontró una factura con ese CUIT y número.");
                    return;
                }

                lista.remove(facturaEncontrada);
                mostrarMensaje(" Factura de venta eliminada correctamente.");
                volverAlMenu.run();

            } catch (CamposVaciosException ex) {
                mostrarMensaje(ex.getMessage());
            } catch (NumberFormatException ex) {
                mostrarMensaje(" El número de factura debe ser un valor numérico.");
            } catch (Exception ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> volverAlMenu.run());
    }

    public void modificarFacturaVentaFX(Stage stage, EmpresaCliente empresa, Runnable volverAlMenu) {
        TextField txtCuit = new TextField();
        txtCuit.setPromptText("CUIT del cliente");

        TextField txtNumFactura = new TextField();
        txtNumFactura.setPromptText("Número de factura");

        Button btnBuscar = new Button("Buscar");
        Button btnVolver = new Button("Volver");

        VBox layout = new VBox(10,
                new Label("Modificar Factura de Venta"),
                new Label("Ingrese CUIT y número de factura:"),
                txtCuit, txtNumFactura, btnBuscar, btnVolver
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);

        btnBuscar.setOnAction(e -> {
            try {
                String cuit = txtCuit.getText().trim();
                String numeroFacturaTexto = txtNumFactura.getText().trim();

                //  Validación de campos vacíos
                if (cuit.isEmpty() || numeroFacturaTexto.isEmpty()) {
                    throw new CamposVaciosException(" Debe completar todos los campos antes de continuar.");
                }

                int numFactura = Integer.parseInt(numeroFacturaTexto);

                var lista = empresa.getListafacturadeventas();
                Factura factura = lista.stream()
                        .filter(f -> f.getCuit().equalsIgnoreCase(cuit) && f.getNumerodefactura() == numFactura)
                        .findFirst()
                        .orElse(null);

                if (factura == null) {
                    mostrarMensaje(" No se encontró una factura con ese CUIT y número.");
                    return;
                }

                //  Abre el formulario de edición
                mostrarFormularioEdicionFactura(stage, factura, volverAlMenu);

            } catch (CamposVaciosException ex) {
                mostrarMensaje(ex.getMessage());
            } catch (NumberFormatException ex) {
                mostrarMensaje(" El número de factura debe ser un valor numérico.");
            } catch (Exception ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> volverAlMenu.run());
    }

    private void mostrarFormularioEdicionFactura(Stage stage, Factura factura, Runnable volverAlMenu) {
        Label lblTitulo = new Label("Modificar " + factura.getClass().getSimpleName());
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(lblTitulo, 0, 0, 2, 1);

        int fila = 1;

        // --- Campos base comunes ---
        TextField txtCuit = new TextField(factura.getCuit());
        txtCuit.setEditable(false);
        TextField txtSucursal = new TextField(String.valueOf(factura.getSucursal()));
        TextField txtNumero = new TextField(String.valueOf(factura.getNumerodefactura()));
        TextField txtFecha = new TextField(String.valueOf(factura.getFecha()));

        grid.add(new Label("CUIT:"), 0, fila);
        grid.add(txtCuit, 1, fila++);
        grid.add(new Label("Sucursal:"), 0, fila);
        grid.add(txtSucursal, 1, fila++);
        grid.add(new Label("N° Factura:"), 0, fila);
        grid.add(txtNumero, 1, fila++);
        grid.add(new Label("Fecha:"), 0, fila);
        grid.add(txtFecha, 1, fila++);

        // --- Campos específicos según el tipo ---
        TextField txtNoGravado = new TextField();
        TextField txtIVA21 = new TextField();
        TextField txtIVA105 = new TextField();
        TextField txtPerIVA = new TextField();
        TextField txtIB = new TextField();
        TextField txtOtros = new TextField();
        TextField txtTotal = new TextField();

        if (factura instanceof FacturaA fA) {
            txtNoGravado.setText(String.valueOf(fA.getNogrado()));
            txtIVA21.setText(String.valueOf(fA.getIVA21()));
            txtIVA105.setText(String.valueOf(fA.getIVA105()));
            txtPerIVA.setText(String.valueOf(fA.getPercepcionIVA()));
            txtIB.setText(String.valueOf(fA.getPercepcionIB()));
            txtOtros.setText(String.valueOf(fA.getOtrosImpuestos()));

            grid.add(new Label("No Gravado:"), 0, fila);
            grid.add(txtNoGravado, 1, fila++);
            grid.add(new Label("IVA 21%:"), 0, fila);
            grid.add(txtIVA21, 1, fila++);
            grid.add(new Label("IVA 10.5%:"), 0, fila);
            grid.add(txtIVA105, 1, fila++);
            grid.add(new Label("Percepción IVA:"), 0, fila);
            grid.add(txtPerIVA, 1, fila++);
            grid.add(new Label("Percepción IB:"), 0, fila);
            grid.add(txtIB, 1, fila++);
            grid.add(new Label("Otros:"), 0, fila);
            grid.add(txtOtros, 1, fila++);

        } else if (factura instanceof FacturaB fB) {
            txtTotal.setText(String.valueOf(fB.getTotal()));
            txtOtros.setText(String.valueOf(fB.getOtrosImpuestos()));

            grid.add(new Label("Total (IVA incluido):"), 0, fila);
            grid.add(txtTotal, 1, fila++);
            grid.add(new Label("Otros:"), 0, fila);
            grid.add(txtOtros, 1, fila++);

        } else if (factura instanceof FacturaC fC) {
            txtTotal.setText(String.valueOf(fC.getTotal()));
            txtOtros.setText(String.valueOf(fC.getNogrado()));

            grid.add(new Label("Total:"), 0, fila);
            grid.add(txtTotal, 1, fila++);
            grid.add(new Label("No Gravado:"), 0, fila);
            grid.add(txtOtros, 1, fila++);
        }

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        grid.add(btnGuardar, 0, fila);
        grid.add(btnCancelar, 1, fila);

        Scene scene = new Scene(grid, 400, 500);
        stage.setScene(scene);

        // --- ACCIÓN GUARDAR ---
        btnGuardar.setOnAction(e -> {
            try {
                // Validación de campos vacíos
                if (txtSucursal.getText().trim().isEmpty() || txtNumero.getText().trim().isEmpty() ||
                        txtFecha.getText().trim().isEmpty()) {
                    throw new CamposVaciosException(" Todos los campos obligatorios deben estar completos.");
                }

                //  Validación de CUIT (aunque sea no editable, mantenemos la coherencia)
                String cuit = txtCuit.getText().trim();
                if (!cuit.matches("\\d{11}")) {
                    throw new Cuiterroneo(" El CUIT debe tener exactamente 11 dígitos numéricos.");
                }

                //  Validación numérica de los valores ingresados
                if (factura instanceof FacturaA fA) {
                    if (txtNoGravado.getText().trim().isEmpty() || txtIVA21.getText().trim().isEmpty() ||
                            txtIVA105.getText().trim().isEmpty() || txtPerIVA.getText().trim().isEmpty() ||
                            txtIB.getText().trim().isEmpty() || txtOtros.getText().trim().isEmpty()) {
                        throw new CamposVaciosException(" Debe completar todos los campos antes de guardar.");
                    }

                    fA.setNogrado(Double.parseDouble(txtNoGravado.getText()));
                    fA.setIVA21(Double.parseDouble(txtIVA21.getText()));
                    fA.setIVA105(Double.parseDouble(txtIVA105.getText()));
                    fA.setPercepcionIVA(Double.parseDouble(txtPerIVA.getText()));
                    fA.setPercepcionIB(Double.parseDouble(txtIB.getText()));
                    fA.setOtrosImpuestos(Double.parseDouble(txtOtros.getText()));

                } else if (factura instanceof FacturaB fB) {
                    if (txtTotal.getText().trim().isEmpty() || txtOtros.getText().trim().isEmpty()) {
                        throw new CamposVaciosException(" Debe completar todos los campos antes de guardar.");
                    }

                    fB.setTotal(Double.parseDouble(txtTotal.getText()));
                    fB.setOtrosImpuestos(Double.parseDouble(txtOtros.getText()));

                } else if (factura instanceof FacturaC fC) {
                    if (txtTotal.getText().trim().isEmpty() || txtOtros.getText().trim().isEmpty()) {
                        throw new CamposVaciosException(" Debe completar todos los campos antes de guardar.");
                    }

                    fC.setTotal(Double.parseDouble(txtTotal.getText()));
                    fC.setNogrado(Double.parseDouble(txtOtros.getText()));
                }

                mostrarMensaje("Factura modificada correctamente.");
                volverAlMenu.run();

            } catch (CamposVaciosException | Cuiterroneo ex) {
                mostrarMensaje(ex.getMessage());
            } catch (NumberFormatException ex) {
                mostrarMensaje("Los valores ingresados deben ser numéricos.");
            } catch (Exception ex) {
                mostrarMensaje("Error al guardar cambios: " + ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> volverAlMenu.run());
    }
}