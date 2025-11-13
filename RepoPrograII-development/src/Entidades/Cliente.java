package Entidades;

public class Cliente extends Entidad{

    private String tipoCliente;
    public Cliente(String nombre, String apellido, String cuit, String direccion, String telefono, String email, String tipoCliente) {
        super(nombre, apellido, cuit, direccion, telefono, email);
        this.tipoCliente = tipoCliente;
    }

    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }

    @Override
    public String toString() {
        return super.toString() + ", Tipo Cliente: " + tipoCliente;
    }
}



