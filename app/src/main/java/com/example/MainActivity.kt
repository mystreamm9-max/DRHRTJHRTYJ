package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClientItem
import com.example.ui.components.JackpotGrid
import com.example.ui.components.ModeRow
import com.example.ui.components.OperadorDialog
import com.example.ui.components.OperationMode
import com.example.ui.components.SettingsDialog
import com.example.ui.components.TopBar
import com.example.ui.theme.CapriTheme
import com.example.ui.theme.CasinoAcento
import com.example.ui.theme.CasinoBorde
import com.example.ui.theme.CasinoDevolucionTexto
import com.example.ui.theme.CasinoFondo
import com.example.ui.theme.CasinoIngreso
import com.example.ui.theme.CasinoJackpot
import com.example.ui.theme.CasinoPanel
import com.example.ui.theme.CasinoRetiro
import com.example.ui.theme.CasinoTextoPrincipal
import com.example.ui.theme.CasinoTextoSecundario
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CapriTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Foco y teclado automáticos al iniciar la pantalla
    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // Manejo de retroceso con BackHandler si hay subpantalla abierta
    if (state.activeSubScreen != null) {
        BackHandler {
            viewModel.closeSubScreen()
        }
    }

    // Contenedor principal con margenes 14 dp laterales y 12 dp arriba/abajo con insets
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoFondo)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag("main_screen_container")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Barra superior (punto de conexion, fecha/operador, engranaje, menu de tres puntos)
            TopBar(
                operator = state.operator,
                connectionState = state.connectionState,
                onOperatorClick = { viewModel.openOperatorDialog() },
                onSettingsClick = { viewModel.openSettingsDialog() },
                onMenuItemClick = { item -> viewModel.openSubScreen(item) }
            )

            // 2. Jackpot 2x3 SIN scroll
            JackpotGrid(values = state.jackpotValues)

            // 3. Fila INGRESO | RETIRO | "+" (alto 48-52 dp; el "+" alterna a DEVOLUCION | JACKPOT y flecha regreso)
            ModeRow(
                currentMode = state.currentMode,
                isAlternate = state.isAlternateModeRow,
                onSelectMode = { viewModel.selectMode(it) },
                onToggleAlternate = { viewModel.toggleModeRow() }
            )

            // 4. Etiqueta "Nombre" y caja a todo el ancho (52 dp, 18 sp, foco y teclado automaticos)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nombre",
                    color = CasinoTextoSecundario,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp) // Exacto 52 dp
                        .clip(RoundedCornerShape(8.dp))
                        .background(CasinoPanel)
                        .border(1.dp, CasinoBorde, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = state.clientQuery,
                        onValueChange = { viewModel.updateClientQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .testTag("input_nombre_caja"),
                        textStyle = TextStyle(
                            color = CasinoTextoPrincipal,
                            fontSize = 18.sp, // Exacto 18 sp
                            fontWeight = FontWeight.SemiBold
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(CasinoAcento),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (state.searchResults.isNotEmpty()) {
                                    viewModel.selectClient(state.searchResults.first())
                                }
                            }
                        ),
                        decorationBox = { innerTextField ->
                            if (state.clientQuery.isEmpty()) {
                                Text(
                                    text = "Escribe el nombre del cliente...",
                                    color = CasinoTextoSecundario,
                                    fontSize = 18.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // 5. Debajo: Historial del día (caja vacía) o Resultados de búsqueda (con texto)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CasinoPanel)
                    .border(1.dp, CasinoBorde, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                if (state.clientQuery.isBlank()) {
                    // Historial del día (caja vacía)
                    HistorialDelDiaBox(
                        transactions = state.transactions,
                        connectionText = state.connectionStatusText
                    )
                } else {
                    // Resultados de búsqueda (con texto)
                    ResultadosBusquedaBox(
                        query = state.clientQuery,
                        clientListEmpty = state.clientList.isEmpty(),
                        results = state.searchResults,
                        onSelectClient = { client ->
                            viewModel.selectClient(client)
                            viewModel.showToast("Cliente seleccionado: ${client.n}")
                        }
                    )
                }
            }
        }
    }

    // Diálogos modales
    if (state.showOperatorDialog) {
        OperadorDialog(
            currentOperator = state.operator,
            operatorsList = state.serverConfig.pit,
            onSelectOperator = { viewModel.setOperator(it) },
            onDismiss = { viewModel.closeOperatorDialog() }
        )
    }

    if (state.showSettingsDialog) {
        SettingsDialog(
            config = state.appConfig,
            onSave = { host, room, clientName ->
                viewModel.saveConfig(host, room, clientName)
            },
            onDismiss = { viewModel.closeSettingsDialog() }
        )
    }

    // Subpantallas del menú de tres puntos
    state.activeSubScreen?.let { sub ->
        SubScreenModal(
            title = sub,
            onClose = { viewModel.closeSubScreen() }
        )
    }
}

/**
 * Historial del día (caja vacía / transacciones).
 * Regla de Oro 2: Muestra "Sin conexión" cuando está vacío.
 */
@Composable
fun HistorialDelDiaBox(
    transactions: List<com.example.data.model.TransactionItem>,
    connectionText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("historial_dia_box")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Historial del día",
                color = CasinoAcento,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${transactions.size} registros",
                color = CasinoTextoSecundario,
                fontSize = 11.sp
            )
        }

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("historial_vacio_caja"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = "Sin conexión",
                        tint = CasinoTextoSecundario,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sin conexión", // Regla de Oro 2 literal
                        color = CasinoTextoSecundario,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Esperando sincronización con el servidor",
                        color = CasinoTextoSecundario,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(transactions) { tx ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(CasinoFondo)
                            .border(1.dp, CasinoBorde, RoundedCornerShape(4.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tx.tipo,
                            color = if (tx.tipo == "I") CasinoIngreso else CasinoRetiro,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.width(32.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tx.cliente,
                                color = CasinoTextoPrincipal,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${tx.mesa} • ${tx.hora}",
                                color = CasinoTextoSecundario,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "$${tx.monto}",
                            color = CasinoTextoPrincipal,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Resultados de búsqueda en tiempo real (con texto).
 */
@Composable
fun ResultadosBusquedaBox(
    query: String,
    clientListEmpty: Boolean,
    results: List<ClientItem>,
    onSelectClient: (ClientItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("resultados_busqueda_box")
    ) {
        Text(
            text = "Resultados para '$query'",
            color = CasinoAcento,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        if (clientListEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("search_empty_clients_msg"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin lista de clientes", // Regla de Oro 2
                    color = CasinoTextoSecundario,
                    fontSize = 13.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        } else if (results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin coincidencias para '$query'",
                    color = CasinoTextoSecundario,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(results) { client ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(CasinoFondo)
                            .border(1.dp, CasinoBorde, RoundedCornerShape(6.dp))
                            .clickable { onSelectClient(client) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = client.n,
                            color = CasinoTextoPrincipal,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Seleccionar",
                            color = CasinoAcento,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modal para los módulos secundarios del menú de tres puntos.
 */
@Composable
fun SubScreenModal(
    title: String,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        containerColor = CasinoPanel,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = CasinoAcento)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, color = CasinoAcento, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Módulo: $title",
                    color = CasinoTextoPrincipal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Este módulo se sincroniza con el servidor ntfy ('$title'). En esta Fase 1 el diseño y arquitectura están listos; la comunicación con el servidor se conectará al aprobar la fase.",
                    color = CasinoTextoSecundario,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoAcento,
                    contentColor = CasinoDevolucionTexto
                )
            ) {
                Text("Aceptar", fontWeight = FontWeight.Bold)
            }
        }
    )
}
