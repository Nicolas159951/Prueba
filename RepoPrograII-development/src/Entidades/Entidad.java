package Entidades;

public abstract class Entidad {

    private String nombre;
    private String apellido;
    private String cuit; // o DNI según corresponda
    private String direccion;
    private String telefono;
    private String email;

    public Entidad(String nombre, String apellido, String cuit, String direccion, String telefono, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuit = cuit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public Entidad() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " " + apellido +
                ", CUIT/DNI: " + cuit +
                ", Dirección: " + direccion +
                ", Tel: " + telefono +
                ", Email: " + email;
    }
}
