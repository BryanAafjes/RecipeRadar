package com.bth.reciperadar.presentation.screens.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bth.reciperadar.presentation.viewmodels.CuisineViewModel

@Composable
fun CuisineAccordion(
    cuisines: List<CuisineViewModel>,
    selectedCuisines: List<CuisineViewModel>,
    onCuisineSelect: (CuisineViewModel) -> Unit
) {
    Column {
        cuisines.forEach { cuisine ->
            CuisineItem(
                cuisine = cuisine,
                selectedCuisines = selectedCuisines,
                onCuisineSelect = onCuisineSelect
            )
        }
    }
}

@Composable
fun CuisineItem(
    cuisine: CuisineViewModel,
    selectedCuisines: List<CuisineViewModel>,
    onCuisineSelect: (CuisineViewModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onCuisineSelect(cuisine) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = cuisine.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Checkbox(
                checked = selectedCuisines.contains(cuisine),
                onCheckedChange = {
                    onCuisineSelect(cuisine)
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}