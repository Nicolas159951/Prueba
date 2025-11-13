package Manejodearchivos;
import Entidades.EmpresaCliente;
import Entidades.Enums.TipoCondiciondeIva;
import Facturas.Enums.Tipodecomprobante;
import Facturas.Factura;
import Facturas.FacturaA;
import Facturas.FacturaB;
import Facturas.FacturaC;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class JSONUtilesEmpresa implements Archivos {
    /// La carga del archivo funciona asi : primero creo la "Ruta" del archivo es el nombre de este nada mas , despues de la validacion para ver si el archivo existe
    /// lo voy a recorrer buscando el cuil que el usuario carga previemente para evitar tener dos empresas con el mismo cuit , si encuentra coincidencia la sobreescribe
    /// y si no agrega la nueva empresa al final llamando a convertirempresaJson.
   private static final String RUTA = "Empresas.json";

    public  void escribir(EmpresaCliente empresa) throws JSONException {
        JSONArray empresasArray = new JSONArray();

        try {
            //  Leer archivo existente
            String contenido = new String(Files.readAllBytes(Paths.get(RUTA)));
            JSONObject raiz = new JSONObject(contenido);
            empresasArray = raiz.getJSONArray("empresas");
        } catch (IOException e) {
            System.out.println("No se encontró el archivo, se creará uno nuevo.");
        }

        //  Buscar si la empresa ya existe (por CUIT)
        boolean empresaExistente = false;
        for (int i = 0; i < empresasArray.length(); i++) {
            JSONObject empresaJSON = empresasArray.getJSONObject(i);
            if (empresaJSON.getString("cuit").equalsIgnoreCase(empresa.getCuit())) {
                // Si existe, reemplazamos el objeto en ese índice
                empresasArray.put(i, convertirAJson(empresa));
                empresaExistente = true;
                break;
            }
        }

        //  Si no existe, agregarla al final
        if (!empresaExistente) {
            empresasArray.put(convertirAJson(empresa));
        }

        // Escribir nuevamente el archivo
        JSONObject raizNueva = new JSONObject();
        raizNueva.put("empresas", empresasArray);

        try {
            Files.write(Paths.get(RUTA), raizNueva.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /// Este metodo recibe los datos de la empresa y le asigna la clave que va a tener en el archivo y su valor , hay dos for
    /// porque como tengo arreglos con objetos necesito pasarle objeto por objeto al arreglo correspondiente y al final devuelve un objeto
    /// el cual es cargado en el archivo
    public   JSONObject convertirAJson(EmpresaCliente empresa) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("aliasEmpresa", empresa.getAliasEmpresa());
        obj.put("razonSocial", empresa.getRazonSocial());
        obj.put("nombredefantasia", empresa.getNombredefantasia());
        obj.put("cuit", empresa.getCuit());
        obj.put("condiciondeiva", empresa.getCondiciondeiva().toString());
        obj.put("IB", empresa.getIB());
        obj.put("actividad", empresa.getActividad());
        obj.put("direccion", empresa.getDireccion());
        obj.put("localidad", empresa.getLocalidad());
        obj.put("provincia", empresa.getProvincia());
        obj.put("telefono", empresa.getTelefono());
        obj.put("correoElectronico", empresa.getCorreoElectronico());
        obj.put("paginaweb", empresa.getPaginaweb());
        JSONArray facturasCompra = new JSONArray();
        for (Factura f : empresa.getListafacturadecompras()) {
            facturasCompra.put(f.toJson());
        }
        obj.put("listafacturadecompras", facturasCompra);

        JSONArray facturasVenta = new JSONArray();
        for (Factura f : empresa.getListafacturadeventas()) {
            facturasVenta.put(f.toJson());
        }
        obj.put("listafacturadeventas", facturasVenta);

        return obj;
    }



    @Override
    public JSONObject convertirAJson(Object entidad) throws Exception {
        return null;
    }

    /// Leer basicamente hace eso , apartir del cuil o el nombre de fantasia se busca a la empresa elegida y crea dos arreglos para la lista de compra
    /// y venta , recorre cada dato de la lista y los tranforma en objetos con el cual carga los anteriores arreglos y crea una instancia de empresacliente
    /// y la devuelve al main para cargar las facturas
    public EmpresaCliente leerPorDato(String dato) throws JSONException  {
        EmpresaCliente empresaEncontrada = null;

        try {
            String contenido = new String(Files.readAllBytes(Paths.get(RUTA)));
            JSONObject raiz = new JSONObject(contenido);
            JSONArray empresasArray = raiz.getJSONArray("empresas");

            for (int i = 0; i < empresasArray.length(); i++) {
                JSONObject obj = empresasArray.getJSONObject(i);
                String nombreFantasia = obj.getString("nombredefantasia");
                String cuit = obj.getString("cuit");

                if (nombreFantasia.equalsIgnoreCase(dato) || cuit.equalsIgnoreCase(dato)) {


                    TipoCondiciondeIva condicionIVA = TipoCondiciondeIva.valueOf(obj.getString("condiciondeiva").toUpperCase());


                    ArrayList<Factura> listaCompra = new ArrayList<>();
                    ArrayList<Factura> listaVenta = new ArrayList<>();

                    /// Carga compras
                    JSONArray facturasArrayCompra = obj.getJSONArray("listafacturadecompras");
                    for (int j = 0; j < facturasArrayCompra.length(); j++) {
                        JSONObject fObj = facturasArrayCompra.getJSONObject(j); // ← corregido: era i
                        String tipoStr = fObj.getString("tipo");
                        Tipodecomprobante tipo = Tipodecomprobante.valueOf(tipoStr.toUpperCase());
                        Factura f = switch (tipo) {
                            case A -> new FacturaA(
                                    fObj.optDouble("nogravado",0),
                                    fObj.getString("Cuit"),
                                    tipo,
                                    fObj.getInt("sucursal"),
                                    fObj.getInt("Numero de factura"),
                                    LocalDate.parse(fObj.getString("Fecha")),
                                    fObj.optDouble("neto21",0),
                                    fObj.optDouble("neto105",0),
                                    fObj.optDouble("percepcionIVA",0),
                                    fObj.optDouble("percepcionIB",0),
                                    fObj.optDouble("Otros Impuestos",0)
                            );
                            case B -> new FacturaB(
                                    fObj.optDouble("nogravado",0),
                                    fObj.getString("Cuit"),
                                    tipo,
                                    fObj.getInt("sucursal"),
                                    fObj.getInt("Numero de factura"),
                                    LocalDate.parse(fObj.getString("Fecha")),
                                    fObj.optDouble("neto21",0),
                                    fObj.optDouble("neto105",0),
                                    fObj.optDouble("percepcionIVA",0),
                                    fObj.optDouble("percepcionIB",0),
                                    fObj.optDouble("Otros Impuestos",0)
                            );
                            case C -> new FacturaC(
                                    fObj.optDouble("nogravado",0),
                                    fObj.getString("Cuit"),
                                    tipo,
                                    fObj.getInt("sucursal"),
                                    fObj.getInt("numerodefactura"),
                                    LocalDate.parse(fObj.getString("Fecha"))
                            );
                        };
                        listaCompra.add(f);
                    }

                    /// Carga ventas
                    JSONArray facturasArrayVenta = obj.getJSONArray("listafacturadeventas");
                    for (int j = 0; j < facturasArrayVenta.length(); j++) {
                        JSONObject fObj = facturasArrayVenta.getJSONObject(j); // ← corregido: era i
                        String tipoStr = fObj.getString("tipo");
                        Tipodecomprobante tipo = Tipodecomprobante.valueOf(tipoStr.toUpperCase());
                            Factura f = switch (tipo) {
                                case A -> new FacturaA(
                                        fObj.optDouble("nogravado",0),
                                        fObj.getString("Cuit"),
                                        tipo,
                                        fObj.getInt("sucursal"),
                                        fObj.getInt("Numero de factura"),
                                        LocalDate.parse(fObj.getString("Fecha")),
                                        fObj.optDouble("neto21",0),
                                        fObj.optDouble("neto105",0),
                                        fObj.optDouble("percepcionIVA",0),
                                        fObj.optDouble("percepcionIB",0),
                                        fObj.optDouble("Otros Impuestos",0)
                                );
                                case B -> new FacturaB(
                                        fObj.optDouble("nogravado",0),
                                        fObj.getString("Cuit"),
                                        tipo,
                                        fObj.getInt("sucursal"),
                                        fObj.getInt("Numero de factura"),
                                        LocalDate.parse(fObj.getString("Fecha")),
                                        fObj.optDouble("neto21",0),
                                        fObj.optDouble("neto105",0),
                                        fObj.optDouble("percepcionIVA",0),
                                        fObj.optDouble("percepcionIB",0),
                                        fObj.optDouble("Otros Impuestos",0)
                                );
                                case C -> new FacturaC(
                                        fObj.optDouble("nogravado" ,0),
                                        fObj.getString("Cuit"),
                                        tipo,
                                        fObj.getInt("sucursal"),
                                        fObj.getInt("numerodefactura"),
                                        LocalDate.parse(fObj.getString("Fecha"))
                                );
                            };
                            listaVenta.add(f);

                    }

                    empresaEncontrada = new EmpresaCliente(
                            obj.getString("aliasEmpresa"),
                            obj.getString("razonSocial"),
                            obj.getString("nombredefantasia"),
                            obj.getString("cuit"),
                            condicionIVA,
                            obj.getString("IB"),
                            obj.getString("actividad"),
                            obj.getString("direccion"),
                            obj.getString("localidad"),
                            obj.getString("provincia"),
                            obj.optString("telefono", ""),
                            obj.optString("correoElectronico", ""),
                            obj.optString("paginaweb", ""),
                            listaCompra,
                            listaVenta
                    );

                    break; // empresa encontrada, salir del loop
                }
            }

        } catch (IOException e) {
            System.out.println("No se encontró el archivo JSON.");
        }

        return empresaEncontrada;
    }

    @Override
    public void escribir(List entidades) throws Exception {

    }

    


    @Override
    public List<EmpresaCliente> leerTodas() throws JSONException, IOException {
        List<EmpresaCliente> lista = new ArrayList<>();
        Path path = Paths.get("Empresas.json");

        if (!Files.exists(path)) {
            return lista; // Si no existe el archivo, devuelve lista vacía
        }

        String contenido = new String(Files.readAllBytes(path));
        JSONObject raiz = new JSONObject(contenido);

        if (!raiz.has("empresas")) {
            return lista;
        }

        JSONArray empresasArray = raiz.getJSONArray("empresas");

        for (int i = 0; i < empresasArray.length(); i++) {
            JSONObject obj = empresasArray.getJSONObject(i);

            String cuit = obj.optString("cuit", "");
            String nombreFantasia = obj.optString("nombredefantasia", "");

            // 🔹 Creamos una empresa mínima (solo con estos campos)
            EmpresaCliente empresa = new EmpresaCliente();
            empresa.setCuit(cuit);
            empresa.setNombredefantasia(nombreFantasia);

            lista.add(empresa);
        }

        return lista;
    }
}