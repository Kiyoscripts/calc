package com.mark.kiyocalc.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mark.kiyocalc.domain.model.CalculatorAction
import com.mark.kiyocalc.presentation.CalculatorViewModel
import com.mark.kiyocalc.ui.components.CalculatorButton
import com.mark.kiyocalc.ui.components.HistorySheet

private data class Key(val label: String, val accent: Boolean = false, val action: CalculatorAction)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val state by viewModel.state.collectAsState()
    var showHistory by remember { mutableStateOf(false) }

    val keys = listOf(
        Key("AC", action = CalculatorAction.AllClear),
        Key("+/-", action = CalculatorAction.ToggleSign),
        Key("%", action = CalculatorAction.Percent),
        Key("÷", accent = true, action = CalculatorAction.Operator("÷")),
        Key("7", action = CalculatorAction.Number("7")),
        Key("8", action = CalculatorAction.Number("8")),
        Key("9", action = CalculatorAction.Number("9")),
        Key("×", accent = true, action = CalculatorAction.Operator("×")),
        Key("4", action = CalculatorAction.Number("4")),
        Key("5", action = CalculatorAction.Number("5")),
        Key("6", action = CalculatorAction.Number("6")),
        Key("-", accent = true, action = CalculatorAction.Operator("-")),
        Key("1", action = CalculatorAction.Number("1")),
        Key("2", action = CalculatorAction.Number("2")),
        Key("3", action = CalculatorAction.Number("3")),
        Key("+", accent = true, action = CalculatorAction.Operator("+")),
        Key("0", action = CalculatorAction.Number("0")),
        Key(".", action = CalculatorAction.Decimal),
        Key("⌫", action = CalculatorAction.Delete),
        Key("=", accent = true, action = CalculatorAction.Equals)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KiyoCalc") },
                actions = {
                    IconButton(onClick = { showHistory = true }) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = if (state.expression.isBlank()) "0" else state.expression,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = state.result,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.End,
                    color = if (state.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
                state.errorMessage?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(0.65f)
            ) {
                items(keys) { key ->
                    CalculatorButton(
                        label = key.label,
                        accent = key.accent,
                        onClick = { viewModel.onAction(key.action) }
                    )
                }
            }
        }
    }

    if (showHistory) {
        ModalBottomSheet(onDismissRequest = { showHistory = false }) {
            HistorySheet(history = state.history) {
                viewModel.onAction(CalculatorAction.RestoreHistory(it))
                showHistory = false
            }
        }
    }
}
