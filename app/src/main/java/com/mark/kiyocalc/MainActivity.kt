package com.mark.kiyocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mark.kiyocalc.data.local.CalculatorDatabase
import com.mark.kiyocalc.data.repository.HistoryRepository
import com.mark.kiyocalc.presentation.CalculatorViewModel
import com.mark.kiyocalc.presentation.CalculatorViewModelFactory
import com.mark.kiyocalc.ui.screen.CalculatorScreen
import com.mark.kiyocalc.ui.theme.KiyoCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = HistoryRepository(CalculatorDatabase.getInstance(applicationContext).historyDao())
        val factory = CalculatorViewModelFactory(repository)

        setContent {
            KiyoCalcTheme {
                val viewModel: CalculatorViewModel = viewModel(factory = factory)
                CalculatorScreen(viewModel = viewModel)
            }
        }
    }
}
