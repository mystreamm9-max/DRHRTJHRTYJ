package com.example

import com.example.ui.viewmodel.ConnectionState
import com.example.ui.viewmodel.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias para validar la Regla de Oro 2 (CERO DATOS INVENTADOS).
 */
class AppStateDefaultTest {

    @Test
    fun `default state complies strictly with golden rule 2`() {
        val viewModel = MainViewModel()
        val state = viewModel.uiState.value

        // 1. Operador: "Sin operador"
        assertEquals("Sin operador", state.operator)

        // 2. Jackpot: "—" (guion) en cada una de las 6 celdas
        assertEquals("—", state.jackpotValues.jackpot)
        assertEquals("—", state.jackpotValues.superJackpot)
        assertEquals("—", state.jackpotValues.full)
        assertEquals("—", state.jackpotValues.poker)
        assertEquals("—", state.jackpotValues.escaleraColor)
        assertEquals("—", state.jackpotValues.escaleraReal)

        // 3. Lista de clientes vacía al inicio (sin datos inventados)
        assertTrue(state.clientList.isEmpty())

        // 4. Historial vacío con "Sin conexión"
        assertTrue(state.transactions.isEmpty())
        assertEquals(ConnectionState.DISCONNECTED, state.connectionState)
        assertEquals("Sin conexión", state.connectionStatusText)

        // 5. Configuración del servidor vacía hasta recibir 'g'
        assertTrue(state.serverConfig.mesas.isEmpty())
        assertTrue(state.serverConfig.pit.isEmpty())
    }
}
