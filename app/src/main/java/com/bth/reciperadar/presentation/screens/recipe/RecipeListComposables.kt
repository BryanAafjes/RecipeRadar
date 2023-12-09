package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel

@Composable
fun RecipeListView(recipes: List<RecipeViewModel>) {
    Column {
        recipes.forEach { recipe ->
            RecipeItem(recipe)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RecipeItem(recipe: RecipeViewModel) {
    Column {
        Text(text = "Recipe:")
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {
            Column(modifier = Modifier) {
                Text(text = "Title:")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Description:")
            }
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Text(text = recipe.title)
                Spacer(modifier = Modifier.height(8.dp))
                recipe.description?.let { Text(text = it) }
            }
        }
    }
}