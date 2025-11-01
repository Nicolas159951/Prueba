package Entidades;

import java.util.ArrayList;

public class GestoraEntidades {

    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Proveedor> proveedores = new ArrayList<>();


    public void agregarCliente(Cliente c) {
        clientes.add(c);
    } ///Esto no puede estar , tenemos que traer a los clientes/proveedrores de una bdd que simula ser afip , yo no puedo cargar un proveedor nuevo
    /// Si no lo trae de la base de datos estoy pudiendo generar una empresa/entidad fantasma , no lo borro porque voy a usar esta estructura
    /// para hacer la busqueda con la base de datos
    public void agregarProveedor(Proveedor p) {
        proveedores.add(p);
    }


    public void listarClientes() {
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    public void listarProveedores() {
        for (Proveedor p : proveedores) {
            System.out.println(p);
        }
    }


    public Entidad buscarPorCuit(String cuit) {
        for (Cliente c : clientes)
            if (c.getCuit().equals(cuit))
                return c;
        for (Proveedor p : proveedores)
            if (p.getCuit().equals(cuit))
                return p;
        return null;
    }


}
