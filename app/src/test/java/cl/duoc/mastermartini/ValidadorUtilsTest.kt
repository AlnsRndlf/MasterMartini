package cl.duoc.mastermartini

import cl.duoc.mastermartini.ui.utils.ValidadorUtils
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class ValidadorUtilsTest : StringSpec({

    "esRutValido debe aceptar RUTs chilenos válidos con dígito numérico, 0 y K" {
        ValidadorUtils.esRutValido("11.111.111-1").shouldBeTrue()
        ValidadorUtils.esRutValido("76.123.456-0").shouldBeTrue()
        ValidadorUtils.esRutValido("11.111.112-K").shouldBeTrue()
        ValidadorUtils.esRutValido("11111112k").shouldBeTrue()
    }

    "esRutValido debe rechazar RUTs inválidos, cortos o con letras en el cuerpo" {
        ValidadorUtils.esRutValido("11.111.111-9").shouldBeFalse()
        ValidadorUtils.esRutValido("12345").shouldBeFalse()
        ValidadorUtils.esRutValido("1234567890-1").shouldBeFalse()
        ValidadorUtils.esRutValido("ab.cde.fgh-1").shouldBeFalse()
    }

    "esCorreoValido debe validar presencia de arroba y dominio" {
        ValidadorUtils.esCorreoValido("chef@mastermartini.cl").shouldBeTrue()
        ValidadorUtils.esCorreoValido("correo_sin_arroba.cl").shouldBeFalse()
        ValidadorUtils.esCorreoValido("correo@sindominio").shouldBeFalse()
        ValidadorUtils.esCorreoValido("   ").shouldBeFalse()
    }

    "esTelefonoValido debe aceptar entre 8 y 11 dígitos" {
        ValidadorUtils.esTelefonoValido("+56912345678").shouldBeTrue()
        ValidadorUtils.esTelefonoValido("912345678").shouldBeTrue()
        ValidadorUtils.esTelefonoValido("1234567").shouldBeFalse()
        ValidadorUtils.esTelefonoValido("123456789012").shouldBeFalse()
    }
})