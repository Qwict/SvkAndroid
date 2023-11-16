package com.qwict.svkandroid.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qwict.svkandroid.R
import com.qwict.svkandroid.SvkAndroidApp
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme
import com.qwict.svkandroid.ui.viewModels.TransportViewModel
import java.time.LocalDate
import java.util.Date
import java.util.UUID

// @Composable
// fun DotIndicators(
//    ...,
//    modifier: Modifier
// ) {
//    Row(modifier = modifier) {
//        repeat(pageCount) { iteration ->
//            val color = if (pagerState.currentPage == iteration) Color.White else Color.LightGray
//            Box(
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .background(color)
//            )
//        }
//    }
// }
@Composable
fun Carousel(
    transport : TransportDto,
    transportViewModel : TransportViewModel = hiltViewModel()
){
    LazyRow (
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(transportViewModel.images) { index,image,   ->
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                )
            }

        }
        item {
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(200.dp),
                onClick = {  },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add photo",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

    }
}

@Preview
@Composable
fun imgCarouselPreview(){
    val imgList = listOf<Int>(R.drawable.transport_one, R.drawable.transport_two, R.drawable.transport_four, R.drawable.transport_three)
    val transport = TransportDto(
        routeNumber = "Route1",
        routeDate = Date(2023,8,4),
        cargoDate = Date(2023,8,4),
        driver = "Maarten",
        licensePlate = "1-DDS-809",
        imagesBlobUuid = UUID.randomUUID()
    )

    SvkAndroidTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Carousel(transport = transport)
        }
    }
}