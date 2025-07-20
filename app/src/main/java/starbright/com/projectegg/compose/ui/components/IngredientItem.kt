package starbright.com.projectegg.compose.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.Ingredient

@Composable
fun IngredientItem(
    ingredient: Ingredient,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag("ingredient_checkbox_${ingredient.id}")
        )
        
        Text(
            text = ingredient.name ?: "",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientItemPreview() {
    ChefnutTheme {
        IngredientItem(
            ingredient = Ingredient("400g pasta").apply {
                setId("1")
                setAmount(400)
                setUnit("g")
            },
            isChecked = true,
            onCheckedChange = {}
        )
    }
} 