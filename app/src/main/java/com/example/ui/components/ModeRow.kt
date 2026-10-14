package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CasinoAcento
import com.example.ui.theme.CasinoBorde
import com.example.ui.theme.CasinoDevolucion
import com.example.ui.theme.CasinoDevolucionTexto
import com.example.ui.theme.CasinoIngreso
import com.example.ui.theme.CasinoJackpot
import com.example.ui.theme.CasinoPanel
import com.example.ui.theme.CasinoRetiro

enum class OperationMode {
    INGRESO,
    RETIRO,
    DEVOLUCION,
    JACKPOT
}

/**
 * Fila INGRESO | RETIRO | "+" (alto 48-52 dp; el "+" alterna a DEVOLUCION | JACKPOT y se vuelve flecha de regreso).
 */
@Composable
fun ModeRow(
    currentMode: OperationMode,
    isAlternate: Boolean,
    onSelectMode: (OperationMode) -> Unit,
    onToggleAlternate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp), // Alto entre 48 y 52 dp
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isAlternate) {
            // Botón INGRESO (#1F7A52)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CasinoIngreso)
                    .border(
                        width = if (currentMode == OperationMode.INGRESO) 2.dp else 1.dp,
                        color = if (currentMode == OperationMode.INGRESO) CasinoAcento else CasinoBorde,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelectMode(OperationMode.INGRESO) }
                    .testTag("btn_mode_ingreso"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "INGRESO",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Botón RETIRO (#A63D3D)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CasinoRetiro)
                    .border(
                        width = if (currentMode == OperationMode.RETIRO) 2.dp else 1.dp,
                        color = if (currentMode == OperationMode.RETIRO) CasinoAcento else CasinoBorde,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelectMode(OperationMode.RETIRO) }
                    .testTag("btn_mode_retiro"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RETIRO",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Botón "+" para alternar a DEVOLUCIÓN | JACKPOT
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CasinoPanel)
                    .border(1.dp, CasinoBorde, RoundedCornerShape(8.dp))
                    .clickable { onToggleAlternate() }
                    .testTag("btn_mode_toggle_plus"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = CasinoAcento,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            }
        } else {
            // Botón DEVOLUCION (#E0B84F con texto #2B2208)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CasinoDevolucion)
                    .border(
                        width = if (currentMode == OperationMode.DEVOLUCION) 2.dp else 1.dp,
                        color = if (currentMode == OperationMode.DEVOLUCION) Color.White else CasinoBorde,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelectMode(OperationMode.DEVOLUCION) }
                    .testTag("btn_mode_devolucion"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DEVOLUCIÓN",
                    color = CasinoDevolucionTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Botón JACKPOT (#7B3FA0)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CasinoJackpot)
                    .border(
                        width = if (currentMode == OperationMode.JACKPOT) 2.dp else 1.dp,
                        color = if (currentMode == OperationMode.JACKPOT) CasinoAcento else CasinoBorde,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelectMode(OperationMode.JACKPOT) }
                    .testTag("btn_mode_jackpot"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "JACKPOT",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Flecha de regreso
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CasinoPanel)
                    .border(1.dp, CasinoBorde, RoundedCornerShape(8.dp))
                    .clickable { onToggleAlternate() }
                    .testTag("btn_mode_toggle_back"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar a Ingreso / Retiro",
                    tint = CasinoAcento
                )
            }
        }
    }
}
