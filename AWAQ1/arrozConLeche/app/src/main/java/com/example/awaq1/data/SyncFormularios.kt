package com.example.awaq1.data

import android.content.Context
import android.net.Uri
import com.example.awaq1.data.formularios.*
import com.example.awaq1.data.formularios.FormulariosRepository
import com.example.awaq1.data.remote.AuthApiService
import com.example.awaq1.data.remote.FormulariosRemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun syncAllPending(
    context: Context,
    api: AuthApiService,
    remoteRepo: FormulariosRemoteRepository,
    repo: FormulariosRepository,
    forms1: List<FormularioUnoEntity>,
    forms2: List<FormularioDosEntity>,
    forms3: List<FormularioTresEntity>,
    forms4: List<FormularioCuatroEntity>,
    forms5: List<FormularioCincoEntity>,
    forms6: List<FormularioSeisEntity>,
    forms7: List<FormularioSieteEntity>,
    imageResolver: (formId: Int, localId: Long) -> Uri?
): SyncResult = withContext(Dispatchers.IO) {

    var ok = 0
    val errs = mutableListOf<String>()

    suspend fun <T : Any> pushEach(
        items: List<T>,
        formId: Int,
        isComplete: (T) -> Boolean,
        getLocalId: (T) -> Long,
        markSynced: suspend (T) -> Unit
    ) {
        for (item in items) {
            if (!isComplete(item)) continue
            try {
                val localId = getLocalId(item)
                val imgUri = imageResolver(formId, localId)   // puede ser null
                val result = remoteRepo.sendFormularioConImagen(
                    formId = formId,
                    imageUri = imgUri,
                    metadataEntity = item
                )
                if (result.isSuccess) {
                    markSynced(item)
                    ok++
                } else {
                    errs += "Fallo Formulario$formId (id=$localId)"
                }
            } catch (e: Exception) {
                errs += "Error Formulario$formId: ${e.message}"
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

    /*
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

     */

    // Cómo marcar synced = true usando tu repositorio
    suspend fun mark1(x: FormularioUnoEntity) { x.synced = true; repo.updateFormularioUno(x) }
    suspend fun mark2(x: FormularioDosEntity) { x.synced = true; repo.updateFormularioDos(x) }
    suspend fun mark3(x: FormularioTresEntity) { x.synced = true; repo.updateFormularioTres(x) }
    suspend fun mark4(x: FormularioCuatroEntity) { x.synced = true; repo.updateFormularioCuatro(x) }
    suspend fun mark5(x: FormularioCincoEntity) { x.synced = true; repo.updateFormularioCinco(x) }
    suspend fun mark6(x: FormularioSeisEntity) { x.synced = true; repo.updateFormularioSeis(x) }
    suspend fun mark7(x: FormularioSieteEntity) { x.synced = true; repo.updateFormularioSiete(x) }

    // Empujar cada tipo solo si existe endpoint (1..7)
    pushEach(
        items = forms1.filter { it.esCompleto() && !it.synced },
        formId = 1,
        isComplete = { (it as FormularioUnoEntity).esCompleto() },
        getLocalId = { (it as FormularioUnoEntity).id },
        markSynced = { x -> (x as FormularioUnoEntity).synced = true; repo.updateFormularioUno(x) }
    )

    pushEach(
        items = forms2.filter { it.esCompleto() && !it.synced },
        formId = 2,
        isComplete = { (it as FormularioDosEntity).esCompleto() },
        getLocalId = { (it as FormularioDosEntity).id },
        markSynced = { x -> (x as FormularioDosEntity).synced = true; repo.updateFormularioDos(x) }
    )

    pushEach(
        items = forms3.filter { it.esCompleto() && !it.synced },
        formId = 3,
        isComplete = { (it as FormularioTresEntity).esCompleto() },
        getLocalId = { (it as FormularioTresEntity).id },
        markSynced = { x -> (x as FormularioTresEntity).synced = true; repo.updateFormularioTres(x) }
    )

    pushEach(
        items = forms4.filter { it.esCompleto() && !it.synced },
        formId = 4,
        isComplete = { (it as FormularioCuatroEntity).esCompleto() },
        getLocalId = { (it as FormularioCuatroEntity).id },
        markSynced = { x -> (x as FormularioCuatroEntity).synced = true; repo.updateFormularioCuatro(x) }
    )

    pushEach(
        items = forms5.filter { it.esCompleto() && !it.synced },
        formId = 5,
        isComplete = { (it as FormularioCincoEntity).esCompleto() },
        getLocalId = { (it as FormularioCincoEntity).id },
        markSynced = { x -> (x as FormularioCincoEntity).synced = true; repo.updateFormularioCinco(x) }
    )

    pushEach(
        items = forms6.filter { it.esCompleto() && !it.synced },
        formId = 6,
        isComplete = { (it as FormularioSeisEntity).esCompleto() },
        getLocalId = { (it as FormularioSeisEntity).id },
        markSynced = { x -> (x as FormularioSeisEntity).synced = true; repo.updateFormularioSeis(x) }
    )

    pushEach(
        items = forms7.filter { it.esCompleto() && !it.synced },
        formId = 7,
        isComplete = { (it as FormularioSieteEntity).esCompleto() },
        getLocalId = { (it as FormularioSieteEntity).id },
        markSynced = { x -> (x as FormularioSieteEntity).synced = true; repo.updateFormularioSiete(x) }
    )

    SyncResult(ok, errs)
}

data class SyncResult(val successCount: Int, val errors: List<String>)