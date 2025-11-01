package Entidades;

import Entidades.Enums.TipoCondiciondeIva;
import Excepciones.TipodeEmpresa;
import Facturas.Factura;

import java.util.ArrayList;
import java.util.Scanner;

public class EmpresaCliente {
    private String AliasEmpresa;
    private String RazonSocial;
    private String Nombredefantasia;
    private String Cuit;
    private TipoCondiciondeIva Condiciondeiva;
    private String IB;
    private String actividad;
    private String Direccion;
    private String Localidad;
    private String Provincia;
    private String Telefono;
    private String CorreoElectronico;
    private String Paginaweb;
    private ArrayList<Factura> Listafacturadecompras ;
    private ArrayList<Factura> Listafacturadeventas ;

    public EmpresaCliente() {
    }

    public EmpresaCliente(String aliasEmpresa, String razonSocial, String nombredefantasia, String cuit, TipoCondiciondeIva condiciondeiva, String IB, String actividad, String direccion, String localidad, String provincia, String telefono, String correoElectronico, String paginaweb, ArrayList<Factura> listafacturadecompras, ArrayList<Factura> listafacturadeventas) {
        AliasEmpresa = aliasEmpresa;
        RazonSocial = razonSocial;
        Nombredefantasia = nombredefantasia;
        Cuit = cuit;
        Condiciondeiva = condiciondeiva;
        this.IB = IB;
        this.actividad = actividad;
        Direccion = direccion;
        Localidad = localidad;
        Provincia = provincia;
        Telefono = telefono;
        CorreoElectronico = correoElectronico;
        Paginaweb = paginaweb;
        Listafacturadecompras = listafacturadecompras;
        Listafacturadeventas = listafacturadeventas;

    }

    public EmpresaCliente(String aliasEmpresa, String razonSocial, String nombredefantasia, String cuit, int condiciondeiva, String IB, String actividad, String direccion, String localidad, String provincia, String telefono, String correoElectronico, String paginaweb) {
        AliasEmpresa = aliasEmpresa;
        RazonSocial = razonSocial;
        Nombredefantasia = nombredefantasia;
        Cuit = cuit;
        Condiciondeiva = asignaciondeiva(condiciondeiva);
        this.IB = IB;
        this.actividad = actividad;
        Direccion = direccion;
        Localidad = localidad;
        Provincia = provincia;
        this.Telefono = (telefono == null) ? "" : telefono;
        this.CorreoElectronico = (correoElectronico == null) ? "" : correoElectronico;
        this.Paginaweb = (paginaweb == null) ? "" : paginaweb;
        this.Listafacturadecompras = new ArrayList<>();
        this.Listafacturadeventas = new ArrayList<>();
        if (condiciondeiva != 1 && condiciondeiva!=2 && condiciondeiva!=3){
            throw  new TipodeEmpresa("El numero ingresado debe ser 1 , 2 o 3");
        }
    }

    public String getAliasEmpresa() {
        return AliasEmpresa;
    }

    public void setAliasEmpresa(String aliasEmpresa) {
        AliasEmpresa = aliasEmpresa;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getNombredefantasia() {
        return Nombredefantasia;
    }

    public void setNombredefantasia(String nombredefantasia) {
        Nombredefantasia = nombredefantasia;
    }

    public String getCuit() {
        return Cuit;
    }

    public void setCuit(String cuit) {
        Cuit = cuit;
    }

    public TipoCondiciondeIva getCondiciondeiva() {
        return Condiciondeiva;
    }

    public void setCondiciondeiva(TipoCondiciondeIva condiciondeiva) {
        Condiciondeiva = condiciondeiva;
    }

    public String getIB() {
        return IB;
    }

    public void setIB(String IB) {
        this.IB = IB;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCorreoElectronico() {
        return CorreoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        CorreoElectronico = correoElectronico;
    }

    public String getPaginaweb() {
        return Paginaweb;
    }

    public void setPaginaweb(String paginaweb) {
        Paginaweb = paginaweb;
    }

    public ArrayList<Factura> getListafacturadecompras() {
        return Listafacturadecompras;
    }

    public void setListafacturadecompras(ArrayList<Factura> listafacturadecompras) {
        Listafacturadecompras = listafacturadecompras;
    }

    public ArrayList<Factura> getListafacturadeventas() {
        return Listafacturadeventas;
    }

    public void setListafacturadeventas(ArrayList<Factura> listafacturadeventas) {
        Listafacturadeventas = listafacturadeventas;
    }

    @Override
    public String toString() {
        return "EmpresaCliente{" +
                "AliasEmpresa='" + AliasEmpresa + '\'' +
                ", RazonSocial='" + RazonSocial + '\'' +
                ", Nombredefantasia='" + Nombredefantasia + '\'' +
                ", Cuit='" + Cuit + '\'' +
                ", Condiciondeiva=" + Condiciondeiva +
                ", IB='" + IB + '\'' +
                ", actividad='" + actividad + '\'' +
                ", Direccion='" + Direccion + '\'' +
                ", Localidad='" + Localidad + '\'' +
                ", Provincia='" + Provincia + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", CorreoElectronico='" + CorreoElectronico + '\'' +
                ", Paginaweb='" + Paginaweb + '\'' +
                '}';
    }
    public TipoCondiciondeIva asignaciondeiva (int condiciondeiva ) {
        if (condiciondeiva == 1) {
            return TipoCondiciondeIva.RESPONSABLE;
        } else if (condiciondeiva == 2) {
            return TipoCondiciondeIva.MONOTRIBUTISTA;
        } else {
            return TipoCondiciondeIva.EXCENTO;
        }

    }

    public EmpresaCliente creaciondeempresa(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Ingrese el Alias de la empresa");
        String alias = scanner.nextLine();
        System.out.println("Ingrese la razon social");
        String razon = scanner.nextLine();
        System.out.println("Ingrese el nombre de fantasia");
        String fantasia = scanner.nextLine();
        System.out.println("Ingrese el CUIT");
        String Cuit = scanner.nextLine();
        System.out.println("Ingrese la condicion de IVA");
        System.out.println("1-Responsable Inscripto");
        System.out.println("2-Monotributista");
        System.out.println("3-Exento");
        int tipoiva = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ingrese el Ingreso Bruto");
        String IB = scanner.nextLine();
        System.out.println("Ingrese la actividad");
        String actividad = scanner.nextLine();
        System.out.println("Ingrese la Direccion fisica de la empresa");
        String direccion = scanner.nextLine();
        System.out.println("Ingrese la Localidad");
        String localidad = scanner.nextLine();
        System.out.println("Ingrese la provincia");
        String provincia = scanner.nextLine();
        System.out.println("Ingrese el telefono o 0 si no posee");
        String telefono = scanner.nextLine();
        if (telefono.equals("0")) {
            telefono = null;
        }
        System.out.println("Ingrese el correoelectronico o 0 si no posee");
        String correo = scanner.nextLine();
        if (correo.equals("0")) {
            correo = null;
        }
        System.out.println("Ingrese la direccion web de la empresa o 0 si no posee");
        String dirrecion = scanner.nextLine();
        if (dirrecion.equals("0")) {
            dirrecion = null;
        }
        EmpresaCliente p1 = new EmpresaCliente(alias, razon, fantasia, Cuit, tipoiva, IB, actividad, direccion, localidad, provincia, telefono, correo, dirrecion);
        return p1;
    }
    public void cargadecompra(Factura factura){
        Listafacturadecompras.add(factura);
    }
    public void cargadeventa(Factura factura){
        Listafacturadeventas.add(factura);
    }

}
