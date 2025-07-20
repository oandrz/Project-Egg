package starbright.com.projectegg.compose.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.local.SearchHistory

@Composable
fun SearchHistoryItem(
    searchHistory: SearchHistory,
    onItemClick: (String) -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(searchHistory.query) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = searchHistory.query,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        
        IconButton(
            onClick = { onDeleteClick((searchHistory.id ?: 0).toLong()) }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete search history",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchHistoryItemPreview() {
    ChefnutTheme {
        SearchHistoryItem(
            searchHistory = SearchHistory(
                id = 1,
                query = "Chicken pasta recipe",
                createdAt = System.currentTimeMillis()
            ),
            onItemClick = {},
            onDeleteClick = {}
        )
    }
} 