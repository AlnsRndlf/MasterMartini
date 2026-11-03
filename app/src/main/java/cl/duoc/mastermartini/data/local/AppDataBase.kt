package cl.duoc.mastermartini.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ContenidoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun contenidoDao(): ContenidoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "master_martini_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        // pa qno este vacio
        fun obtenerContenidosIniciales(): List<ContenidoEntity> = listOf(
            ContenidoEntity(
                titulo = "Bombones Templados con Chocolate Ariba",
                tipo = "Receta",
                categoria = "Chocolatería",
                insumoMartini = "Chocolate Ariba Oscuro 54% Master Martini",
                duracion = "45 min",
                pasos = "1. Fundir el chocolate Ariba a 45°C.\n2. Descender temperatura en mármol a 28°C y subir a 31°C.\n3. Moldear casquillos, rellenar con ganache y sellar."
            ),
            ContenidoEntity(
                titulo = "Curso Especialidad: Masas de Hoja y Croissant",
                tipo = "Curso",
                categoria = "Masas de hoja",
                insumoMartini = "Margarina de Hoja Gioia Placa Master Martini",
                duracion = "4 horas",
                pasos = "Módulo 1: Amasado y reposo en frío.\nMódulo 2: Empaste con Margarina Gioia y 3 vueltas simples.\nMódulo 3: Fermentación controlada y horneo a 190°C."
            ),
            ContenidoEntity(
                titulo = "Técnica de Montado de Crema Vegetal en Vitrina",
                tipo = "Video Técnico",
                categoria = "Pastelería de vitrina",
                insumoMartini = "Crema Vegetal Decor Up Master Martini",
                duracion = "15 min",
                pasos = "1. Enfriar Crema Decor Up entre 4°C y 6°C por 12 horas.\n2. Batir a velocidad media hasta punto firme.\n3. Aplicar con boquilla rizada para decoración estable en vitrina."
            ),
            ContenidoEntity(
                titulo = "Helado Artesanal de Pistacho y Fior di Latte",
                tipo = "Receta",
                categoria = "Heladería",
                insumoMartini = "Base Láctea y Pasta Concentrada Master Martini",
                duracion = "60 min",
                pasos = "1. Mezclar base neutra con leche tibia.\n2. Incorporar pasta de pistacho y madurar por 4 horas.\n3. Mantecar y abatir a -18°C."
            )
        )
    }
}