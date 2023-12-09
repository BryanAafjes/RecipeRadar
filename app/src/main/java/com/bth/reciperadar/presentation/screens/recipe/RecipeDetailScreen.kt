package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.StepViewModel
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    recipeController: RecipeController
) {
    var recipe by remember { mutableStateOf<RecipeViewModel?>(null) }
    var selectedIngredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }

    LaunchedEffect(recipeId) {
        withContext(Dispatchers.IO) {
            val recipeModel = recipeController.getRecipeById(recipeId)
            recipe = recipeModel?.toViewModel()
        }
    }

    recipe?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = it.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                if (it.description != null) {
                    Text(
                        text = it.description!!,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                it.ingredients?.let { ingredients ->
                    Text(
                        text = "Ingredients:",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column {
                        ingredients.forEach { ingredient ->
                            IngredientDetailItem(
                                ingredient,
                                selectedIngredients = selectedIngredients,
                                onIngredientSelect = { selectedIngredient ->
                                    selectedIngredients = if (selectedIngredients.contains(selectedIngredient)) {
                                        selectedIngredients.minus(selectedIngredient)
                                    } else {
                                        selectedIngredients.plus(selectedIngredient)
                                    }
                                },
                            )
                        }
                    }
                }

                it.steps?.let { steps ->
                    Text(
                        text = "Steps:",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column {
                        steps.forEach { step ->
                            StepItem(step)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientDetailItem(
    ingredient: IngredientViewModel,
    selectedIngredients: List<IngredientViewModel>,
    onIngredientSelect: (IngredientViewModel) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Row {
            Checkbox(
                checked = selectedIngredients.contains(ingredient),
                onCheckedChange = {
                    onIngredientSelect(ingredient)
                },
                modifier = Modifier.align(Alignment.CenterVertically).padding(end = 10.dp)
            )
            Column {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                if (ingredient.amount != null) {
                    Text(
                        text = "Amount: ${ingredient.amount}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(vertical = 10.dp))
    }
}


@Composable
fun StepItem(step: StepViewModel) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Row {
            Text(
                text = step.number.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                if(step.description != null){
                    Text(
                        text = step.description!!,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(vertical = 20.dp))
    }
}


