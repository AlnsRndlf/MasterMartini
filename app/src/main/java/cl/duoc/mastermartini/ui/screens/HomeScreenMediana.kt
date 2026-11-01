package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.R
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme

//para pantallas medinas o "cuadradas" ; tablets , plegables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana(
    onIrACatalogo: () -> Unit = {},
    onIrARegistro: () -> Unit = {},
    onIrASubirContenido: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Master Martini Chile - Vista Tablet Mediana") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Cabecera en fila (Logo a la izquierda, texto de bienvenida a la derecha)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Master Martini",
                    modifier = Modifier
                        .size(140.dp),
                    contentScale = ContentScale.Fit
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Plataforma de Contenidos Técnicos",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Capacitación continua para panaderías, pastelerías, chocolaterías, casinos y restaurantes.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Dos tarjetas agrupadas lado a lado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Cursos",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Cursos y Videos", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "Chocolatería, masas de hoja, bollería y pastelería de vitrina.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "Insumos",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Recetario Técnico", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "Paso a paso con insumos profesionales Master Martini.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Fila de botones grandes de acción directa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onIrACatalogo,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Catálogo")
                }

                Button(
                    onClick = onIrASubirContenido,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Subir Receta")
                }
            }

            OutlinedButton(
                onClick = onIrARegistro,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Registrar o Actualizar Perfil de Cliente / Prospecto")
            }
        }
    }
}

@Preview(name = "Medium", widthDp = 600, heightDp = 800, showBackground = true)
@Composable
fun PreviewMedium() {
    MasterMartiniTheme {
        HomeScreenMediana()
    }
}