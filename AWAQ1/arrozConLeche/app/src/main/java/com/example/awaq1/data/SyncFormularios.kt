package com.example.awaq1.data

import com.example.awaq1.data.formularios.*
import com.example.awaq1.data.formularios.FormulariosRepository
import com.example.awaq1.data.remote.AuthApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun syncAllPending(
    api: AuthApiService,
    repo: FormulariosRepository,
    forms1: List<FormularioUnoEntity>,
    forms2: List<FormularioDosEntity>,
    forms3: List<FormularioTresEntity>,
    forms4: List<FormularioCuatroEntity>,
    forms5: List<FormularioCincoEntity>,
    forms6: List<FormularioSeisEntity>,
    forms7: List<FormularioSieteEntity>,
): SyncResult = withContext(Dispatchers.IO) {

    var ok = 0
    val errs = mutableListOf<String>()

    suspend fun <T> pushEach(
        items: List<T>,
        send: suspend (T) -> Boolean,
        markSynced: suspend (T) -> Unit,
        typeName: String
    ) {
        for (item in items) {
            try {
                val success = send(item)
                if (success) {
                    markSynced(item)
                    ok++
                } else {
                    errs += "Fallo $typeName"
                }
            } catch (e: Exception) {
                errs += "Error $typeName: ${e.message}"
            }
        }
    }

    // Solo COMPLETOS y NO sincronizados
    val p1 = forms1.filter { it.esCompleto() && !it.synced }
    val p2 = forms2.filter { it.esCompleto() && !it.synced }
    val p3 = forms3.filter { it.esCompleto() && !it.synced }
    val p4 = forms4.filter { it.esCompleto() && !it.synced }
    val p5 = forms5.filter { it.esCompleto() && !it.synced }
    val p6 = forms6.filter { it.esCompleto() && !it.synced }
    val p7 = forms7.filter { it.esCompleto() && !it.synced }

    // Un "send" por tipo, mapeado a tu AuthApiService
    // (devuelve true si Response.isSuccessful)
    suspend fun send1(x: FormularioUnoEntity) =
        api.sendFormularioUno(x).isSuccessful
    suspend fun send2(x: FormularioDosEntity) =
        api.sendFormularioDos(x).isSuccessful
    suspend fun send3(x: FormularioTresEntity) =
        api.sendFormularioTres(x).isSuccessful
    suspend fun send4(x: FormularioCuatroEntity) =
        api.sendFormularioCuatro(x).isSuccessful
    suspend fun send5(x: FormularioCincoEntity) =
        api.sendFormularioCinco(x).isSuccessful
    suspend fun send6(x: FormularioSeisEntity) =
        api.sendFormularioSeis(x).isSuccessful
    suspend fun send7(x: FormularioSieteEntity) =
        api.sendFormularioSiete(x).isSuccessful

    // Cómo marcar synced = true usando tu repositorio
    suspend fun mark1(x: FormularioUnoEntity) { x.synced = true; repo.updateFormularioUno(x) }
    suspend fun mark2(x: FormularioDosEntity) { x.synced = true; repo.updateFormularioDos(x) }
    suspend fun mark3(x: FormularioTresEntity) { x.synced = true; repo.updateFormularioTres(x) }
    suspend fun mark4(x: FormularioCuatroEntity) { x.synced = true; repo.updateFormularioCuatro(x) }
    suspend fun mark5(x: FormularioCincoEntity) { x.synced = true; repo.updateFormularioCinco(x) }
    suspend fun mark6(x: FormularioSeisEntity) { x.synced = true; repo.updateFormularioSeis(x) }
    suspend fun mark7(x: FormularioSieteEntity) { x.synced = true; repo.updateFormularioSiete(x) }

    // Empujar cada tipo solo si existe endpoint (1..7)
    pushEach(p1, ::send1, ::mark1, "Formulario1")
    pushEach(p2, ::send2, ::mark2, "Formulario2")
    pushEach(p3, ::send3, ::mark3, "Formulario3")
    pushEach(p4, ::send4, ::mark4, "Formulario4")
    pushEach(p5, ::send5, ::mark5, "Formulario5")
    pushEach(p6, ::send6, ::mark6, "Formulario6")
    pushEach(p7, ::send7, ::mark7, "Formulario7")

    SyncResult(ok, errs)
}

data class SyncResult(val successCount: Int, val errors: List<String>)
//