package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CasinoAcento
import com.example.ui.theme.CasinoBorde
import com.example.ui.theme.CasinoDevolucionTexto
import com.example.ui.theme.CasinoPanel
import com.example.ui.theme.CasinoTextoPrincipal
import com.example.ui.theme.CasinoTextoSecundario

@Composable
fun OperadorDialog(
    currentOperator: String,
    operatorsList: List<String>,
    onSelectOperator: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var manualName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CasinoPanel,
        title = {
            Text(
                text = "Seleccionar Operador",
                color = CasinoAcento,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Operador actual: $currentOperator",
                    color = CasinoTextoSecundario,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (operatorsList.isNotEmpty()) {
                    Text(
                        text = "Operadores disponibles:",
                        color = CasinoTextoPrincipal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        items(operatorsList) { op ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CasinoPanel)
                                    .border(1.dp, if (op == currentOperator) CasinoAcento else CasinoBorde, RoundedCornerShape(8.dp))
                                    .clickable { onSelectOperator(op) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = op,
                                    color = if (op == currentOperator) CasinoAcento else CasinoTextoPrincipal,
                                    fontWeight = if (op == currentOperator) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Sin operadores recibidos del servidor ('g'). Ingresa el nombre:",
                        color = CasinoTextoSecundario,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = manualName,
                    onValueChange = { manualName = it },
                    label = { Text("Nombre de operador") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CasinoTextoPrincipal,
                        unfocusedTextColor = CasinoTextoPrincipal,
                        focusedBorderColor = CasinoAcento,
                        unfocusedBorderColor = CasinoBorde,
                        focusedLabelColor = CasinoAcento,
                        unfocusedLabelColor = CasinoTextoSecundario,
                        cursorColor = CasinoAcento
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("operador_manual_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (manualName.isNotBlank()) {
                        onSelectOperator(manualName.trim())
                    }
                },
                enabled = manualName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoAcento,
                    contentColor = CasinoDevolucionTexto
                ),
                modifier = Modifier.testTag("operador_confirm_button")
            ) {
                Text("Usar este operador", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onSelectOperator("Sin operador") }
            ) {
                Text("Limpiar (Sin operador)", color = CasinoTextoSecundario)
            }
        }
    )
}
