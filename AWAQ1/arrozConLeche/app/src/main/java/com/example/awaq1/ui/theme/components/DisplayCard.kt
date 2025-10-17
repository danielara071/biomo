package com.example.awaq1.ui.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.awaq1.data.formularios.FormInfo
import com.example.awaq1.navigator.goEditFormulario

@Composable
fun DisplayCard(
    navController: NavController,
    formInfo: FormInfo,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { formInfo.goEditFormulario(navController) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment =  Alignment.Top
        ){
            //Columna izquierda: titulo y fecha
            Column(
                modifier = Modifier
                    .weight(1f)
                    .width(0.dp) //garantiza que el peso controle el ancho
            ) {
                Text(
                    text = "${formInfo.tipo}: ${formInfo.valorIdentificador}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically){
                    if(formInfo.synced) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32) // Green color
                        )
                    }
                    else if(formInfo.completo && !formInfo.synced){
                        // Completed form: show yellow warning
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = null,
                            tint = Color(0xFFED9121) // Yellow color
                        )
                    }else{
                        // Incomplete form: show red warning
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F) // Red color
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Creado: ${formInfo.fechaCreacion}")
                }
            }
            //Columna derecha
            Column(horizontalAlignment = Alignment.End){
                Text("${formInfo.primerTag}: ${formInfo.primerContenido}" )
                Text("${formInfo.segundoTag}: ${formInfo.segundoContenido}")
            }
        }
    }
}