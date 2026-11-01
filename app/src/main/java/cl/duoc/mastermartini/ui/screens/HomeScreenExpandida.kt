package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Restaurant
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.R
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme

//pantallas grandes pal lado (horizontalmente extendidas)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida(
    onIrACatalogo: () -> Unit = {},
    onIrARegistro: () -> Unit = {},
    onIrASubirContenido: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Master Martini Chile SPA - Panel Técnico Expandido") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Panel Izquierdo: Identidad Corporativa y Resumen del Negocio
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Master Martini",
                        modifier = Modifier
                            .size(180.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Master Martini Chile SPA",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Impulsando la innovación en panaderías, pastelerías, chocolaterías y heladerías con contenidos técnicos accesibles.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Panel Derecho: Acciones Directas de Alto Contraste
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = "Accesos Rápidos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = onIrACatalogo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Explorar Recetas, Cursos y Videos", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = onIrASubirContenido,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Publicar o Editar Receta / Curso", style = MaterialTheme.typography.labelLarge)
                }

                OutlinedButton(
                    onClick = onIrARegistro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Registrar Mi Empresa y Preferencias", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview(name = "Expanded", widthDp = 840, heightDp = 600, showBackground = true)
@Composable
fun PreviewExpanded() {
    MasterMartiniTheme {
        HomeScreenExpandida()
    }
}