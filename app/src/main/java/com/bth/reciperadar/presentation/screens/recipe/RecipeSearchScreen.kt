package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.R
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.IngredientTypeController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RecipeSearchScreen(
    searchQuery: String,
    recipeController: RecipeController,
    ingredientController: IngredientController,
    ingredientTypeController: IngredientTypeController
) {
    var recipes by remember { mutableStateOf<List<RecipeViewModel>>(emptyList()) }
    var ingredientTypes by remember { mutableStateOf<List<IngredientTypeViewModel>>(emptyList()) }
    val state = rememberScrollState()
    var expandedCategories by remember { mutableStateOf<Set<String>>(setOf()) }
    var selectedIngredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }
    var isIngredientDropdownVisible by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        withContext(Dispatchers.IO) {
            val recipeModels = recipeController.searchRecipes(searchQuery)
            recipes = recipeModels.map { it.toViewModel() }

            val ingredientTypeModels = ingredientTypeController.getIngredientTypes()
            ingredientTypes = ingredientTypeModels.map { it.toViewModel() }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .verticalScroll(state)
    ) {
        Text(
            text = "Recipe Search",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isIngredientDropdownVisible = !isIngredientDropdownVisible }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filter ingredients",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_filter_list_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (isIngredientDropdownVisible) {
            IngredientTypeAccordion(
                ingredientTypes = ingredientTypes,
                expandedCategories = expandedCategories,
                selectedIngredients = selectedIngredients,
                onIngredientSelect = { selectedIngredient ->
                    selectedIngredients = if (selectedIngredients.contains(selectedIngredient)) {
                        selectedIngredients.minus(selectedIngredient)
                    } else {
                        selectedIngredients.plus(selectedIngredient)
                    }
                },
                onCategoryToggle = { toggledIngredientType ->
                    expandedCategories =
                        if (expandedCategories.contains(toggledIngredientType.id)) {
                            expandedCategories.minus(toggledIngredientType.id)
                        } else {
                            expandedCategories.plus(toggledIngredientType.id)
                        }

                    if (expandedCategories.contains(toggledIngredientType.id)) {
                        val categoryIndex = ingredientTypes.indexOfFirst { it.id == toggledIngredientType.id }

                        if (toggledIngredientType.ingredients == null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val ingredientModels =
                                        ingredientController.getIngredientsForIngredientType(
                                            toggledIngredientType.id
                                        )

                                    val updatedIngredientTypes = ingredientTypes.toMutableList()

                                    updatedIngredientTypes[categoryIndex] =
                                        updatedIngredientTypes[categoryIndex].copy(ingredients = ingredientModels.map { it.toViewModel() })

                                    ingredientTypes = updatedIngredientTypes
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Recipe Search",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        RecipeListView(recipes = recipes)
        Spacer(modifier = Modifier.height(20.dp))
    }
}