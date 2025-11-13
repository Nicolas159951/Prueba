package Manejodearchivos;

import Entidades.Cliente;
import Entidades.Proveedor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONUTILESPROCLIEN implements Archivos {

    private final String RUTA_ARCHIVO = "ProveedoresClientes.json";



    @Override
    public JSONObject convertirAJson(Object entidad) throws Exception {
        JSONObject obj = new JSONObject();

        if (entidad instanceof Proveedor) {
            Proveedor p = (Proveedor) entidad;
            obj.put("tipo", "Proveedor");
            obj.put("nombre", p.getNombre());
            obj.put("apellido", p.getApellido());
            obj.put("cuit", p.getCuit());
            obj.put("direccion", p.getDireccion());
            obj.put("telefono", p.getTelefono());
            obj.put("email", p.getEmail());
            obj.put("rubro", p.getRubro());
        }
        else if (entidad instanceof Cliente) {
            Cliente c = (Cliente) entidad;
            obj.put("tipo", "Cliente");
            obj.put("nombre", c.getNombre());
            obj.put("apellido", c.getApellido());
            obj.put("cuit", c.getCuit());
            obj.put("direccion", c.getDireccion());
            obj.put("telefono", c.getTelefono());
            obj.put("email", c.getEmail());
            obj.put("tipoCliente", c.getTipoCliente());
        }
        else {
            throw new Exception("Tipo de entidad desconocido: " + entidad.getClass().getSimpleName());
        }

        return obj;
    }
    @Override
    public Object leerPorDato(String dato) throws Exception {
       return null;
    }

    // ---------------- MÉTODOS CON LISTAS ----------------
    @Override
    public void escribir(List entidades) throws Exception {
        JSONArray array = new JSONArray();
        for (Object entidad : entidades) {
            array.put(convertirAJson(entidad));
        }

        try (FileWriter file = new FileWriter(RUTA_ARCHIVO)) {
            file.write(array.toString(4)); // 4 = identado bonito
        }
    }



    @Override
    public List leerTodas() throws Exception {
        List<Object> lista = new ArrayList<>();

        if (!Files.exists(Paths.get(RUTA_ARCHIVO))) {
            return lista; // archivo vacío o inexistente
        }

        String contenido = new String(Files.readAllBytes(Paths.get(RUTA_ARCHIVO)));
        if (contenido.isEmpty()) return lista;

        JSONArray array = new JSONArray(contenido);

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String tipo = obj.optString("tipo");

            if ("Proveedor".equalsIgnoreCase(tipo)) {
                Proveedor p = new Proveedor(
                        obj.getString("nombre"),
                        obj.getString("apellido"),
                        obj.getString("cuit"),
                        obj.getString("direccion"),
                        obj.getString("telefono"),
                        obj.getString("email"),
                        obj.getString("rubro")
                );
                lista.add(p);
            } else if ("Cliente".equalsIgnoreCase(tipo)) {
                Cliente c = new Cliente(
                        obj.getString("nombre"),
                        obj.getString("apellido"),
                        obj.getString("cuit"),
                        obj.getString("direccion"),
                        obj.getString("telefono"),
                        obj.getString("email"),
                        obj.getString("tipoCliente")
                );
                lista.add(c);
            }
        }

        return lista;
    }
}