package FX;

import Entidades.EmpresaCliente;
import Manejodearchivos.JSONUtilesEmpresa;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONException;

/// MainfxExtiene de application obligatoriamente y su funcionamiento consiste en crear una empresacliente vacia(null) y trabajar con la copia que los
/// metodos le proveen se crean 3 botones , cargar/crear empresa que llama a loginfx el cual va a devolver la empresa elegida , carga facturas que recive
/// la empresa provista por el primer boton y si no salta una excepcion de empresavacia , y nos lleva a Menugeneral y el ultimo boton sirve para cerrar
/// la aplicacion
public class MainFX extends Application {

    private EmpresaCliente empresaActual = null;

    @Override
    public void start(Stage stage) {
        mostrarMenuPrincipal(stage);
    }


    public void mostrarMenuPrincipal(Stage stage) {
        stage.setTitle("Gestor de Empresa");

        // Etiqueta principal
        Label lblTitulo = new Label("Seleccione una opción:");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Botones del menú
        Button btnCargarEmpresa = new Button("Cargar / Cambiar Empresa");
        Button btnCargarFacturas = new Button("Cargar Facturas");
        Button btnSalir = new Button("Cerrar aplicación");

        // Diseño visual
        VBox layout = new VBox(15, lblTitulo, btnCargarEmpresa, btnCargarFacturas, btnSalir);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Escena principal
        Scene scene = new Scene(layout, 400, 250);
        stage.setScene(scene);
        stage.show();

        // Botón: Cargar / Cambiar Empresa
        btnCargarEmpresa.setOnAction(e -> {
            LoingInicialFX.mostrarMenu(stage, empresaSeleccionada -> {
                if (empresaSeleccionada != null) {
                    empresaActual = empresaSeleccionada;
                    mostrarAlerta("Empresa cargada: " + empresaActual.getNombredefantasia());


                }
            }, () -> {

                mostrarMenuPrincipal(stage);
            });
        });
        JSONUtilesEmpresa JSONUtilesEmpresa = new JSONUtilesEmpresa();
        //  Botón: Cargar Facturas
        btnCargarFacturas.setOnAction(e -> {
            try {
                if (empresaActual == null) {
                    mostrarAlerta("Debe cargar una empresa primero ⚠️");
                } else {
                    MenuGeneralFX.mostrar(stage, empresaActual, () -> mostrarMenuPrincipal(stage));
                    JSONUtilesEmpresa.escribir(empresaActual);
                    mostrarAlerta("Facturas cargadas correctamente ✅");
                }
            } catch (JSONException ex) {
                mostrarAlerta("Error al guardar la empresa: " + ex.getMessage());
            } catch (Exception ex) {
                mostrarAlerta("Error al cargar facturas: " + ex.getMessage());
            }
        });

        //  Botón: Salir
        btnSalir.setOnAction(e -> {
            mostrarAlerta("El programa se cerrará.");
            stage.close();
        });
    }
/// Mostrar informacion recibe un mensaje de las excepciones y muestra ese mensaje , me ahorro un sout en cada caso

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}