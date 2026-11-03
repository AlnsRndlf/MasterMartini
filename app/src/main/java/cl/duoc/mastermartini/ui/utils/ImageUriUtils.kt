package cl.duoc.mastermartini.ui.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ImageUriUtils {

    // Genera un archivo temporal en el directorio cache/images y devuelve su Uri seguro
    fun crearUriTemporalCamara(context: Context): Uri {
        val directorioImagenes = File(context.cacheDir, "images")
        if (!directorioImagenes.exists()) {
            directorioImagenes.mkdirs()
        }
        val archivoTemporal = File.createTempFile(
            "captura_martini_${System.currentTimeMillis()}_",
            ".jpg",
            directorioImagenes
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivoTemporal
        )
    }
}