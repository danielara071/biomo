package com.example.awaq1.data.remote

import com.example.awaq1.data.formularios.FormularioDosEntity
import com.example.awaq1.data.formularios.FormularioUnoEntity

class FormulariosRemoteRepository(private val api: AuthApiService) {
    suspend fun enviarFormularioUno(form: FormularioUnoEntity): Result<Unit> {
        return try {
            val r = api.sendFormularioUno(form)
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${r.code()} ${r.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enviarFormularioDos(form: FormularioDosEntity): Result<Unit> {
        return try {
            val r = api.sendFormularioDos(form)
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${r.code()} ${r.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}