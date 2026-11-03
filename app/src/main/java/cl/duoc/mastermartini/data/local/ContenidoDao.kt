package cl.duoc.mastermartini.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContenidoDao {

    @Query("SELECT * FROM contenidos_tecnicos ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<ContenidoEntity>>

    @Query("SELECT * FROM contenidos_tecnicos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ContenidoEntity?

    @Query("SELECT COUNT(*) FROM contenidos_tecnicos")
    suspend fun contarRegistros(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(contenido: ContenidoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVarios(contenidos: List<ContenidoEntity>)

    @Update
    suspend fun actualizar(contenido: ContenidoEntity)

    @Delete
    suspend fun eliminar(contenido: ContenidoEntity)
}