import Entidades.EmpresaCliente;
import Excepciones.Empresavacia;
import Gestor.Gestor;
import Manejodearchivos.JSONUtilesEmpresa;
import Menus.LoingInicial;
import Menus.MenuGeneral;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws JSONException {
    Scanner scanner = new Scanner(System.in);
    EmpresaCliente p1 = new EmpresaCliente();
    p1= null;
    int opc ;
    do {
        System.out.println("Ingrese lo que desea");
        System.out.println("0-Cerrar la aplicacion");
        System.out.println("1-Cargar una nueva empresa/Cambiar de empresa");
        System.out.println("2-Cargar Facturas");
        try {
            opc = scanner.nextInt();
        }catch (InputMismatchException e) {
            System.out.println("⚠️ Entrada inválida. Debe ingresar un número.");
            scanner.nextLine();
            opc = -1;
        }
            switch (opc) {
                case 0: {
                    System.out.println("El programa se cerrrara"); ///Aca va el gatito Lau
                }
                break;
                case 1: {
                    p1 = LoingInicial.menuinicial(scanner);
                }
                break;
                case 2: {
                    if (p1 == null) {
                        System.out.println("Debe cargar una empresa primero");
                    }else {
                        MenuGeneral.menu(scanner, p1);
                        JSONUtilesEmpresa.escribirEmpresa(p1);
                    }
                }
                break;
                default: {
                }
                break;
            }

        } while (opc != 0) ;
    /// Explicacion de como funciona el main se debe cargar primero una empresa llamando al case 1 , apartir de ahi se da la opcion de 1 entrar con una empresa
    /// ya existente lo cual es obligatorio o 2 crear una empresa nueva cargar sus datos y elegir la opcion 1 , ambas opciones llaman a Log in el cual tiene las
    /// funciones para crear o cargar la empresa. Una vez la empresa esta cargada las funciones de log in devuelve el objeto de esa empresa y apartir de ahi
    /// podemos ir a la opcion dos "Carga facturas" , la cual nos lleva al menu principal y cuando salimos de el guarda los datos en el archivo

    /// Cosas a corregir/añadir:
    /// Tenemos que hacer algo con el provedor o cliente , ya que aunque lo guardamos en un arreglo esa informacion no se usa o se pierde al cerrar la aplicacion
    /// lo que se me ocurre es en la factura añadir una atributo el cual sea el nombre del provedor.

    /// En log in abria que añadir una opcion para modificar los datos de la empresa.

    ///Añadir validaciones "Prueben el codigo y traten de romperlo, ubiquen porque sucede y añadan o una excepcion o una validacion" , principalmente en la carga
    /// de los datos de la factura ocurre mucho.
    /// De lo que pide el tp solo falta la interfas , que no se donde meterla



    }
}