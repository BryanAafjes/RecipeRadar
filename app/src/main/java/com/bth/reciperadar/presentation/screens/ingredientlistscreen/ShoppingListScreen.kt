package com.bth.reciperadar.presentation.screens.ingredientlistscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bth.reciperadar.domain.controllers.IngredientController
import com.bth.reciperadar.domain.controllers.ShoppingListController
import com.bth.reciperadar.presentation.viewmodels.IngredientViewModel
import com.bth.reciperadar.presentation.viewmodels.ShoppingListViewModel
import com.bth.reciperadar.presentation.viewmodels.toViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material3.IconButton as IconButton

@Composable
fun ShoppingListScreen(
    ingredientController: IngredientController,
    shoppingListController: ShoppingListController
) {
    var searchText by remember { mutableStateOf( "") }
    var shoppingList by remember { mutableStateOf<ShoppingListViewModel?>(null) }
    var ingredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }
    var selectedIngredients by remember { mutableStateOf<List<IngredientViewModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val shoppingListModel = shoppingListController.getShoppingList()
            shoppingList = shoppingListModel?.toViewModel()

            ingredients = shoppingList?.ingredients ?: emptyList()
        }
    }

    Column( modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
    ) {
        Text(
            text = "Shopping List",
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            fontSize = 30.sp,
            fontWeight = FontWeight(800),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 30.dp, top = 15.dp, bottom = 5.dp)
        )
        Row (
            modifier = Modifier
                .background(Color.Transparent)
                .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(text = "Type ingredient name to add:") },
                singleLine = true,
                trailingIcon = { IconButton( onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val foundIngredient = ingredientController.searchIngredientsByName(searchText)

                            if (foundIngredient != null){
                                ingredients = ingredients.plus(foundIngredient.toViewModel())
                            }

                            searchText = ""
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "search_icon"
                    )
                }},
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                colors = OutlinedTextFieldDefaults.colors( focusedBorderColor = MaterialTheme.colorScheme.primary ) ,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(horizontal = 20.dp)
            )
        }

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
                .padding(6.dp)
                .weight(1f),
            Arrangement.SpaceEvenly
        ) {
            IconButton( modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(Color.Red)
                .height(100.dp)
                .padding(6.dp)
                .fillMaxWidth(0.45f),
                onClick = { ingredients = emptyList() } ) {
                Text(text = "Clear List",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(800) )
            }

            IconButton( modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(Color.Green)
                .height(100.dp)
                .padding(6.dp)
                .fillMaxWidth(0.8f),
                onClick = { /*TODO*/ } ) {
                Text("Add ingredients to storage",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight(800)
                )
            }
        }
    }
}