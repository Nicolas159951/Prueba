package FX;

import Entidades.Cliente;
import Entidades.Proveedor;
import Excepciones.CamposVaciosException;
import Excepciones.Cuiterroneo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class CrearEntidadFX {

    //  Crear un nuevo Cliente
    public static Cliente mostrarFormularioCliente(Stage owner, String cuitPreCargado) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nuevo Cliente");

        TextField txtNombre = new TextField();
        TextField txtApellido = new TextField();
        TextField txtDireccion = new TextField();
        TextField txtTelefono = new TextField();
        TextField txtEmail = new TextField();
        TextField txtTipo = new TextField();
        TextField txtCuit = new TextField(cuitPreCargado);
        txtCuit.setEditable(false);

        // Evitar números en los campos de texto donde no corresponden
        txtNombre.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                txtNombre.setText(old);
            }
        });

        txtApellido.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                txtApellido.setText(old);
            }
        });

        txtTipo.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                txtTipo.setText(old);
            }
        });

        // Solo números en teléfono
        txtTelefono.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("\\d*")) {
                txtTelefono.setText(old);
            }
        });

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Apellido:"), txtApellido);
        grid.addRow(2, new Label("Dirección:"), txtDireccion);
        grid.addRow(3, new Label("Teléfono:"), txtTelefono);
        grid.addRow(4, new Label("Email:"), txtEmail);
        grid.addRow(5, new Label("Tipo Cliente:"), txtTipo);
        grid.addRow(6, new Label("CUIT/CUIL:"), txtCuit);

        VBox layout = new VBox(10, grid, new VBox(10, btnGuardar, btnCancelar));
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 350, 420);
        dialog.setScene(scene);

        final Cliente[] clienteCreado = {null};

        btnGuardar.setOnAction(e -> {
            try {
                // Validar campos vacíos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtApellido.getText().trim().isEmpty() ||
                        txtDireccion.getText().trim().isEmpty() ||
                        txtTelefono.getText().trim().isEmpty() ||
                        txtEmail.getText().trim().isEmpty() ||
                        txtTipo.getText().trim().isEmpty() ||
                        txtCuit.getText().trim().isEmpty()) {
                    throw new CamposVaciosException("⚠️ Todos los campos son obligatorios.");
                }

                // Validar CUIT/CUIL (solo números y 11 dígitos)
                String cuit = txtCuit.getText().trim();
                if (!cuit.matches("\\d{11}")) {
                    throw new Cuiterroneo("⚠️ El CUIT/CUIL debe tener 11 dígitos numéricos.");
                }

                clienteCreado[0] = new Cliente(
                        txtNombre.getText().trim(),
                        txtApellido.getText().trim(),
                        cuit,
                        txtDireccion.getText().trim(),
                        txtTelefono.getText().trim(),
                        txtEmail.getText().trim(),
                        txtTipo.getText().trim()
                );
                dialog.close();

            } catch (CamposVaciosException | Cuiterroneo ex) {
                mostrarAlerta(ex.getMessage());
            } catch (Exception ex) {
                mostrarAlerta(" Error al guardar el cliente: " + ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> dialog.close());

        dialog.showAndWait();
        return clienteCreado[0];
    }
    //  Crear un nuevo Proveedor
    public static Proveedor mostrarFormularioProveedor(Stage owner, String cuitPreCargado) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nuevo Proveedor");

        TextField txtNombre = new TextField();
        TextField txtApellido = new TextField();
        TextField txtDireccion = new TextField();
        TextField txtTelefono = new TextField();
        TextField txtEmail = new TextField();
        TextField txtRubro = new TextField();
        TextField txtCuit = new TextField(cuitPreCargado);
        txtCuit.setEditable(false);

        // Evitar números en campos de texto donde no corresponden
        txtNombre.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                txtNombre.setText(old);
            }
        });

        txtApellido.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                txtApellido.setText(old);
            }
        });

        txtRubro.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                txtRubro.setText(old);
            }
        });

        // Solo números en teléfono
        txtTelefono.textProperty().addListener((obs, old, nuevo) -> {
            if (!nuevo.matches("\\d*")) {
                txtTelefono.setText(old);
            }
        });

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Apellido:"), txtApellido);
        grid.addRow(2, new Label("Dirección:"), txtDireccion);
        grid.addRow(3, new Label("Teléfono:"), txtTelefono);
        grid.addRow(4, new Label("Email:"), txtEmail);
        grid.addRow(5, new Label("Rubro:"), txtRubro);
        grid.addRow(6, new Label("CUIT/CUIL:"), txtCuit);

        VBox layout = new VBox(10, grid, new VBox(10, btnGuardar, btnCancelar));
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 350, 420);
        dialog.setScene(scene);

        final Proveedor[] proveedorCreado = {null};

        btnGuardar.setOnAction(e -> {
            try {
                // Validar campos vacíos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtApellido.getText().trim().isEmpty() ||
                        txtDireccion.getText().trim().isEmpty() ||
                        txtTelefono.getText().trim().isEmpty() ||
                        txtEmail.getText().trim().isEmpty() ||
                        txtRubro.getText().trim().isEmpty() ||
                        txtCuit.getText().trim().isEmpty()) {
                    throw new CamposVaciosException("⚠️ Todos los campos son obligatorios.");
                }

                // Validar CUIT/CUIL (solo números y 11 dígitos)
                String cuit = txtCuit.getText().trim();
                if (!cuit.matches("\\d{11}")) {
                    throw new Cuiterroneo(" El CUIT/CUIL debe tener 11 dígitos numéricos.");
                }

                proveedorCreado[0] = new Proveedor(
                        txtNombre.getText().trim(),
                        txtApellido.getText().trim(),
                        cuit,
                        txtDireccion.getText().trim(),
                        txtTelefono.getText().trim(),
                        txtEmail.getText().trim(),
                        txtRubro.getText().trim()
                );
                dialog.close();

            } catch (CamposVaciosException | Cuiterroneo ex) {
                mostrarAlerta(ex.getMessage());
            } catch (Exception ex) {
                mostrarAlerta("Error al guardar el proveedor: " + ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> dialog.close());

        dialog.showAndWait();
        return proveedorCreado[0];
    }


    private static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}