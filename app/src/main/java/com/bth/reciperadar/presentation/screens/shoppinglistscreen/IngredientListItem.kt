package com.bth.reciperadar.presentation.screens.shoppinglistscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel

@Composable
fun IngredientList(
    ingredientList: List<IngredientViewModel>,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit,
    onIngredientRemove: (IngredientViewModel) -> Unit
) {
    Column {
        ingredientList.forEach { ingredient ->
            IngredientListItem(
                ingredient = ingredient,
                selectedIngredients = selectedIngredients,
                onIngredientSelect = onIngredientSelect,
                onIngredientRemove = onIngredientRemove
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun IngredientListItem(
    ingredient: IngredientViewModel,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit,
    onIngredientRemove: (IngredientViewModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onIngredientSelect(ingredient) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Checkbox(
                checked = selectedIngredients.contains(ingredient),
                onCheckedChange = {
                    onIngredientSelect(ingredient)
                },
                modifier = Modifier.size(24.dp).align(CenterVertically).padding(horizontal = 20.dp)
            )

            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f).align(CenterVertically).padding(horizontal = 20.dp)
            )

            IconButton( onClick = { onIngredientRemove(ingredient) } ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "search_icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}