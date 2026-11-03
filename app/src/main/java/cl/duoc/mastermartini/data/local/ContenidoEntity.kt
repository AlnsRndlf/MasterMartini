package cl.duoc.mastermartini.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.duoc.mastermartini.model.Contenido

@Entity(tableName = "contenidos_tecnicos")
data class ContenidoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val tipo: String,
    val categoria: String,
    val insumoMartini: String,
    val duracion: String,
    val pasos: String,
    val imagenUri: String? = null
)

// mapeo entre entity y modelo de dominio
fun ContenidoEntity.toDomain(): Contenido = Contenido(
    id = id,
    titulo = titulo,
    tipo = tipo,
    categoria = categoria,
    insumoMartini = insumoMartini,
    duracion = duracion,
    pasos = pasos,
    imagenUri = imagenUri
)

fun Contenido.toEntity(): ContenidoEntity = ContenidoEntity(
    id = id,
    titulo = titulo,
    tipo = tipo,
    categoria = categoria,
    insumoMartini = insumoMartini,
    duracion = duracion,
    pasos = pasos,
    imagenUri = imagenUri
)