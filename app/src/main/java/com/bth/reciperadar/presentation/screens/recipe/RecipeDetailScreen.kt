package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.domain.controllers.InventoryController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.domain.controllers.ShoppingListController
import com.bth.reciperadar.presentation.screens.screen.Screen
import com.bth.reciperadar.presentation.viewmodels.CuisineViewModel
import com.bth.reciperadar.presentation.viewmodels.DietaryInfoViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.StepViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    recipeController: RecipeController,
    shoppingListController: ShoppingListController,
    inventoryController: InventoryController
) {
    var recipe by remember { mutableStateOf<RecipeViewModel?>(null) }
    var selectedIngredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }
    val state = rememberScrollState()

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
                .padding(20.dp)
                .verticalScroll(state)
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

                if (it.prepTime != null) {
                    Text(
                        text = "Preparation time: ${ it.prepTime!! }",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
                    )
                }

                if (it.servingAmount != null) {
                    Text(
                        text = "Amount of servings: ${ it.servingAmount!! }",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
                    )
                }

                it.cuisines?.let { cuisineList ->
                    Text(
                        text = "Cuisine(s):",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column {
                        cuisineList.forEach { cuisine ->
                            CuisineItem(cuisine)
                        }
                    }
                }

                it.dietaryInfo?.let { dietaryInfoList ->
                    Text(
                        text = "Dietary Information:",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column {
                        dietaryInfoList.forEach { dietaryInfo ->
                            DietaryInfoItem(dietaryInfo)
                        }
                    }
                }

                it.ingredients?.let { ingredients ->
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Ingredients:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).weight(1f)
                        )
                        Column(Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                            IconButton(modifier = Modifier
                                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 5.dp)
                                .height(35.dp)
                                .width(100.dp)
                                .fillMaxWidth(),
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            if (selectedIngredients.isNotEmpty()) {
                                                shoppingListController.addIngredientListToShoppingList(
                                                    selectedIngredients.map { it.toDomain() }
                                                )

                                                selectedIngredients = emptyList()
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    "Add to list ✅",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
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
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    if (selectedIngredients.isNotEmpty()) {
                                        inventoryController.removeIngredientListFromInventory(
                                            selectedIngredients.map { it.toDomain() }
                                        )

                                        selectedIngredients = emptyList()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Remove selected ingredients from inventory")
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
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 10.dp)
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

@Composable
fun DietaryInfoItem(dietaryInfo: DietaryInfoViewModel) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = dietaryInfo.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        if(dietaryInfo.description != null){
            Text(
                text = dietaryInfo.description!!,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Divider(modifier = Modifier.padding(vertical = 20.dp))
    }
}

@Composable
fun CuisineItem(cuisineItem: CuisineViewModel) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = cuisineItem.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        if(cuisineItem.description != null){
            Text(
                text = cuisineItem.description!!,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Divider(modifier = Modifier.padding(vertical = 20.dp))
    }
}


