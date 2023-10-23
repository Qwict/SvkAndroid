package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteEditScreen(nextNav: () -> Unit, photoNav: () -> Unit, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Route Edit",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
        )
        Row {
            LazyRow(userScrollEnabled = true) {
                items(5) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp),
                    )
                }
                item {
                    IconButton(
                        onClick = { photoNav() },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add photo",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.laadBonnen.size) { laadbon ->
                ListItem(
                    headlineText = { Text(text = "BarcodeNr") },
                    supportingText = {
                        Text(
                            text = "1507$laadbon",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        headlineColor = MaterialTheme.colorScheme.onPrimary,
                        supportingColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Button(onClick = { nextNav() }) {
                Text(text = "Add laadbon")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RouteEditScreenPreview() {
    SvkAndroidTheme(darkTheme = false) {
        RouteEditScreen(nextNav = {}, photoNav = {}, viewModel = MainViewModel())
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RouteEditDarkScreenPreview() {
    SvkAndroidTheme(darkTheme = true) {
        RouteEditScreen(nextNav = {}, photoNav = {}, viewModel = MainViewModel())
    }
}
