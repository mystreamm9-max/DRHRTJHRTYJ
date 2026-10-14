package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppConfig
import com.example.data.model.ClientItem
import com.example.data.model.JackpotValues
import com.example.data.model.ServerConfig
import com.example.data.model.TransactionItem
import com.example.data.protocol.ClientSearch
import com.example.ui.components.OperationMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED
}

data class UiState(
    // Regla de Oro 2: Cero datos inventados en la app instalada
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val connectionStatusText: String = "Sin conexión",
    val operator: String = "Sin operador",
    val jackpotValues: JackpotValues = JackpotValues(), // Cada una de las 6 celdas es "—"
    val clientList: List<ClientItem> = emptyList(), // "Sin lista de clientes" cuando está vacía
    val transactions: List<TransactionItem> = emptyList(), // "Sin conexión" cuando está vacía
    val serverConfig: ServerConfig = ServerConfig(),
    val appConfig: AppConfig = AppConfig(),

    // Modo actual de operación y alternancia de fila
    val currentMode: OperationMode = OperationMode.INGRESO,
    val isAlternateModeRow: Boolean = false,

    // Búsqueda de cliente
    val clientQuery: String = "",
    val searchResults: List<ClientItem> = emptyList(),
    val selectedClient: ClientItem? = null,

    // Modales y menús
    val showOperatorDialog: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val activeSubScreen: String? = null,

    // Mensaje de estado/toast
    val toastMessage: String? = null
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun selectMode(mode: OperationMode) {
        _uiState.update { it.copy(currentMode = mode) }
    }

    fun toggleModeRow() {
        _uiState.update { it.copy(isAlternateModeRow = !it.isAlternateModeRow) }
    }

    fun updateClientQuery(query: String) {
        _uiState.update { current ->
            val results = if (query.isBlank()) {
                emptyList()
            } else {
                ClientSearch.search(current.clientList, query)
            }
            current.copy(
                clientQuery = query,
                searchResults = results,
                selectedClient = null
            )
        }
    }

    fun selectClient(client: ClientItem) {
        _uiState.update {
            it.copy(
                clientQuery = client.n,
                selectedClient = client,
                searchResults = emptyList()
            )
        }
    }

    fun setOperator(operatorName: String) {
        _uiState.update {
            it.copy(
                operator = operatorName.ifBlank { "Sin operador" },
                showOperatorDialog = false
            )
        }
    }

    fun openOperatorDialog() {
        _uiState.update { it.copy(showOperatorDialog = true) }
    }

    fun closeOperatorDialog() {
        _uiState.update { it.copy(showOperatorDialog = false) }
    }

    fun openSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = true) }
    }

    fun closeSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = false) }
    }

    fun openSubScreen(name: String) {
        if (name == "Configuración de Servidor") {
            openSettingsDialog()
        } else {
            _uiState.update { it.copy(activeSubScreen = name) }
        }
    }

    fun closeSubScreen() {
        _uiState.update { it.copy(activeSubScreen = null) }
    }

    fun saveConfig(host: String, room: String, clientName: String) {
        _uiState.update {
            it.copy(
                appConfig = it.appConfig.copy(
                    host = host.trim(),
                    room = room.trim().replace(Regex("[^A-Za-z0-9_-]"), "-"),
                    clientName = clientName.trim()
                ),
                showSettingsDialog = false
            )
        }
        showToast("Configuración guardada para sala ${uiState.value.appConfig.room}")
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
    }
}
