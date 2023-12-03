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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel

class ItemsList: ViewModel() {
    inner class Item(private val itemName: String) {
        @Composable
        fun ShowElement() {
            var localCheckValue by rememberSaveable { mutableStateOf(false) }
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
                    checked = localCheckValue,
                    onCheckedChange = { localCheckValue = !localCheckValue } ,
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
                IconButton( onClick = { removeElement(this@Item) } ) {
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
    }

    private var _elements = mutableStateListOf<Item>()
    var elements: List<Item> = _elements
    fun addElement( itemName: String ) { _elements.add(Item(itemName)) }
    fun removeElement( itemID: Item ) {
        for ( item in _elements) {
            if (item == itemID) {
                _elements.remove(item)
                break
            }
        }
    }
    fun removeAll() { _elements.removeAll(_elements) }
}