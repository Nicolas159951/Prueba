package Gestor;

import Entidades.Entidad;
import Entidades.GestoraEntidades;
import Entidades.Proveedor;
import Facturas.*;
import Facturas.Enums.Tipodecomprobante;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
public class Gestor {
   ///Se que parece mucho pero Cargafacturascompra y cargafacturasventa son lo mismo solo que venta tiene un filtro mas para poder cargar un monotributista
   /// los dos metodos funcionan igual practicamente se pide primero el tipo de factura y despues el cuit del provedor/cliente (en caso de ventas se agraga un 1)
   /// para indicar que es consumidor final. Posteriormente si detecta que el provedor o el cliente no esta cargado pide sus datos.
   /// Despues una vez que el provedor/cliente esta cargado pide los datos generales de la factura fecha , sucursal , etc
   /// para despues pedir los datos de la factura general y pasarselo a la clase CargaFact la cual recibe los datos de la factura y devuelve
   /// la factura ya como objeto la cual la funcion carga-compra o venta devuelve y es cargada en el arreglo
   /// Despues  esta verfactura que solo recorre el arreglo que se le pasa venta o compras
    GestoraEntidades gestoraEntidades = new GestoraEntidades();

    public Factura Cargadefacturascompra(Scanner scanner) {
        int tipo = 0;
        try {
            System.out.print("Ingrese tipo de factura (1=A, 2=B, 3=C): ");
            tipo = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("⚠️ Entrada inválida. Debe ingresar un número entre 1 y 3.");
            scanner.nextLine();
            return null;
        }
        if (tipo < 1 || tipo > 3) {
            System.out.println("⚠️ Tipo de factura inválido. Debe ser 1, 2 o 3.");
            return null;
        }

        System.out.print("Ingrese el CUIT: ");
        String cuit = scanner.next();

        Entidad personaEncontrada = gestoraEntidades.buscarPorCuit(cuit);
        Proveedor proveedor;
        if (personaEncontrada != null && personaEncontrada instanceof Proveedor) {
            proveedor = (Proveedor) personaEncontrada;
            System.out.println("Proveedor encontrado: " + proveedor);
        } else {
            System.out.println("Proveedor no encontrado. Ingrese datos del nuevo proveedor:");

            scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();

            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();

            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Rubro: ");
            String rubro = scanner.nextLine();

            proveedor = new Proveedor(nombre, apellido, cuit, direccion, telefono, email, rubro);
            gestoraEntidades.agregarProveedor(proveedor);
        }
            System.out.print("Ingrese número de sucursal: ");
            int sucursal = scanner.nextInt();

            System.out.print("Ingrese número de factura: ");
            int numFactura = scanner.nextInt();
             System.out.println("Ingrese la fecha , con este formato yyyy-MM-dd (año-mes-día).");
             scanner.nextLine();
             String fechaTexto = scanner.nextLine();
             LocalDate fecha = LocalDate.parse(fechaTexto);

            Factura factura = null;

            switch (tipo) {
                case 1 -> {
                    CargaFact<FacturaA> cargaA = new CargaFact<>(Tipodecomprobante.A);
                    System.out.println("Ingrese el iva 21%");
                    double iva21 = scanner.nextInt();
                    System.out.println("Ingrese el iva 10.5%");
                    double iva105 = scanner.nextInt();
                    System.out.println("Percepcion de iva");
                    double per = scanner.nextInt();
                    System.out.println("Percepcion de ingresos brutos");
                    double preIB = scanner.nextInt();
                    System.out.println("Otros impuestos");
                    double otros = scanner.nextInt();
                    System.out.println("No gravado");
                    double nogravado = scanner.nextInt();

                    factura = cargaA.crearFactura(nogravado , cuit , Tipodecomprobante.A , sucursal , numFactura , fecha , iva21 , iva105 , per , preIB , otros);
                }
                case 2 -> {
                    CargaFact<FacturaB> cargaB = new CargaFact<>(Tipodecomprobante.B);
                    System.out.println("Ingrese el iva 21%");
                    double iva21 = scanner.nextInt();
                    System.out.println("Ingrese el iva 10.5%");
                    double iva105 = scanner.nextInt();
                    System.out.println("Percepcion de iva");
                    double per = scanner.nextInt();
                    System.out.println("Percepcion de ingresos brutos");
                    double preIB = scanner.nextInt();
                    System.out.println("Otros impuestos");
                    double otros = scanner.nextInt();
                    System.out.println("No gravado");
                    double nogravado = scanner.nextInt();
                    factura = cargaB.crearFactura(nogravado , cuit , Tipodecomprobante.B , sucursal , numFactura , fecha , iva21 , iva105 , per , preIB , otros);
                }
                case 3 -> {
                    CargaFact<FacturaC> cargaC = new CargaFact<>(Tipodecomprobante.C);
                    System.out.println("No gravado");
                    double nogravado = scanner.nextInt();
                    double iva21 = 0;
                    double iva105 = 0;
                    double per = 0;
                    double preIB = 0;
                    double otros = 0;
                    factura = cargaC.crearFactura(nogravado , cuit , Tipodecomprobante.C , sucursal , numFactura , fecha , iva21 , iva105 , per , preIB , otros);
                }
                default -> System.out.println("Tipo de factura inválido.");
            }

         return factura ;

    }

