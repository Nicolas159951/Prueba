package Manejodearchivos;

import org.json.JSONObject;
import java.util.List;

public interface Archivos<T> {

    // --- Métodos originales (para un solo objeto) ---
    JSONObject convertirAJson(T entidad) throws Exception;
    T leerPorDato(String dato) throws Exception;

    // --- Nuevas sobrecargas (para listas completas) ---
    void escribir(List<T> entidades) throws Exception;
    List<T> leerTodas() throws Exception;
}