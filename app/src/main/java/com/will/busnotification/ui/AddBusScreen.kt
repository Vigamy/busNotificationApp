package com.will.busnotification.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.ui.components.HeaderComponent
import com.will.busnotification.viewmodel.AddBusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusScreen(
    navController: NavHostController,
    viewModel: AddBusViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    // val isSearching by viewModel.isSearching.collectAsState() // Descomente se precisar mostrar um indicador de loading

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderComponent(
            text = "Adicionar Notificação",
            hasBack = true,
            onBackClick = { navController.popBackStack() }
        )

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onSearch = {},
            active = searchResults.isNotEmpty() || searchQuery.isNotBlank(),
            onActiveChange = {},
            placeholder = { Text("Pesquisar parada ou endereço") }
        ) {
            LazyColumn {
                items(searchResults) { place ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: Ação ao clicar no resultado */ }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ícone de localização",
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Text(text = place.name)
                    }
                }
            }
        }

        // A lista de ônibus por proximidade pode ser adicionada aqui no futuro
        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun AddBusScreenPreview() {
    // Nota: O preview não fará chamadas de API reais.
    AddBusScreen(navController = rememberNavController())
}
