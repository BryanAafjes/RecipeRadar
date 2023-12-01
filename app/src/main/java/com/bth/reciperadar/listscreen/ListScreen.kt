package com.bth.reciperadar.listscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListScreen() {
    Overall_look()
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text(text = "List Screen")
//    }
}

@Preview(showBackground = true)
@Composable
fun Overall_look() {
    Column( modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)) {
        var searchText by remember { mutableStateOf( "Type ingredient nane to add:") }
        var activated by remember { mutableStateOf( false) }
        Text(
            text = "Shopping List",
            color = Color.White,
            textAlign = TextAlign.Start,
            fontSize = 30.sp,
            fontWeight = FontWeight(800),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 30.dp, top = 15.dp, bottom = 5.dp)
                .background(Color.Transparent)
        )

        TextField(
            value = searchText,
            onValueChange = { newText ->
                if (!activated) {
                    searchText = ""
                    activated = true
                } else searchText = newText
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(Color.Transparent)
                .padding(12.dp)
                .clip(shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)),
        )

        LazyColumn(
            modifier = Modifier
                .weight(8f)
                .background(Color.Transparent)
        ) { /*TODO*/ }

        Row( modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(6.dp)
            .weight(1f),
            Arrangement.SpaceEvenly
        ) {
            IconButton( modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(Color.Red)
                .height(100.dp)
                .padding(6.dp)
                .fillMaxWidth(0.45f),
                onClick = { /*TODO*/ } ) {
                Text(text = "Clear List",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(800) )
            }

            IconButton( modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(Color.Green)
                .height(100.dp)
                .padding(6.dp)
                .fillMaxWidth(0.8f),
                onClick = { /*TODO*/ } ) {
                Text("Add ingredients to storage",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(800)
                )
            }
        }
    }

}