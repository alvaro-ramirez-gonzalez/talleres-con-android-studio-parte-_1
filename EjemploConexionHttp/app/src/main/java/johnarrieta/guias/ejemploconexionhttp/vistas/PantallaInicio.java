package johnarrieta.guias.ejemploconexionhttp.vistas;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.util.*;
import java.util.concurrent.Executors;
import johnarrieta.guias.ejemploconexionhttp.R;
import johnarrieta.guias.ejemploconexionhttp.controladores.ConexionHttpPostServer;
import johnarrieta.guias.ejemploconexionhttp.datos.*;

public class PantallaInicio extends AppCompatActivity {
    private EditText campoEmail, campoPassword;
    public static Usuario fulanito;   // usuario con sesion iniciada

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.pantalla_inicio);
        campoEmail = findViewById(R.id.campoEmail);
        campoPassword = findViewById(R.id.campoClave);
        fulanito = null;
        findViewById(R.id.btnCancelar).setOnClickListener(v ->
            startActivity(new Intent(this, PantallaCrudUsuario.class)));   // Registrate
        findViewById(R.id.btnIniciarSesion).setOnClickListener(v -> {
            String email = campoEmail.getText().toString().trim(), pass = campoPassword.getText().toString().trim();
            if (email.isEmpty()) { Toast.makeText(this, "Debe ingresar el Email", Toast.LENGTH_LONG).show(); return; }
            if (pass.isEmpty()) { Toast.makeText(this, "Debe ingresar el Password", Toast.LENGTH_LONG).show(); return; }
            login(email, pass);
        });
    }
    private void login(String email, String pass) {
        ProgressBarDialog.show(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            String resultado; Usuario u = null;
            try {
                List<String[]> p = new ArrayList<>();
                p.add(new String[]{"accion", "login"}); p.add(new String[]{"email", email}); p.add(new String[]{"psw", pass});
                String json = new ConexionHttpPostServer().conexionConElServidor(p, ConexionHttpPostServer.direccionDelServidor);
                Gson g = new Gson();
                Usuario tmp = g.fromJson(json, Usuario.class);
                if (tmp != null && tmp.getEmail() != null) { u = tmp; resultado = "OK"; }
                else resultado = g.fromJson(json, Mensaje.class).getMensaje();
            } catch (Exception e) { resultado = e.getMessage(); }
            final String res = resultado; final Usuario usr = u;
            runOnUiThread(() -> {
                ProgressBarDialog.hide();
                if ("OK".equals(res)) {
                    fulanito = usr;
                    Intent i = new Intent(this, PantallaListado.class);
                    i.putExtra("sesion", usr);
                    startActivity(i);
                } else Toast.makeText(this, res, Toast.LENGTH_LONG).show();
            });
        });
    }
}
