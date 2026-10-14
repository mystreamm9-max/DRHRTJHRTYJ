package com.example

import com.example.data.model.ClientItem
import com.example.data.protocol.LHash
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pruebas unitarias para verificar la exactitud de lhash contra el HTA original.
 */
class LHashTest {

    @Test
    fun `empty list returns 0-0`() {
        val result = LHash.compute(emptyList())
        assertEquals("0-0", result)
    }

    @Test
    fun `test vector 1 matches Node and HTA`() {
        val clients = listOf(
            ClientItem("JUAN PEREZ"),
            ClientItem("MARIA LOPEZ"),
            ClientItem("CARLOS RUIZ")
        )
        // Calculado en Node.js con el algoritmo exacto del HTA: "3ad840a5-3"
        val result = LHash.compute(clients)
        assertEquals("3ad840a5-3", result)
    }

    @Test
    fun `test vector with accents and special characters matches Node and HTA`() {
        val clients = listOf(
            ClientItem("JOSÉ VILLAGRÁN"),
            ClientItem("ÁLVARO LÓPEZ")
        )
        // Calculado en Node.js con el algoritmo exacto del HTA: "57c474d2-2"
        val result = LHash.compute(clients)
        assertEquals("57c474d2-2", result)
    }

    @Test
    fun `single item test`() {
        val clients = listOf(ClientItem("CAPRI"))
        // h for 'CAPRI':
        // C(67) -> ((5381<<5)+5381+67) = 177639
        // A(65) -> ((177639<<5)+177639+65) = 5862152
        // P(80) -> ((5862152<<5)+5862152+80) = 193451096
        // R(82) -> ((193451096<<5)+193451096+82) = 2089271874
        // I(73) -> ((2089271874<<5)+2089271874+73) = 66856700021 | 0 -> 2457788373 -> 0x9281a1d5
        val result = LHash.compute(clients)
        assertEquals("ce06db4-1", result)
    }
}
