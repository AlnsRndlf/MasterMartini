package cl.duoc.mastermartini.ui.utils

object ValidadorUtils {

    // Valida formato del rut - dv chileno
    fun esRutValido(rut: String): Boolean {
        val rutLimpio = rut.replace(".", "").replace("-", "").trim().uppercase()
        if (rutLimpio.length < 8 || rutLimpio.length > 9) return false

        val cuerpo = rutLimpio.substring(0, rutLimpio.length - 1)
        val dvIngresado = rutLimpio.last()

        if (!cuerpo.all { it.isDigit() }) return false

        var suma = 0
        var multiplicador = 2

        for (i in cuerpo.length - 1 downTo 0) {
            suma += cuerpo[i].digitToInt() * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        val resto = 11 - (suma % 11)
        val dvCalculado = when (resto) {
            11 -> '0'
            10 -> 'K'
            else -> resto.digitToChar()
        }

        return dvIngresado == dvCalculado
    }

    fun esCorreoValido(correo: String): Boolean {
        val limpio = correo.trim()
        return limpio.isNotBlank() && limpio.contains("@") && limpio.contains(".")
    }

    fun esTelefonoValido(telefono: String): Boolean {
        val digitos = telefono.filter { it.isDigit() }
        return digitos.length in 8..11
    }
}