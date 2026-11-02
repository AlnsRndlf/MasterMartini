package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.viewmodel.MainViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel


// pantalla registro con validaciones visuales
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegistroScreen(
    mainViewModel: MainViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val estado by usuarioViewModel.estado.collectAsState()

    val rubrosNegocio = listOf(
        "Pastelería",
        "Panadería",
        "Chocolatería",
        "Heladería",
        "Casino / Restaurante"
    )

    val tamanosNegocio = listOf(
        "Microemprendimiento",
        "Pyme (2 a 10 personas)",
        "Mediana / Grande (+10)"
    )

    val especialidadesInteres = listOf(
        "Chocolatería",
        "Pastelería de vitrina",
        "Heladería",
        "Bollería",
        "Masas de hoja",
        "Galletas",
        "Empanadas",
        "Productos de horneo"
    )

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = "Registro Cliente / Prospecto",
                mostrarBotonAtras = true,
                onBackClick = { mainViewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.Registro.route,
                onNavigate = { destino -> mainViewModel.navigateTo(destino) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 14.dp)
        ) {
            Text(
                text = "Ficha Comercial y Preferencias",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Complete sus datos ficticios/académicos para segmentar recetas, videos técnicos e invitaciones a eventos Master Martini.",
                style = MaterialTheme.typography.bodyLarge
            )

            // seleccion tipo usuario | cliente prospecto
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "1. Tipo de Relación con Master Martini",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf("Cliente", "Prospecto").forEach { tipo ->
                            val seleccionado = estado.tipoPerfil == tipo
                            FilterChip(
                                selected = seleccionado,
                                onClick = { usuarioViewModel.onTipoPerfilChange(tipo) },
                                label = {
                                    Text(
                                        text = tipo,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                leadingIcon = if (seleccionado) {
                                    { Icon(Icons.Default.Check, contentDescription = null) }
                                } else null
                            )
                        }
                    }
                }
            }


            /*   CAMPOS   */

            // nombre completo
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = usuarioViewModel::onNombreChange,
                label = { Text(text = "Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
                trailingIcon = {
                    if (estado.errores.nombre != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en nombre",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // rut
            OutlinedTextField(
                value = estado.rutEmpresa,
                onValueChange = usuarioViewModel::onRutEmpresaChange,
                label = { Text(text = "RUT Empresa o Emprendedor (Ej: 76.123.456-0)") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = "RUT Empresa") },
                trailingIcon = {
                    if (estado.errores.rutEmpresa != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en RUT",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = estado.errores.rutEmpresa != null,
                supportingText = {
                    estado.errores.rutEmpresa?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    } ?: Text("Ingrese RUT con guion y dígito verificador válido")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = usuarioViewModel::onCorreoChange,
                label = { Text(text = "Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Correo") },
                trailingIcon = {
                    if (estado.errores.correo != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en correo",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // telefono
            OutlinedTextField(
                value = estado.telefono,
                onValueChange = usuarioViewModel::onTelefonoChange,
                label = { Text(text = "Teléfono de contacto (Ej: +56912345678)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Teléfono") },
                trailingIcon = {
                    if (estado.errores.telefono != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en teléfono",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = estado.errores.telefono != null,
                supportingText = {
                    estado.errores.telefono?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // direccion
            OutlinedTextField(
                value = estado.direccion,
                onValueChange = usuarioViewModel::onDireccionChange,
                label = { Text(text = "Dirección comercial / Comuna") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Dirección") },
                trailingIcon = {
                    if (estado.errores.direccion != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en dirección",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = estado.errores.direccion != null,
                supportingText = {
                    estado.errores.direccion?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // contraseña
            OutlinedTextField(
                value = estado.clave,
                onValueChange = usuarioViewModel::onClaveChange,
                label = { Text(text = "Contraseña (mínimo 6 caracteres)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
                trailingIcon = {
                    if (estado.errores.clave != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en contraseña",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = {
                    estado.errores.clave?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // 8. Tipo y Tamaño del Negocio Gastronómico
            Text(
                text = "2. Tipo de Negocio Gastronómico",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rubrosNegocio.forEach { rubro ->
                    val seleccionado = estado.tipoNegocio == rubro
                    FilterChip(
                        selected = seleccionado,
                        onClick = { usuarioViewModel.onTipoNegocioChange(rubro) },
                        label = { Text(rubro, style = MaterialTheme.typography.bodyMedium) },
                        leadingIcon = {
                            Icon(
                                imageVector = if (seleccionado) Icons.Default.Check else Icons.Default.Business,
                                contentDescription = null
                            )
                        }
                    )
                }
            }

            Text(
                text = "3. Tamaño Aproximado del Negocio",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tamanosNegocio.forEach { tamano ->
                    val seleccionado = estado.tamanoNegocio == tamano
                    FilterChip(
                        selected = seleccionado,
                        onClick = { usuarioViewModel.onTamanoNegocioChange(tamano) },
                        label = { Text(tamano, style = MaterialTheme.typography.bodyMedium) }
                    )
                }
            }

            // 9. Encuesta de Intereses y Preferencias de Contenido
            Text(
                text = "4. Encuesta de Preferencias de Contenido",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Toque las especialidades de su interés (puede elegir varias):",
                style = MaterialTheme.typography.bodyMedium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                especialidadesInteres.forEach { especialidad ->
                    val seleccionada = estado.preferenciasContenido.contains(especialidad)
                    FilterChip(
                        selected = seleccionada,
                        onClick = { usuarioViewModel.onTogglePreferencia(especialidad) },
                        label = { Text(especialidad, style = MaterialTheme.typography.bodyMedium) },
                        leadingIcon = if (seleccionada) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }
            estado.errores.preferencias?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }

            // 10. Checkbox: Aceptación de términos y contacto para eventos
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = estado.aceptaTerminos,
                        onCheckedChange = usuarioViewModel::onAceptarTerminosChange
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Acepto los términos y autorizo recibir invitaciones a cursos y eventos Master Martini (datos académicos ficticios).",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                estado.errores.terminos?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            // 11. Botón de Envío Grande (60.dp)
            Button(
                onClick = {
                    if (usuarioViewModel.validarFormulario()) {
                        mainViewModel.navigateTo(Screen.ResumenRegistro)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Registrar y Ver Resumen",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}