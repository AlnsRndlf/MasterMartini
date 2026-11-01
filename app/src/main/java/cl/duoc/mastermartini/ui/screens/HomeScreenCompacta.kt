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
import androidx.compose.material.icons.filled.PlayCircle
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

// para pantallas en vertical
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(
    onIrACatalogo: () -> Unit = {},
    onIrARegistro: () -> Unit = {},
    onIrASubirContenido: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Master Martini - Móvil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Master Martini",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 140.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "¡Bienvenido, Emprendedor Gastronómico!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Áreas Técnicas",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Contenidos de Valor",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = "Explora recetas simples, videos técnicos y cursos en chocolatería, pastelería de vitrina, heladería, bollería y horneo.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                onClick = onIrACatalogo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.MenuBook, contentDescription = "Catálogo")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Explorar Recetas y Cursos", style = MaterialTheme.typography.labelLarge)
            }

            Button(
                onClick = onIrASubirContenido,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = "Nuevo Contenido")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Gestionar / Subir Receta", style = MaterialTheme.typography.labelLarge)
            }

            OutlinedButton(
                onClick = onIrARegistro,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Registro Cliente")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Registrar Perfil de Cliente", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewCompact() {
    MasterMartiniTheme {
        HomeScreenCompacta()
    }
}