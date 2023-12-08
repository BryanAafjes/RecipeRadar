package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel

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
    Column {
        Text(
            text = ingredientType.name,
            modifier = Modifier.clickable { onToggle() }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isExpanded) {
            ingredientType.ingredients?.let { IngredientListView(
                ingredients = it, selectedIngredients = selectedIngredients, onIngredientSelect = onIngredientSelect
            ) }
        }
    }
}

@Composable
fun IngredientListView(
    ingredients: List<IngredientViewModel>,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit
) {
    Column {
        ingredients.forEach { ingredient ->
            IngredientItem(ingredient, selectedIngredients = selectedIngredients, onIngredientSelect = onIngredientSelect)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: IngredientViewModel,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 15.dp)) {
        Column(modifier = Modifier) {
            Text(text = "Title:")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description:")
        }
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Text(text = ingredient.name)
            Spacer(modifier = Modifier.height(8.dp))
            ingredient.description?.let { Text(text = it) }
        }
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Checkbox(
                checked = selectedIngredients.contains(ingredient),
                onCheckedChange = {
                    onIngredientSelect(ingredient)
                }
            )
        }
    }
}