package com.example.data.model

/**
 * Representa un cliente del sistema de Cierre de Fichas (Capri GDL).
 * En el HTA: {n: "NOMBRE", k: "CLAVE_FONETICA"}
 */
data class ClientItem(
    val n: String,
    val k: String = ""
)

/**
 * Representa un registro de transacción (Ingreso 'I', Retiro 'R', Devolución 'D', Jackpot 'J').
 */
data class TransactionItem(
    val id: String = "",
    val hora: String = "",
    val tipo: String = "I", // 'I' = Ingreso, 'R' = Retiro, 'D' = Devolución, 'J' = Jackpot
    val monto: Long = 0L,
    val mesa: String = "",
    val cliente: String = "",
    val sup: String = "",
    val ticket: String = "",
    val tkNum: String = ""
)

/**
 * Franja de Jackpot con los 6 valores.
 * Regla de Oro 2: Antes de recibir datos del servidor, cada celda muestra "—" (guion).
 */
data class JackpotValues(
    val jackpot: String = "—",
    val superJackpot: String = "—",
    val full: String = "—",
    val poker: String = "—",
    val escaleraColor: String = "—",
    val escaleraReal: String = "—"
)

/**
 * Configuración de conexión y equipo para ntfy.
 */
data class AppConfig(
    val host: String = "http://192.168.1.72:8080",
    val room: String = "capri-gdl",
    val clientName: String = "cli-movil",
    val operator: String = "Sin operador",
    val monitorista: String = ""
)

/**
 * Información de configuración global enviada por el servidor ('g').
 */
data class ServerConfig(
    val mi: Long = 5000L,
    val mr: Long = 5000L,
    val lim: Long = 20000L,
    val mesas: List<String> = emptyList(),
    val pit: List<String> = emptyList(),
    val monl: List<String> = emptyList(),
    val ign: List<String> = listOf("JL"),
    val mon: String = ""
)

/**
 * Datos para el cierre de informe / aforo.
 */
data class AforoMesa(
    val mesa: String,
    val valor: String = ""
)