        public void verFacturas(ArrayList<Factura> Lista) {

            if (Lista.isEmpty()) {
                System.out.println("No hay facturas cargadas.");
                return;
            }

            System.out.println("=== LISTADO DE FACTURAS ===");
            int index = 1;
            for (Factura factura : Lista) {
                System.out.println(index++ + ". " + factura);
                System.out.println("----------------------------");
            }
        }
    public Factura Cargadevacturasventas(Scanner scanner){
        int tipo = 0;
        try {
            System.out.print("Ingrese tipo de factura (1=A, 2=B, 3=C): ");
            tipo = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("⚠️ Entrada inválida. Debe ingresar un número entre 1 y 3.");
            scanner.nextLine();
            return null;
        }
        if (tipo < 1 || tipo > 3) {
            System.out.println("⚠️ Tipo de factura inválido. Debe ser 1, 2 o 3.");
            return null;
        }
        System.out.println("Ingrese el Cuil o 1 si es para consumidor final");
        String cuit = scanner.next();
        Entidad personaEncontrada = gestoraEntidades.buscarPorCuit(cuit);
        Proveedor proveedor;
        if (personaEncontrada != null && personaEncontrada instanceof Proveedor || cuit.equalsIgnoreCase("1")) {
            if(cuit.equalsIgnoreCase("1")){
                System.out.println("Se ingreso consumidor final");
            }
            else {
                proveedor = (Proveedor) personaEncontrada;
                System.out.println("Proveedor encontrado: " + proveedor);
            }
        } else {
            System.out.println("Proveedor no encontrado. Ingrese datos del nuevo proveedor:");

            scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();

            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();

            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Rubro: ");
            String rubro = scanner.nextLine();

            proveedor = new Proveedor(nombre, apellido, cuit, direccion, telefono, email, rubro);
            gestoraEntidades.agregarProveedor(proveedor);
        }
        System.out.print("Ingrese número de sucursal: ");
        int sucursal = scanner.nextInt();

        System.out.print("Ingrese número de factura: ");
        int numFactura = scanner.nextInt();
        System.out.println("Ingrese la fecha , con este formato yyyy-MM-dd (año-mes-día).");
        scanner.nextLine();
        String fechaTexto = scanner.nextLine();
        LocalDate fecha = LocalDate.parse(fechaTexto);

        Factura factura = null;

        switch (tipo) {
            case 1 -> {
                CargaFact<FacturaA> cargaA = new CargaFact<>(Tipodecomprobante.A);
                System.out.println("Ingrese el iva 21%");
                double iva21 = scanner.nextInt();
                System.out.println("Ingrese el iva 10.5%");
                double iva105 = scanner.nextInt();
                System.out.println("Percepcion de iva");
                double per = scanner.nextInt();
                System.out.println("Percepcion de ingresos brutos");
                double preIB = scanner.nextInt();
                System.out.println("Otros impuestos");
                double otros = scanner.nextInt();
                System.out.println("No gravado");
                double nogravado = scanner.nextInt();

                factura = cargaA.crearFactura(nogravado , cuit , Tipodecomprobante.A , sucursal , numFactura , fecha , iva21 , iva105 , per , preIB , otros);
            }
            case 2 -> {
                CargaFact<FacturaB> cargaB = new CargaFact<>(Tipodecomprobante.B);
                System.out.println("Ingrese el iva 21%");
                double iva21 = scanner.nextInt();
                System.out.println("Ingrese el iva 10.5%");
                double iva105 = scanner.nextInt();
                System.out.println("Percepcion de iva");
                double per = scanner.nextInt();
                System.out.println("Percepcion de ingresos brutos");
                double preIB = scanner.nextInt();
                System.out.println("Otros impuestos");
                double otros = scanner.nextInt();
                System.out.println("No gravado");
                double nogravado = scanner.nextInt();
                factura = cargaB.crearFactura(nogravado , cuit , Tipodecomprobante.B , sucursal , numFactura , fecha , iva21 , iva105 , per , preIB , otros);
            }
            case 3 -> {
                CargaFact<FacturaC> cargaC = new CargaFact<>(Tipodecomprobante.C);
                System.out.println("No gravado");
                double nogravado = scanner.nextInt();
                double iva21 = 0;
                double iva105 = 0;
                double per = 0;
                double preIB = 0;
                double otros = 0;
                factura = cargaC.crearFactura(nogravado , cuit , Tipodecomprobante.C , sucursal , numFactura , fecha , iva21 , iva105 , per , preIB , otros);
            }
            default -> System.out.println("Tipo de factura inválido.");
        }

        return factura ;

    }
    }


