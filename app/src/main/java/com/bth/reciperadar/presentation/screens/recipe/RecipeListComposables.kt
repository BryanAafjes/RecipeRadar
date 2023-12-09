package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bth.reciperadar.presentation.screens.screen.Screen
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel

@Composable
fun RecipeListView(recipes: List<RecipeViewModel>, navController: NavController) {
    Column {
        recipes.forEach { recipe ->
            RecipeItem(recipe, navController = navController)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RecipeItem(recipe: RecipeViewModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.RecipeDetailScreen.withArgs(recipe.id)) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Recipe:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Title:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Description:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(modifier = Modifier.weight(2f).padding(start = 16.dp)) {
                    Text(text = recipe.title, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    recipe.description?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                }
            }
        }
    }
}
