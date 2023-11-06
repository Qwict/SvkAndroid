package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.ui.theme.SVKTextfield
import com.qwict.svkandroid.ui.viewModels.MainViewModel

@Composable
fun EditScreen(nextNav: () -> Unit, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
            .padding(top = 60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Textfields()
        Button(onClick = {
            nextNav()
            viewModel.laadBonnen.add(viewModel.currentBarcode.value)
        }) {
            Text("Save laadbon")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Textfields() {
    var laadbonNummer by remember { mutableStateOf("654481519849") }

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
            singleLine = true,
        )
    }
}
