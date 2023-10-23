package com.qwict.svkandroid.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.theme.SVKTextfield

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(barcodeValue: String?, nextNav: () -> Unit) {
    val str = stringResource(R.string.title_edit_screen, "654481519849")

    Scaffold(
//        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                onClick = { onClick() },
//                icon = { Icon(Icons.Filled.Add, "Voeg foto toe button") },
//                text = { Text(text = "Voeg foto toe") },
//            )
//        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Laadbon 654481519849")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },

    ) { values ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(values)
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Textfields()
            Button(onClick = { nextNav() }) {
                Text("Opslaan")
            }
        }
    }
}

fun onClick() {
    Log.i("EditScreen", "Add button pressed: ")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Textfields() {
    var laadbonNummer by remember { mutableStateOf("654481519849") }
    var nummerplaat by remember { mutableStateOf("") }
    var chauffeur by remember { mutableStateOf("") }

//    Image(
//        painter = rememberAsyncImagePainter(
//            model = "https://picsum.photos/seed/${Random.nextInt()}/900/500",
//        ),
//        contentDescription = null,
//        modifier = Modifier
//            .clip(MaterialTheme.shapes.large)
//            .fillMaxWidth(1f)
//            .padding(bottom = 20.dp),
//
//    )
    SVKTextfield {
        OutlinedTextField(
            value = laadbonNummer,
            onValueChange = { laadbonNummer = it },
            label = { Text("Laadnummer") },
            modifier = Modifier.padding(bottom = 5.dp),
        )
    }

    SVKTextfield {
        OutlinedTextField(
            value = nummerplaat,
            onValueChange = { nummerplaat = it },
            label = { Text("Nummerplaat") },
            modifier = Modifier.padding(bottom = 5.dp),
        )
    }

    SVKTextfield {
        OutlinedTextField(
            value = chauffeur,
            onValueChange = { chauffeur = it },
            label = { Text("Chauffeur") },
            modifier = Modifier.padding(bottom = 5.dp),
        )
    }
}
