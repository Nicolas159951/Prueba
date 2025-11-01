package Facturas;
import Facturas.Enums.Tipodecomprobante;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Scanner;

public abstract class Factura {
    private double nogrado;
    private double Total;
    private String Cuit ;
    private Tipodecomprobante Tipo ;
    private int sucursal;
    private int numerodefactura ;
    private LocalDate fecha ;

    public Factura(double nogrado, String cuit, Tipodecomprobante tipo, int sucursal, int numerodefactura, LocalDate fecha) {
        this.nogrado = nogrado;
        Cuit = cuit;
        Tipo = tipo;
        this.sucursal = sucursal;
        this.numerodefactura = numerodefactura;
        this.fecha = fecha;
    }

    public String getCuit() {
        return Cuit;
    }

    public void setCuit(String cuit) {
        Cuit = cuit;
    }

    public Tipodecomprobante getTipo() {
        return Tipo;
    }

    public void setTipo(Tipodecomprobante tipo) {
        Tipo = tipo;
    }

    public int getSucursal() {
        return sucursal;
    }

    public void setSucursal(int sucursal) {
        this.sucursal = sucursal;
    }

    public int getNumerodefactura() {
        return numerodefactura;
    }

    public void setNumerodefactura(int numerodefactura) {
        this.numerodefactura = numerodefactura;
    }

    public double getNogrado() {
        return nogrado;
    }

    public void setNogrado(double nogrado) {
        this.nogrado = nogrado;
    }

    public double getTotal() {
        return Total;
    }


    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double setTotal(double total) {
        Total = total;
        return total;
    }



    @Override
    public String toString() {
        return "Factura A - CUIT: " + getCuit() +
                ", Fecha: " + getFecha() +
                ", Total: " + getTotal();
    }

    public abstract double calculototal();


    public abstract void cargaDatos(Scanner scanner);
    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("nogravado" , nogrado);
        obj.put("Total" , Total);
        obj.put("Cuit" , Cuit);
        obj.put("tipo" , Tipo.toString());
        obj.put("sucursal" , sucursal);
        obj.put("Numero de factura" , numerodefactura);
        obj.put("Fecha" , fecha.toString());

        return obj;
    }

}