package cl.duoc.mastermartini.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

interface PreferenciasStorage {
    suspend fun guardarEstado(valor: Boolean)
    fun obtenerEstado(): Flow<Boolean?>
    suspend fun guardarAltoContraste(valor: Boolean)
    fun obtenerAltoContraste(): Flow<Boolean>
}

class EstadoDataStore(private val context: Context) : PreferenciasStorage {

    private val ESTADO_ACTIVADO = booleanPreferencesKey(name = "modo_activado")
    private val ALTO_CONTRASTE = booleanPreferencesKey(name = "alto_contraste_activo")

    override suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    override fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO]
        }
    }

    override suspend fun guardarAltoContraste(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ALTO_CONTRASTE] = valor
        }
    }

    override fun obtenerAltoContraste(): Flow<Boolean> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ALTO_CONTRASTE] ?: false
        }
    }
}