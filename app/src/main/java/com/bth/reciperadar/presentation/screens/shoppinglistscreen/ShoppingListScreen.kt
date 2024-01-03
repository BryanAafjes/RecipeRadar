package com.bth.reciperadar.presentation.screens.shoppinglistscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.InventoryController
import com.bth.reciperadar.domain.controllers.ShoppingListController
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.ShoppingListViewModel
import com.bth.reciperadar.presentation.viewmodels.toDomain
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import com.bth.reciperadar.ui.theme.SuccessGreen
import com.bth.reciperadar.ui.theme.WarningRed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.material3.IconButton as IconButton
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.layout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.customDialogModifier() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight) { placeable.place(0, constraints.maxHeight/5, 10f) }
}

@Composable
fun DialogWithAutoDismiss(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    dismissAfterMillis: Long = 1200
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier.customDialogModifier(),
            backgroundColor = Color.Red,
            contentColor = Color.White,
            shape = RoundedCornerShape(15.dp),
            onDismissRequest = onDismiss,
            text = {
                Text(
                    text = "Ingredient not found, please try again.",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(600),
                    fontSize = 16.sp
                )
            },
            buttons = {}
        )

        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                delay(dismissAfterMillis)
                onDismiss()
            }
        }
    }
}

@Composable
fun ShoppingListScreen(
    ingredientController: IngredientController,
    shoppingListController: ShoppingListController,
    inventoryController: InventoryController
) {
    var searchText by remember { mutableStateOf( "") }
    var shoppingList by remember { mutableStateOf<ShoppingListViewModel?>(ShoppingListViewModel("", "", emptyList())) }
    var ingredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }
    var selectedIngredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }
    var isIngredientFound by remember { mutableStateOf( true) }
    val state = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val shoppingListModel = shoppingListController.getShoppingList()
                if (shoppingListModel != null) {
                    shoppingList = shoppingListModel.toViewModel()
                    ingredients = shoppingList?.ingredients ?: emptyList()
                    selectedIngredients = shoppingList?.checkedIngredients ?: emptyList()
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
                    if (shoppingList != null) {
                        shoppingList!!.ingredients = ingredients
                        shoppingList!!.checkedIngredients = selectedIngredients
                        shoppingListController.createOrUpdateShoppingList(shoppingList?.toDomain()!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    if (shoppingList != null) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .verticalScroll(state)
        ) {
            Text(
                text = "Shopping List",
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
                        showDialog = true
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

            if (!isIngredientFound) { DialogWithAutoDismiss( showDialog = showDialog, onDismiss = { showDialog = false } ) }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                IngredientList(
                    ingredientList = ingredients,
                    selectedIngredients = selectedIngredients,
                    onIngredientSelect = { ingredient ->
                        selectedIngredients = if (selectedIngredients.contains(ingredient)) {
                            selectedIngredients.minus(ingredient)
                        } else {
                            selectedIngredients.plus(ingredient)
                        }
                    },
                    onIngredientRemove = { ingredient ->
                        ingredients = ingredients.minus(ingredient)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                    .background(WarningRed)
                    .height(75.dp)
                    .fillMaxWidth(0.45f),
                    onClick = { ingredients = emptyList() }) {
                    Text(
                        text = "Clear List",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                    .background(SuccessGreen)
                    .height(75.dp)
                    .fillMaxWidth(0.8f),
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                if (selectedIngredients.isNotEmpty()) {
                                    inventoryController.addIngredientListToInventory(
                                        selectedIngredients.map { it.toDomain() }
                                    )

                                    ingredients = ingredients.minus(selectedIngredients)

                                    selectedIngredients = emptyList()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }) {
                    Text(
                        "Add selected ingredients to inventory",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}