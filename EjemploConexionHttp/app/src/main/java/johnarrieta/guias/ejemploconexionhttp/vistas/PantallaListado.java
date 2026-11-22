package johnarrieta.guias.ejemploconexionhttp.vistas;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import java.util.concurrent.Executors;
import johnarrieta.guias.ejemploconexionhttp.R;
import johnarrieta.guias.ejemploconexionhttp.controladores.ConexionHttpPostServer;
import johnarrieta.guias.ejemploconexionhttp.datos.Usuario;

public class PantallaListado extends AppCompatActivity {
    private ListView lista;
    private ArrayAdapter<String> items;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.pantalla_listado);
        lista = findViewById(R.id.listaUsuarios);
        items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lista.setAdapter(items);
        findViewById(R.id.btnGuardar).setOnClickListener(v ->
            startActivity(new Intent(this, PantallaCrudUsuario.class)));
    }
    @Override protected void onResume() { super.onResume(); cargar(); }   // refresca al volver de editar

    private void cargar() {
        ProgressBarDialog.show(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Usuario> usuarios = null; String error = null;
            try {
                List<String[]> p = new ArrayList<>();
                p.add(new String[]{"accion", "listar"});
                String json = new ConexionHttpPostServer().conexionConElServidor(p, ConexionHttpPostServer.direccionDelServidor);
                if (json.startsWith("[")) usuarios = new Gson().fromJson(json, new TypeToken<List<Usuario>>() {}.getType());
                else error = "No hay Usuarios";
            } catch (Exception e) { error = e.getMessage(); }
            final List<Usuario> us = usuarios; final String err = error;
            runOnUiThread(() -> {
                ProgressBarDialog.hide();
                items.clear();
                if (us != null) for (Usuario u : us) items.add(u.getEmail() + " - " + u.getNombre());
                else Toast.makeText(this, err, Toast.LENGTH_LONG).show();
            });
        });
    }
}
