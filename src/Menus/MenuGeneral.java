package Menus;

import Entidades.EmpresaCliente;
import Gestor.Gestor;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuGeneral  {
    public static void menu (Scanner scanner , EmpresaCliente p1) {
        Gestor gestor = new Gestor();
        int opc;
        do {
            System.out.println("Ingrese lo que Desea");
            System.out.println("0-Salir");
            System.out.println("1-Cargar factura de compra");
            System.out.println("2-Ver facturas cargadas de compra");
            System.out.println("3-Cargar facturas venta");
            System.out.println("4-Ver facturas cargadas de Venta");
            try {
                opc = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Entrada inválida. Debe ingresar un número.");
                scanner.nextLine();
                opc = -1;
            }
            switch (opc) {
                case 0: {
                    System.out.println("El programa se cerrara");
                }
                break;
                case 1: {
                    p1.cargadecompra(gestor.Cargadefacturascompra(scanner));
                }
                break;
                case 2: {
                    gestor.verFacturas(p1.getListafacturadecompras());
                }
                break;
                case 3:{
                   p1.cargadeventa(gestor.Cargadevacturasventas(scanner));
                }break;
                case 4:{
                    gestor.verFacturas(p1.getListafacturadeventas());
                }break;
                default: {
                    System.out.println("Error");
                }
                break;
            }
        } while (opc != 0);
    }
}
///La clase menu principal lo unico que hace es llamar a los diferentes metodos de la clase gestora la cual es la que se dedica de cargar o visualizar facturas