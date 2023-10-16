package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(nextNav: () -> Unit) {

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onClick() },
                icon = { Icon(Icons.Filled.Add, "Voeg foto toe button") },
                text = { Text(text = "Voeg foto toe") },
            )

        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "RouteNummer 654481519849")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

    ) { values ->


        Column(
            modifier = Modifier
                .fillMaxSize() // Make the Column fill the available space
                .padding(values)
                .padding(top = 60.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Textfields()
            Button(onClick = { nextNav() }) {
                Text("Opslaan")

            }
        }


    }
}

fun onClick() {
    TODO("Not yet implemented")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Textfields() {

    var laadbonNummer by remember { mutableStateOf("654481519849") }
    var nummerplaat by remember { mutableStateOf("") }
    var chauffeur by remember { mutableStateOf("") }


    Image(
        painter = rememberAsyncImagePainter(
            model = "https://picsum.photos/seed/${Random.nextInt()}/900/500"
        ),
        contentDescription = null,
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth(1f)
            .padding(bottom = 20.dp)

    )
    TextField(
        value = laadbonNummer,
        onValueChange = { laadbonNummer = it },
        label = { Text("RouteNummer") },
        modifier = Modifier.padding(bottom = 5.dp)
    )

    TextField(
        value = nummerplaat,
        onValueChange = { nummerplaat = it },
        label = { Text("Nummerplaat") },
        modifier = Modifier.padding(bottom = 5.dp)
    )

    TextField(
        value = chauffeur,
        onValueChange = { chauffeur = it },
        label = { Text("Chauffeur") },
        modifier = Modifier.padding(bottom = 5.dp)
    )


}

