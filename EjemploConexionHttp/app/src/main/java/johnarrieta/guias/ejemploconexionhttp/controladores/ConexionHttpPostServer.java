package johnarrieta.guias.ejemploconexionhttp.controladores;
import java.io.*;
import java.net.*;
import java.util.List;
/** Gestiona las peticiones HTTP POST hacia el servicio PHP. Cada parametro es un String[]{nombre, valor}. */
public class ConexionHttpPostServer {
    // 10.0.2.2 = el PC anfitrion visto desde el emulador. En un celular real use la IP del PC (ej. 192.168.1.31)
    public static String direccionDelServidor = "http://192.168.1.5/crudphpjson/crud/operacion.php";

    public String conexionConElServidor(List<String[]> parametros, String ruta) throws Exception {
        HttpURLConnection con = null;
        try {
            StringBuilder cuerpo = new StringBuilder();
            for (String[] p : parametros) {
                if (cuerpo.length() > 0) cuerpo.append('&');
                cuerpo.append(URLEncoder.encode(p[0], "UTF-8")).append('=').append(URLEncoder.encode(p[1], "UTF-8"));
            }
            con = (HttpURLConnection) new URL(ruta).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(8000);
            con.setReadTimeout(8000);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            try (OutputStream os = con.getOutputStream()) { os.write(cuerpo.toString().getBytes("UTF-8")); }
            int codigo = con.getResponseCode();
            if (codigo == HttpURLConnection.HTTP_NOT_IMPLEMENTED) throw new Exception("ERROR 1: Parametros mal codificados");
            if (codigo != HttpURLConnection.HTTP_OK) throw new Exception("ERROR 2: URL invalida");
            return procesarRespuesta(con.getInputStream());
        } catch (Exception e) {
            throw new Exception("ERROR 3: Conexion fallida:\n" + e.getMessage());
        } finally {
            if (con != null) con.disconnect();
        }
    }
    private String procesarRespuesta(InputStream in) throws Exception {
        StringBuilder json = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            String linea;
            while ((linea = br.readLine()) != null) if (!linea.trim().isEmpty()) json.append(linea);
        } catch (IOException e) { throw new Exception("ERROR 5: Sin respuesta\n" + e.getMessage()); }
        return json.toString();
    }
}
