package Facturas;

import Facturas.Enums.Tipodecomprobante;

import java.lang.reflect.Constructor;
import java.time.LocalDate;

public class CargaFact<T extends Factura>{

    private Tipodecomprobante tipo; // en lugar de Class<T>

    public CargaFact(Tipodecomprobante tipo) {
        this.tipo = tipo;
    }

    public Factura crearFactura(double nogravado , String cuit , Tipodecomprobante tipo , int sucursal , int numerodefacutra ,LocalDate fecha , double neto21, double neto105, double percepcionIVA, double percepcionIB, double otrosImpuestos) {
        Factura factura = null;

        switch (tipo) {
            case A:
                factura = new FacturaA( nogravado ,  cuit ,  tipo ,  sucursal ,  numerodefacutra , fecha ,  neto21, neto105,  percepcionIVA,  percepcionIB, otrosImpuestos);
                break;
            case B:
                factura = new FacturaB(nogravado ,  cuit ,  tipo ,  sucursal ,  numerodefacutra , fecha ,  neto21, neto105,  percepcionIVA,  percepcionIB, otrosImpuestos);
                break;
            case C:
                factura = new FacturaC(nogravado ,  cuit ,  tipo ,  sucursal ,  numerodefacutra , fecha );
                break;
            default:
                System.out.println("⚠️ Tipo de factura no reconocido.");
        }

        return factura;
    }

}