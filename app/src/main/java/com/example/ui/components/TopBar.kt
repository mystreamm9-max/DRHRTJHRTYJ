package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.ui.theme.CasinoFondo
import com.example.ui.theme.CasinoPanel
import com.example.ui.theme.CasinoTextoPrincipal
import com.example.ui.theme.CasinoTextoSecundario
import com.example.ui.theme.StatusConectado
import com.example.ui.theme.StatusDesconectado
import com.example.ui.viewmodel.ConnectionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopBar(
    operator: String,
    connectionState: ConnectionState,
    onOperatorClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMenuItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val todayDate = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Punto de conexión + Fecha/Operador
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(CasinoPanel)
                .border(1.dp, CasinoBorde, RoundedCornerShape(6.dp))
                .clickable { onOperatorClick() }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("operator_date_chip")
        ) {
            // Punto de conexión
            val dotColor = if (connectionState == ConnectionState.CONNECTED) StatusConectado else StatusDesconectado
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dotColor)
                    .testTag("connection_dot")
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Fecha y Operador
            Text(
                text = "$todayDate • $operator",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (operator == "Sin operador") CasinoAcento else CasinoTextoPrincipal
            )
        }

        // Acciones derecha: Engranaje y Menú de tres puntos
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("btn_settings")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = CasinoAcento,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("btn_menu_more")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones",
                        tint = CasinoTextoPrincipal,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier
                        .background(CasinoPanel)
                        .border(1.dp, CasinoBorde)
                ) {
                    val menuItems = listOf(
                        "Historial del día",
                        "Cierre (mesas, fichas, jackpot)",
                        "Aforo (aq / as)",
                        "Score WhatsApp (sc)",
                        "Cajas (cj)",
                        "Fichas no negociables (fn)",
                        "Cartas (ct / fq)",
                        "Cierre del Informe (cc)",
                        "Configuración de Servidor"
                    )

                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item,
                                    color = CasinoTextoPrincipal,
                                    fontSize = 13.sp
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onMenuItemClick(item)
                            }
                        )
                    }
                }
            }
        }
    }
}
