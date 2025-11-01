package Menus;

import Entidades.EmpresaCliente;
import Manejodearchivos.JSONUtilesEmpresa;
import org.json.JSONException;

import java.util.Scanner;

public class LoingInicial {

    public static EmpresaCliente menuinicial(Scanner scanner) throws JSONException {
        EmpresaCliente empresaCliente = new EmpresaCliente();
        int opc;
        do{
            System.out.println("Ingrese la opcion que desea ");
            System.out.println("0-Cerrar la aplicacion");
            System.out.println("1-Ingresar con una empresa ya existente");
            System.out.println("2-Crear una nueva empresa");
            opc = scanner.nextInt();
            scanner.nextLine();
            switch (opc){
                case 0:{
                    System.out.println("El programa se cerrara");

                } break;
                case 1:{
                    System.out.println("Ingrese el Cuit de la empresa o su nombre");
                    String nombrecuit = scanner.nextLine();
                    empresaCliente = JSONUtilesEmpresa.leerEmpresaPorDato(nombrecuit);

                } break;
                case 2:{
                   EmpresaCliente p1 = empresaCliente.creaciondeempresa(scanner);
                   JSONUtilesEmpresa.escribirEmpresa(p1);
                   empresaCliente = null;
                } break;
                default:{
                    System.out.println("Error");
                    empresaCliente = null;
                } break;

            }

        }while (opc !=0 && opc!=1);
        return  empresaCliente;
    }
}
