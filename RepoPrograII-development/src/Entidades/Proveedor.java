package Entidades;

public class Proveedor extends Entidad{
    private String rubro;

    public Proveedor(String nombre, String apellido, String cuit, String direccion, String telefono, String email, String rubro) {
        super(nombre, apellido, cuit, direccion, telefono, email);
        this.rubro = rubro;
    }

    public String getRubro() { return rubro; }
    public void setRubro(String rubro) { this.rubro = rubro; }

    @Override
    public String toString() {
        return super.toString() + ", Rubro: " + rubro;
    }
}
