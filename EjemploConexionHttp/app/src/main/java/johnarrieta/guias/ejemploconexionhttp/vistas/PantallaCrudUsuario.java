package johnarrieta.guias.ejemploconexionhttp.vistas;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.util.*;
import java.util.concurrent.Executors;
import johnarrieta.guias.ejemploconexionhttp.R;
import johnarrieta.guias.ejemploconexionhttp.controladores.ConexionHttpPostServer;
import johnarrieta.guias.ejemploconexionhttp.datos.*;

public class PantallaCrudUsuario extends AppCompatActivity {
    private EditText campoCodigo, campoPassword, campoNombre;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.pantalla_crud_usuario);
        campoCodigo = findViewById(R.id.campoCodigo2);
        campoPassword = findViewById(R.id.campoPassword2);
        campoNombre = findViewById(R.id.campoNombre);
        findViewById(R.id.botonCancelar2).setOnClickListener(v -> finish());
        Usuario u = PantallaInicio.fulanito;
        if (u != null) {                      // editar: se precargan los datos y el email no se cambia
            campoCodigo.setText(u.getEmail()); campoCodigo.setEnabled(false);
            campoPassword.setText(u.getPassword()); campoNombre.setText(u.getNombre());
        }
        findViewById(R.id.botonModificar).setOnClickListener(v -> guardar());
    }
    private void guardar() {
        final String email = campoCodigo.getText().toString().trim(), pass = campoPassword.getText().toString().trim(),
                     nombre = campoNombre.getText().toString().trim();
        if (email.isEmpty() || pass.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show(); return; }
        final String accion = PantallaInicio.fulanito == null ? "Agregar" : "editar";
        ProgressBarDialog.show(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            String resultado;
            try {
                List<String[]> p = new ArrayList<>();
                p.add(new String[]{"accion", accion}); p.add(new String[]{"email", email});
                p.add(new String[]{"psw", pass}); p.add(new String[]{"nombre", nombre});
                String json = new ConexionHttpPostServer().conexionConElServidor(p, ConexionHttpPostServer.direccionDelServidor);
                resultado = new Gson().fromJson(json, Mensaje.class).getMensaje();
            } catch (Exception e) { resultado = e.getMessage(); }
            final String res = resultado;
            runOnUiThread(() -> {
                ProgressBarDialog.hide();
                if ("OK".equals(res)) {
                    if (PantallaInicio.fulanito != null) PantallaInicio.fulanito = new Usuario(email, pass, nombre);
                    Toast.makeText(this, "Usuario Guardado con Exito", Toast.LENGTH_LONG).show();
                    finish();
                } else Toast.makeText(this, "ERROR: " + res, Toast.LENGTH_LONG).show();
            });
        });
    }
}
