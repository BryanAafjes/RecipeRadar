package com.bth.reciperadar.presentation.screens.listscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.IconButton as IconButton

@Composable
fun ListScreen() { Overall_look() }

fun hexToComposeColor(hexColor: String): Color {
    val cleanHex = if (hexColor.startsWith("#")) hexColor.substring(1) else hexColor
    return Color(android.graphics.Color.parseColor("#$cleanHex"))
}

val itemsList: ItemsList = ItemsList()
val elements = itemsList.elements

@Preview(showBackground = true)
@Composable
fun Overall_look(  ) {
    println("Compose main")
    //var recomposeTrigger = remember { mutableStateOf(false) }
    Column( modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)) {
        var searchText by remember { mutableStateOf( "") }
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
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f)
            ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(text = "Type ingredient nane to add:") },
                singleLine = true,
                trailingIcon = { IconButton( onClick = { itemsList.addElement(searchText) } )
                { Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "search_icon"
                ) }  },
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                colors = OutlinedTextFieldDefaults.colors( focusedBorderColor = Color.Blue ) ,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f)
                .background(Color.Transparent),
        ) { items( elements ) { elem -> elem.ShowElement() } }

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
                onClick = { itemsList.removeAll() } ) {
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
