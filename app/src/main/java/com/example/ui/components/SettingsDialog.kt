package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppConfig
import com.example.ui.theme.CasinoAcento
import com.example.ui.theme.CasinoBorde
import com.example.ui.theme.CasinoDevolucionTexto
import com.example.ui.theme.CasinoPanel
import com.example.ui.theme.CasinoTextoPrincipal
import com.example.ui.theme.CasinoTextoSecundario

@Composable
fun SettingsDialog(
    config: AppConfig,
    onSave: (host: String, room: String, clientName: String) -> Unit,
    onDismiss: () -> Unit
) {
    var host by remember { mutableStateOf(config.host) }
    var room by remember { mutableStateOf(config.room) }
    var clientName by remember { mutableStateOf(config.clientName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CasinoPanel,
        title = {
            Text(
                text = "Configuración de Servidor ntfy",
                color = CasinoAcento,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Servidor ntfy (propio o ntfy.sh):",
                    color = CasinoTextoSecundario,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = host,
                    onValueChange = { host = it },
                    label = { Text("URL Servidor") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CasinoTextoPrincipal,
                        unfocusedTextColor = CasinoTextoPrincipal,
                        focusedBorderColor = CasinoAcento,
                        unfocusedBorderColor = CasinoBorde,
                        focusedLabelColor = CasinoAcento,
                        unfocusedLabelColor = CasinoTextoSecundario
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_host_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Sala (se limpian caracteres: [^A-Za-z0-9_-]):",
                    color = CasinoTextoSecundario,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text("Nombre de Sala") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CasinoTextoPrincipal,
                        unfocusedTextColor = CasinoTextoPrincipal,
                        focusedBorderColor = CasinoAcento,
                        unfocusedBorderColor = CasinoBorde,
                        focusedLabelColor = CasinoAcento,
                        unfocusedLabelColor = CasinoTextoSecundario
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_room_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Identificador de este dispositivo:",
                    color = CasinoTextoSecundario,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Nombre de cliente") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CasinoTextoPrincipal,
                        unfocusedTextColor = CasinoTextoPrincipal,
                        focusedBorderColor = CasinoAcento,
                        unfocusedBorderColor = CasinoBorde,
                        focusedLabelColor = CasinoAcento,
                        unfocusedLabelColor = CasinoTextoSecundario
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_client_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(host, room, clientName) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoAcento,
                    contentColor = CasinoDevolucionTexto
                ),
                modifier = Modifier.testTag("settings_save_button")
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = CasinoTextoSecundario)
            }
        }
    )
}
