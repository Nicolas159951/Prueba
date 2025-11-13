package Entidades;

import Manejodearchivos.JSONUTILESPROCLIEN;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestoraEntidades<T extends Entidad> {

    private List<T> entidades = new ArrayList<>();
    private JSONUTILESPROCLIEN jsonUtil = new JSONUTILESPROCLIEN();

    // -------------------------------
    //  CONSTRUCTOR: CARGA DESDE ARCHIVO
    // -------------------------------
    public GestoraEntidades() {
        try {
            List<Object> listaLeida = jsonUtil.leerTodas();
            for (Object obj : listaLeida) {
                if (obj != null) {
                    entidades.add((T) obj);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al leer entidades desde JSON: " + e.getMessage());
        }
    }

    // -------------------------------
    //  AGREGAR ENTIDAD
    // -------------------------------
    public boolean agregarEntidad(T entidad) {
        // Solo busca coincidencias dentro del mismo tipo (Cliente o Proveedor)
        if (buscarPorCuit(entidad.getCuit(), entidad.getClass()) != null) {
            System.out.println("⚠️ Ya existe un " + entidad.getClass().getSimpleName() +
                    " con ese CUIT: " + entidad.getCuit());
            return false;
        }

        entidades.add(entidad);
        guardarCambios();
        return true;
    }


    public T buscarPorCuit(String cuit, Class<?> tipo) {
        for (T e : entidades) {
            if (e.getCuit().equalsIgnoreCase(cuit) && tipo.isInstance(e)) {
                return e;
            }
        }
        return null;
    }



    private void guardarCambios() {
        try {
            jsonUtil.escribir(new ArrayList<>(entidades)); // usa solo la versión con List
        } catch (Exception e) {
            System.out.println("⚠️ Error al guardar cambios en JSON: " + e.getMessage());
        }
    }

    // -------------------------------
    // 🔹 GETTER
    // -------------------------------
    public List<T> getEntidades() {
        return entidades;
    }
}
