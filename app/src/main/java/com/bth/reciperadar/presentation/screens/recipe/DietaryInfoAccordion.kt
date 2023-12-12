package com.bth.reciperadar.presentation.screens.recipe

import com.bth.reciperadar.presentation.viewmodels.DietaryInfoViewModel
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

@Composable
fun DietaryInfoAccordion(
    dietaryInfoList: List<DietaryInfoViewModel>,
    selectedDietaryInfo: List<DietaryInfoViewModel>,
    onDietaryInfoSelect: (DietaryInfoViewModel) -> Unit
) {
    Column {
        dietaryInfoList.forEach { dietaryInfo ->
            DietaryInfoItem(
                dietaryInfo = dietaryInfo,
                selectedDietaryInfo = selectedDietaryInfo,
                onDietaryInfoSelect = onDietaryInfoSelect
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DietaryInfoItem(
    dietaryInfo: DietaryInfoViewModel,
    selectedDietaryInfo: List<DietaryInfoViewModel>,
    onDietaryInfoSelect: (DietaryInfoViewModel) -> Unit
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
            .clickable { onDietaryInfoSelect(dietaryInfo) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = dietaryInfo.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Checkbox(
                checked = selectedDietaryInfo.contains(dietaryInfo),
                onCheckedChange = {
                    onDietaryInfoSelect(dietaryInfo)
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }
}