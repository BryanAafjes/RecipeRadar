package com.bth.reciperadar.presentation.screens.inventoryscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.InventoryController
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.InventoryViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import com.bth.reciperadar.ui.theme.WarningRed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun InventoryScreen(
    ingredientController: IngredientController,
    inventoryController: InventoryController
) {
    var searchText by remember { mutableStateOf( "") }
    var inventory by remember { mutableStateOf<InventoryViewModel?>(InventoryViewModel("", "", emptyList())) }
    var ingredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }
    var isIngredientFound by remember { mutableStateOf( true) }
    val state = rememberScrollState()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val inventoryModel = inventoryController.getInventory()
                if (inventoryModel != null) {
                    inventory = inventoryModel.toViewModel()
                    ingredients = inventory?.ingredients ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (inventory != null) {
                        inventory!!.ingredients = ingredients
                        inventoryController.createOrUpdateInventory(inventory?.toDomain()!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    if (inventory != null) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(state)
        ) {
            Text(
                text = "Inventory",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(text = "Type ingredient name to add:") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val foundIngredient =
                                    ingredientController.searchIngredientsByName(searchText)

                                if (foundIngredient != null) {
                                    ingredients = ingredients.plus(foundIngredient.toViewModel())
                                    isIngredientFound = true
                                } else {
                                    isIngredientFound = false
                                }

                                searchText = ""
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "search_icon"
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            if (!isIngredientFound) {
                Card(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(containerColor = WarningRed)
                ) {
                    Text(
                        "Ingredient not found, please try again.",
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                IngredientList(
                    ingredientList = ingredients,
                    onIngredientRemove = { ingredient ->
                        ingredients = ingredients.minus(ingredient)
                    }
                )
            }
        }
    }
}