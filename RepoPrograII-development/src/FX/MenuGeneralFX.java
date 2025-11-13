package FX;

import Entidades.EmpresaCliente;
import Facturas.Factura;
import Manejodearchivos.JSONUtilesEmpresa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import GestorFX.GestorFX;
/// MenuGeneral sirve como puente entre la carga de facturas y las empresas ya que apartir de los arreglos de facturacompra y venta es que funciona
/// esta dividido en dos columnas una para compra y otras para venta para diferenciar visualmente el uso de los dos arreglos
/// Cuando se utilizan los metodos de venta se llama a su correspondiente arreglo y se pide la carga de la factura a , b o c mediente el GestorFx , cuando
/// se vuelve del gestro se guardan los datos en el arreglo y cuando se toca el boton volver al menu principal se guardan los datos en el archivo
public class MenuGeneralFX {

    public static void mostrar(Stage stage, EmpresaCliente empresa, Runnable volverAlMenuPrincipal) {
        GestorFX gestorFX = new GestorFX();

        Label titulo = new Label("Menú de Facturación - " + empresa.getNombredefantasia());
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        //  Botones de COMPRAS
        Button btnCargarCompra = new Button("Cargar factura de compra");
        Button btnVerCompras = new Button("Ver facturas de compra");
        Button btnEliminarCompra = new Button("Eliminar factura de compra");
        Button btnModificarCompra = new Button("Modificar factura de compra");

        VBox columnaCompras = new VBox(10, btnCargarCompra, btnVerCompras, btnEliminarCompra, btnModificarCompra);
        columnaCompras.setAlignment(Pos.CENTER);
        columnaCompras.setPadding(new Insets(10));
        columnaCompras.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label lblCompras = new Label("Facturas de Compra");
        lblCompras.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        VBox cajaCompra = new VBox(10, lblCompras, columnaCompras);
        cajaCompra.setAlignment(Pos.CENTER);

        //  Botones de VENTAS
        Button btnCargarVenta = new Button("Cargar factura de venta");
        Button btnVerVentas = new Button("Ver facturas de venta");
        Button btnEliminarVenta = new Button("Eliminar factura de venta");
        Button btnModificarVenta = new Button("Modificar factura de venta");

        VBox columnaVentas = new VBox(10, btnCargarVenta, btnVerVentas, btnEliminarVenta, btnModificarVenta);
        columnaVentas.setAlignment(Pos.CENTER);
        columnaVentas.setPadding(new Insets(10));
        columnaVentas.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label lblVentas = new Label("Facturas de Venta");
        lblVentas.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        VBox cajaVenta = new VBox(10, lblVentas, columnaVentas);
        cajaVenta.setAlignment(Pos.CENTER);

        //  Layout con dos columnas
        HBox columnas = new HBox(40, cajaCompra, cajaVenta);
        columnas.setAlignment(Pos.CENTER);

        Button btnVolver = new Button("Volver al menú principal");

        VBox root = new VBox(25, titulo, columnas, btnVolver);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 550, 500);
        stage.setScene(scene);
        stage.setTitle("Gestión de Facturas");
        stage.show();

        //  ACCIONES
        // Cargar factura de compra
        btnCargarCompra.setOnAction(e -> {
            Factura nuevaFactura = gestorFX.cargarFacturaCompraFX(stage, empresa, () -> mostrar(stage, empresa, volverAlMenuPrincipal));
            if (nuevaFactura != null) {
                empresa.getListafacturadecompras().add(nuevaFactura);
                mostrarAlerta("Factura de compra agregada correctamente a " + empresa.getNombredefantasia());
            }
        });

        // Cargar factura de venta
        btnCargarVenta.setOnAction(e -> {
            Factura nuevaFactura = gestorFX.cargarFacturaVentaFX(stage, empresa, () -> mostrar(stage, empresa, volverAlMenuPrincipal));
            if (nuevaFactura != null) {
                empresa.getListafacturadeventas().add(nuevaFactura);
                mostrarAlerta("Factura de venta agregada correctamente a " + empresa.getNombredefantasia());
            }
        });

        // Ver facturas
        btnVerVentas.setOnAction(e -> gestorFX.verFacturasFX(stage, empresa.getListafacturadeventas(), () -> mostrar(stage, empresa, volverAlMenuPrincipal)));
        btnVerCompras.setOnAction(e -> gestorFX.verFacturasFX(stage, empresa.getListafacturadecompras(), () -> mostrar(stage, empresa, volverAlMenuPrincipal)));

        // Eliminar
        btnEliminarCompra.setOnAction(e -> gestorFX.eliminarFacturaCompraFX(stage, empresa, () -> mostrar(stage, empresa, volverAlMenuPrincipal)));
        btnEliminarVenta.setOnAction(e -> gestorFX.eliminarFacturaVentaFX(stage, empresa, () -> mostrar(stage, empresa, volverAlMenuPrincipal)));

        // Modificar
        btnModificarCompra.setOnAction(e -> gestorFX.modificarFacturaCompraFX(stage, empresa, () -> mostrar(stage, empresa, volverAlMenuPrincipal)));
        btnModificarVenta.setOnAction(e -> gestorFX.modificarFacturaVentaFX(stage, empresa, () -> mostrar(stage, empresa, volverAlMenuPrincipal)));

        // Guardar y volver
        JSONUtilesEmpresa jsonUtil = new JSONUtilesEmpresa();
        btnVolver.setOnAction(e -> {
            try {
                jsonUtil.escribir(empresa);
                mostrarAlerta("Cambios guardados correctamente en el archivo JSON.");
                volverAlMenuPrincipal.run();
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error al guardar los datos en el JSON.");
            }
        });
    }

    private static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}