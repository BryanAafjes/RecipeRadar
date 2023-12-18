package com.bth.reciperadar.presentation.screens.recipe

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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.R
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel

@Composable
fun IngredientTypeAccordion(
    ingredientTypes: List<IngredientTypeViewModel>,
    expandedCategories: Set<String>,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit,
    onCategoryToggle: (IngredientTypeViewModel) -> Unit
) {
    Column {
        ingredientTypes.forEach { ingredientType ->
            IngredientTypeAccordionItem(
                ingredientType = ingredientType,
                isExpanded = expandedCategories.contains(ingredientType.id),
                selectedIngredients = selectedIngredients,
                onIngredientSelect = onIngredientSelect,
                onToggle = { onCategoryToggle(ingredientType) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun IngredientTypeAccordionItem(
    ingredientType: IngredientTypeViewModel,
    isExpanded: Boolean,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onToggle() }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = ingredientType.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            if (isExpanded) {
                ingredientType.ingredients?.let {
                    for (ingredient in it) {
                        IngredientItem(
                            ingredient = ingredient,
                            selectedIngredients = selectedIngredients,
                            onIngredientSelect = onIngredientSelect
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: IngredientViewModel,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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
                .padding(16.dp)
        ) {
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Checkbox(
                checked = selectedIngredients.any { it.id == ingredient.id },
                onCheckedChange = {
                    onIngredientSelect(ingredient)
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }
}