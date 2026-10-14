package com.example

import com.example.data.model.ClientItem
import com.example.data.protocol.ClientSearch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClientSearchTest {

    private val clients = listOf(
        ClientItem("JUAN PEREZ", ClientSearch.nkey("JUAN PEREZ")),
        ClientItem("LOPEZ GARCIA MARIA", ClientSearch.nkey("LOPEZ GARCIA MARIA")),
        ClientItem("HERNANDEZ GARCIA CARLOS", ClientSearch.nkey("HERNANDEZ GARCIA CARLOS")),
        ClientItem("RODRIGUEZ ANA", ClientSearch.nkey("RODRIGUEZ ANA")),
        ClientItem("GONZALEZ JOSE", ClientSearch.nkey("GONZALEZ JOSE"))
    )

    @Test
    fun `garcia encuentra nombres con Garcia como segundo apellido`() {
        val results = ClientSearch.search(clients, "garcia").map { it.n }
        // Debe encontrar LOPEZ GARCIA MARIA y HERNANDEZ GARCIA CARLOS
        assertTrue("Debe contener 'LOPEZ GARCIA MARIA'", results.contains("LOPEZ GARCIA MARIA"))
        assertTrue("Debe contener 'HERNANDEZ GARCIA CARLOS'", results.contains("HERNANDEZ GARCIA CARLOS"))
    }

    @Test
    fun `jaun encuentra Juan por transposicion en lev`() {
        val results = ClientSearch.search(clients, "jaun").map { it.n }
        // 'jaun' debe encontrar 'JUAN PEREZ'
        assertTrue("Debe encontrar 'JUAN PEREZ'", results.contains("JUAN PEREZ"))
        assertEquals("JUAN PEREZ", results.first())
    }

    @Test
    fun `mar lop encuentra Lopez Garcia Maria`() {
        val results = ClientSearch.search(clients, "mar lop").map { it.n }
        // 'mar lop' debe encontrar 'LOPEZ GARCIA MARIA' (coincidencia de múltiples palabras en cualquier orden)
        assertTrue("Debe encontrar 'LOPEZ GARCIA MARIA'", results.contains("LOPEZ GARCIA MARIA"))
        assertEquals("LOPEZ GARCIA MARIA", results.first())
    }
}
