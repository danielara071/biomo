package com.example.awaq1.data.remote

import com.example.awaq1.data.formularios.FormularioCincoEntity
import com.example.awaq1.data.formularios.FormularioCuatroEntity
import com.example.awaq1.data.formularios.FormularioDosEntity
import com.example.awaq1.data.formularios.FormularioSeisEntity
import com.example.awaq1.data.formularios.FormularioSieteEntity
import com.example.awaq1.data.formularios.FormularioTresEntity
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

    suspend fun enviarFormularioTres(form: FormularioTresEntity): Result<Unit> {
        return try {
            val r = api.sendFormularioTres(form)
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${r.code()} ${r.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enviarFormularioCuatro(form: FormularioCuatroEntity): Result<Unit> {
        return try {
            val r = api.sendFormularioCuatro(form)
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${r.code()} ${r.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enviarFormularioCinco(form: FormularioCincoEntity): Result<Unit> {
        return try {
            val r = api.sendFormularioCinco(form)
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${r.code()} ${r.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enviarFormularioSeis(form: FormularioSeisEntity): Result<Unit> {
        return try {
            val r = api.sendFormularioSeis(form)
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${r.code()} ${r.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enviarFormularioSiete(form: FormularioSieteEntity): Result<Unit> {
        return try {
            val r = api.sendFormularioSiete(form)
            if (r.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${r.code()} ${r.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}