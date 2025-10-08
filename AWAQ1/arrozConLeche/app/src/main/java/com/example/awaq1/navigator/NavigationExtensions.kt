package com.example.awaq1.navigator

import androidx.navigation.NavController
import com.example.awaq1.data.formularios.FormInfo

//Funcion de extension sobre FormInfo
fun FormInfo.goEditFormulario(navController: NavController) {
    when (formulario) {
        "form1" -> navController.navigate(route = FormUnoID(formId))
        "form2" -> navController.navigate(route = FormDosID(formId))
        "form3" -> navController.navigate(route = FormTresID(formId))
        "form4" -> navController.navigate(route = FormCuatroID(formId))
        "form5" -> navController.navigate(route = FormCincoID(formId))
        "form6" -> navController.navigate(route = FormSeisID(formId))
        "form7" -> navController.navigate(route = FormSieteID(formId))
        else -> throw IllegalArgumentException("CARD NAVIGATION NOT IMPLEMENTED FOR $formulario")
    }
}