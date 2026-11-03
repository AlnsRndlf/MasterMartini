package cl.duoc.mastermartini.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class EstadoDataStore(private val context: Context) {

    // Clave usada para guardar el estado del modo accesible / alertas
    private val ESTADO_ACTIVADO = booleanPreferencesKey(name = "modo_activado")

    // Clave adicional para persistir el filtro de alto contraste visual
    private val ALTO_CONTRASTE = booleanPreferencesKey(name = "alto_contraste_activo")

    // Función suspendida para guardar el estado en DataStore
    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    // Función para obtener el estado como Flow reactivo
    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO]
        }
    }

    suspend fun guardarAltoContraste(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ALTO_CONTRASTE] = valor
        }
    }

    fun obtenerAltoContraste(): Flow<Boolean> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ALTO_CONTRASTE] ?: false
        }
    }
}