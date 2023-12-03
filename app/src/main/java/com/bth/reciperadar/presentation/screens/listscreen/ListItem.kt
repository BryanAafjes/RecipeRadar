package com.bth.reciperadar.presentation.screens.listscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel

data class Item(val itemName: String, var localCheckClsVal: MutableState<Boolean> = mutableStateOf(false))
class ItemsList: ViewModel() {
    private var _elements = mutableStateListOf<Item>()
    var elements: List<Item> = _elements
    fun addElement( itemName: String ) { _elements.add(Item(itemName)) }
    fun removeElement( itemID: Item ) { _elements.remove(itemID); }
    fun removeAll() { _elements.removeAll(_elements) }
}

@Composable
fun ShowElement(itemsList: ItemsList, item: Item, itemName: String, localCheckClsVal: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(18.dp))
            .background(hexToComposeColor("#485E9D"))
            .fillMaxWidth()
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Checkbox(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxHeight()
                .fillMaxWidth(),
            checked = localCheckClsVal.value,
            onCheckedChange = { item.localCheckClsVal.value = !localCheckClsVal.value; } ,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                uncheckedColor = Color.White,
                checkmarkColor = hexToComposeColor("#485E9D")
            )
        )
        Text(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth()
                .padding(start = 15.dp),
            text = itemName,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight(600),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton( onClick = { itemsList.removeElement(item); } ) {
            Icon(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                imageVector = Icons.Default.Close,
                contentDescription = "search_icon",
                tint = Color.White
            )
        }
    }
}