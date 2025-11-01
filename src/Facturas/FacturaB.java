package Facturas;

import Facturas.Enums.Tipodecomprobante;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Scanner;

public class FacturaB extends Factura {
    private double neto21;
    private double neto105;
    private double PercepcionIVA;
    private double PercepcionIB;
    private double OtrosImpuestos;
    private double IVA21;
    private double IVA105;



    public FacturaB(double nogrado, String cuit, Tipodecomprobante tipo, int sucursal, int numerodefactura, LocalDate fecha, double neto21, double neto105, double percepcionIVA, double percepcionIB, double otrosImpuestos) {
        super(nogrado, cuit, tipo, sucursal, numerodefactura, fecha);
        this.neto21 = neto21;
        this.neto105 = neto105;
        PercepcionIVA = percepcionIVA;
        PercepcionIB = percepcionIB;
        OtrosImpuestos = otrosImpuestos;
        this.IVA21 = calculodeiva21();
        this.IVA105 = calculoiva105();
        setTotal(calculototal());
    }
    public double getNeto21() {
        return neto21;
    }

    public void setNeto21(double neto21) {
        this.neto21 = neto21;
    }

    public double getNeto105() {
        return neto105;
    }

    public void setNeto105(double neto105) {
        this.neto105 = neto105;
    }

    public double getPercepcionIVA() {
        return PercepcionIVA;
    }

    public void setPercepcionIVA(double percepcionIVA) {
        PercepcionIVA = percepcionIVA;
    }

    public double getPercepcionIB() {
        return PercepcionIB;
    }

    public void setPercepcionIB(double percepcionIB) {
        PercepcionIB = percepcionIB;
    }

    public double getOtrosImpuestos() {
        return OtrosImpuestos;
    }

    public void setOtrosImpuestos(double otrosImpuestos) {
        OtrosImpuestos = otrosImpuestos;
    }

    public double getIVA21() {
        return IVA21;
    }

    public void setIVA21(double IVA21) {
        this.IVA21 = IVA21;
    }

    public double getIVA105() {
        return IVA105;
    }

    public void setIVA105(double IVA105) {
        this.IVA105 = IVA105;
    }
    public double calculodeiva21(){
        return getNeto21()*0.21 ;
    }
    public double calculoiva105(){
        return getNeto105()*0.105;
    }



    @Override
    public double calculototal() {
        double total = getNogrado() + getNeto21() + getNeto105()
                + getPercepcionIVA() + getPercepcionIB() + getOtrosImpuestos() + getIVA21() + getIVA105();
        return total;
    }

    @Override
    public void cargaDatos(Scanner scanner) {

        System.out.print("Ingrese el no gravado: ");
        setNogrado(scanner.nextDouble());
        System.out.print("Ingrese el neto 21%: ");
        neto21 = scanner.nextDouble();
        System.out.print("Ingrese el neto 10.5%: ");
        neto105 = scanner.nextDouble();
        System.out.print("Ingrese la percepción de IVA: ");
        PercepcionIVA = scanner.nextDouble();
        System.out.print("Ingrese la percepción de IIBB: ");
        PercepcionIB = scanner.nextDouble();
        System.out.print("Ingrese otros impuestos: ");
        OtrosImpuestos = scanner.nextDouble();

        //recalculo de impuestos

        IVA21 = calculodeiva21();
        IVA105 = calculoiva105();
        setTotal(calculototal());


    }

    @Override
    public String toString() {
        return "\n========= FACTURA B =========" +
                "\nCUIT: " + getCuit() +
                "\nTipo: " + getTipo() +
                "\nSucursal: " + getSucursal() +
                "\nN° Factura: " + getNumerodefactura() +
                "\nFecha: " + getFecha() +
                "\n-----------------------------" +
                "\nNo Gravado: $" + String.format("%.2f", getNogrado()) +
                "\nNeto 21%%: $" + String.format("%.2f", neto21) +
                "\nIVA 21%%: $" + String.format("%.2f", IVA21) +
                "\nNeto 10.5%%: $" + String.format("%.2f", neto105) +
                "\nIVA 10.5%%: $" + String.format("%.2f", IVA105) +
                "\nOtros Impuestos: $" + String.format("%.2f", OtrosImpuestos) +
                "\n=============================" +
                "\nTOTAL: $" + String.format("%.2f", getTotal()) +
                "\n=============================\n";
    }
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        obj.put("neto21" , neto21);
        obj.put("neto105" , neto105);
        obj.put("percepcionIVA", PercepcionIVA);
        obj.put("percepcionIB", PercepcionIB);
        obj.put("Otros Impuestos", OtrosImpuestos);
        obj.put("Iva21", IVA21);
        obj.put("Iva 105", IVA105);
        return obj;
    }
}
