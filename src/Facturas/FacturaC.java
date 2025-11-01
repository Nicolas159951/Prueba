package Facturas;

import Facturas.Enums.Tipodecomprobante;
import org.json.JSONException;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Scanner;

public class FacturaC extends Factura {

    public FacturaC(double nogrado, String cuit, Tipodecomprobante tipo, int sucursal, int numerodefactura, LocalDate fecha) {
        super(nogrado, cuit, tipo, sucursal, numerodefactura, fecha);
        setTotal(calculototal());
    }


    @Override
    public double calculototal() {
        return getNogrado();
    }

    @Override
    public void cargaDatos(Scanner scanner) {

        System.out.print("Ingrese el no gravado: ");
        setNogrado(scanner.nextDouble());

    }

    @Override
    public String toString() {
        return "\n========= FACTURA C =========" +
                "\nCUIT: " + getCuit() +
                "\nTipo: " + getTipo() +
                "\nSucursal: " + getSucursal() +
                "\nN° Factura: " + getNumerodefactura() +
                "\nFecha: " + getFecha() +
                "\n-----------------------------" +
                "\nNo Gravado: $" + String.format("%.2f", getNogrado()) +
                "\n=============================" +
                "\nTOTAL: $" + String.format("%.2f", getTotal()) +
                "\n=============================\n";
    }
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        return obj;
    }
}
