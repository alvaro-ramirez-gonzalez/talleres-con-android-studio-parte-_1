package johnarrieta.guias.ejemploconexionhttp.vistas;
import android.app.ProgressDialog;
import android.content.Context;
/** Muestra/oculta el mensaje "Conectando..." */
class ProgressBarDialog {
    private static ProgressDialog d;
    static void show(Context c) { d = new ProgressDialog(c); d.setMessage("Conectando..."); d.setCancelable(false); d.show(); }
    static void hide() { if (d != null && d.isShowing()) d.dismiss(); }
}
