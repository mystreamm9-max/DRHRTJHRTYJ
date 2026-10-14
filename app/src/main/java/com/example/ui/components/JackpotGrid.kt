package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JackpotValues
import com.example.ui.theme.CasinoAcento
import com.example.ui.theme.CasinoBorde
import com.example.ui.theme.CasinoPanel
import com.example.ui.theme.CasinoTextoPrincipal
import com.example.ui.theme.CasinoTextoSecundario

/**
 * Grilla de Jackpot 2x3 fija SIN scroll.
 * Paneles y celdas en #16352D, bordes en #2A5246.
 */
@Composable
fun JackpotGrid(
    values: JackpotValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CasinoPanel)
            .border(1.dp, CasinoBorde, RoundedCornerShape(8.dp))
            .padding(6.dp)
            .testTag("jackpot_grid_2x3"),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Fila 1: JACKPOT | SUPER JACKPOT
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            JackpotCell(
                title = "JACKPOT",
                value = values.jackpot,
                isMajor = true,
                modifier = Modifier.weight(1f),
                tag = "jp_cell_jackpot"
            )
            JackpotCell(
                title = "SUPER JACKPOT",
                value = values.superJackpot,
                isMajor = true,
                modifier = Modifier.weight(1f),
                tag = "jp_cell_super"
            )
        }

        // Fila 2: FULL | POKER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            JackpotCell(
                title = "FULL",
                value = values.full,
                isMajor = false,
                modifier = Modifier.weight(1f),
                tag = "jp_cell_full"
            )
            JackpotCell(
                title = "POKER",
                value = values.poker,
                isMajor = false,
                modifier = Modifier.weight(1f),
                tag = "jp_cell_poker"
            )
        }

        // Fila 3: ESC. COLOR | ESC. REAL
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            JackpotCell(
                title = "ESC. COLOR",
                value = values.escaleraColor,
                isMajor = false,
                modifier = Modifier.weight(1f),
                tag = "jp_cell_esccolor"
            )
            JackpotCell(
                title = "ESC. REAL",
                value = values.escaleraReal,
                isMajor = false,
                modifier = Modifier.weight(1f),
                tag = "jp_cell_escreal"
            )
        }
    }
}

@Composable
private fun JackpotCell(
    title: String,
    value: String,
    isMajor: Boolean,
    tag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag(tag)
            .clip(RoundedCornerShape(4.dp))
            .background(CasinoPanel)
            .border(1.dp, CasinoBorde, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isMajor) CasinoAcento else CasinoTextoSecundario,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value, // Regla de Oro 2: "—" (guion) cuando no hay datos
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CasinoTextoPrincipal,
                textAlign = TextAlign.End
            )
        }
    }
}
