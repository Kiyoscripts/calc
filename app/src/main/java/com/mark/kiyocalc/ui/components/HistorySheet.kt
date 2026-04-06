package com.mark.kiyocalc.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mark.kiyocalc.data.local.HistoryEntity

@Composable
fun HistorySheet(
    history: List<HistoryEntity>,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("History", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(history) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(item.expression) }
                        .padding(vertical = 8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(item.expression, style = MaterialTheme.typography.bodyLarge)
                        Text(item.result, style = MaterialTheme.typography.titleMedium)
                    }
                    Divider(modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
