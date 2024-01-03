package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bth.reciperadar.R
import com.bth.reciperadar.domain.controllers.CuisineController
import com.bth.reciperadar.domain.controllers.DietaryInfoController
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.IngredientTypeController
import com.bth.reciperadar.domain.controllers.InventoryController
import com.bth.reciperadar.domain.controllers.ProfileController
import com.bth.reciperadar.domain.controllers.RecipeController
import com.bth.reciperadar.presentation.viewmodels.CuisineViewModel
import com.bth.reciperadar.presentation.viewmodels.DietaryInfoViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientTypeViewModel
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.RecipeViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RecipeSearchScreen(
    searchQuery: String?,
    searchWithIngredients: Boolean,
    navController: NavController,
    recipeController: RecipeController,
    ingredientController: IngredientController,
    ingredientTypeController: IngredientTypeController,
    cuisineController: CuisineController,
    dietaryInfoController: DietaryInfoController,
    profileController: ProfileController,
    inventoryController: InventoryController
) {
    var searchTerm by remember { mutableStateOf(searchQuery) }
    var recipes by remember { mutableStateOf<List<RecipeViewModel>>(emptyList()) }
    var ingredientTypes by remember { mutableStateOf<List<IngredientTypeViewModel>>(emptyList()) }
    val state = rememberScrollState()
    var expandedCategories by remember { mutableStateOf<Set<String>>(setOf()) }
    var selectedIngredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }
    var anyRecipesWithSelectedIngredients by remember { mutableStateOf(false) }
    var dontAllowExtraIngredients by remember { mutableStateOf(false) }
    var isIngredientDropdownVisible by remember { mutableStateOf(false) }
    var cuisines by remember { mutableStateOf<List<CuisineViewModel>>(emptyList()) }
    var selectedCuisines by remember { mutableStateOf<List<CuisineViewModel>>(emptyList()) }
    var dietaryInfo by remember { mutableStateOf<List<DietaryInfoViewModel>>(emptyList()) }
    var selectedDietaryInfo by remember { mutableStateOf<List<DietaryInfoViewModel>>(emptyList()) }
    var isCuisineDropdownVisible by remember { mutableStateOf(false) }
    var isDietaryInfoDropdownVisible by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        withContext(Dispatchers.IO) {
            val selectedDietaryInfoModels = profileController.getProfile()?.dietaryInfo ?: emptyList()
            selectedDietaryInfo = selectedDietaryInfoModels.map { it.toViewModel() }

            if (searchWithIngredients) {
                val selectedIngredientModels = inventoryController.getInventory()?.ingredients ?: emptyList()
                selectedIngredients = selectedIngredientModels.map { it.toViewModel() }

                anyRecipesWithSelectedIngredients = true
            }

            val recipeModels = recipeController.searchRecipesByTitleAndFilters(
                searchQuery,
                selectedIngredients.map { it.toDomain() },
                emptyList(),
                selectedDietaryInfo.map { it.toDomain() },
                anyRecipesWithSelectedIngredients = anyRecipesWithSelectedIngredients,
                dontAllowExtraIngredients = dontAllowExtraIngredients
            )
            recipes = recipeModels.map { it.toViewModel() }

            val ingredientTypeModels = ingredientTypeController.getIngredientTypes()
            ingredientTypes = ingredientTypeModels.map { it.toViewModel() }

            val cuisineModels = cuisineController.getCuisines()
            cuisines = cuisineModels.map { it.toViewModel() }

            val dietaryModels = dietaryInfoController.getDietaryInfo()
            dietaryInfo = dietaryModels.map { it.toViewModel() }
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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = searchTerm ?: "",
            onValueChange = {
                searchTerm = it
            },
            modifier = Modifier.fillMaxWidth()
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

        if (isIngredientDropdownVisible) {
            Spacer(modifier = Modifier.height(5.dp))
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

        if (selectedIngredients.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Use any of the selected ingredients",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = anyRecipesWithSelectedIngredients,
                        onCheckedChange = {
                            anyRecipesWithSelectedIngredients = it
                            dontAllowExtraIngredients = false
                        },
                        modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Don't allow extra ingredients",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = dontAllowExtraIngredients,
                        onCheckedChange = {
                            dontAllowExtraIngredients = it
                            anyRecipesWithSelectedIngredients = false
                        },
                        modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isCuisineDropdownVisible = !isCuisineDropdownVisible }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filter cuisines",
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
        Spacer(modifier = Modifier.height(5.dp))

        if (isCuisineDropdownVisible) {
            CuisineAccordion(
                cuisines = cuisines,
                selectedCuisines = selectedCuisines,
                onCuisineSelect = { selectedCuisine ->
                    selectedCuisines = if (selectedCuisines.contains(selectedCuisine)) {
                        selectedCuisines.minus(selectedCuisine)
                    } else {
                        selectedCuisines.plus(selectedCuisine)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isDietaryInfoDropdownVisible = !isDietaryInfoDropdownVisible }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filter diet",
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
        Spacer(modifier = Modifier.height(5.dp))

        if (isDietaryInfoDropdownVisible) {
            DietaryInfoAccordion(
                dietaryInfoList = dietaryInfo,
                selectedDietaryInfo = selectedDietaryInfo,
                onDietaryInfoSelect = { dietaryInfoItem ->
                    selectedDietaryInfo = if (selectedDietaryInfo.contains(dietaryInfoItem)) {
                        selectedDietaryInfo.minus(dietaryInfoItem)
                    } else {
                        selectedDietaryInfo.plus(dietaryInfoItem)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    if (searchTerm == "") {
                        searchTerm = null
                    }

                    val recipeModels = recipeController.searchRecipesByTitleAndFilters(
                        searchQuery = searchTerm,
                        ingredientsList = selectedIngredients.map { it.toDomain() },
                        cuisinesList = selectedCuisines.map { it.toDomain() },
                        dietaryInfoList = selectedDietaryInfo.map { it.toDomain() },
                        anyRecipesWithSelectedIngredients = anyRecipesWithSelectedIngredients,
                        dontAllowExtraIngredients = dontAllowExtraIngredients
                    )
                    recipes = recipeModels.map { it.toViewModel() }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        ) {
            Text(text = "Search")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Found recipes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        RecipeListView(recipes = recipes, navController = navController)
        Spacer(modifier = Modifier.height(20.dp))
    }
}