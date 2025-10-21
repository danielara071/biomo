
package com.example.awaq1.data.remote


import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException


class FormulariosRemoteRepository(
    private val api: AuthApiService,
    private val appContext: Context
) {
    suspend fun sendFormularioConImagen(
        formId: Int,
        imageUri: Uri?,
        metadataEntity: Any
    ): Result<FormularioResponse> = try {

        // 🔹 Usa SOLO el metadata limpio (sin id, formId, synced)
        val metadataBody = metadataBodyFromEntitySafe(metadataEntity)

        Log.d("Upload", "== PREPARANDO POST ==")
        Log.d("Upload", "formId = $formId")
        Log.d("Upload", "metadata.clean (final) listo para envío")

        // 🔹 Prepara IMAGEN como archivo (jpg/png)
        val imagePart = imageUri?.let { uri ->
            val resolver = appContext.contentResolver
            val type = resolver.getType(uri) ?: "image/jpeg"
            Log.d("Upload", "imageUri = $uri")
            Log.d("Upload", "image ContentType = $type")

            val bytes = resolver.openInputStream(uri)?.use { it.readBytes() } ?: ByteArray(0)
            Log.d("Upload", "image size = ${bytes.size} bytes")

            val ext = when {
                type.contains("png", ignoreCase = true) -> "png"
                else -> "jpg"
            }
            val body = bytes.toRequestBody(type.toMediaType())
            MultipartBody.Part.createFormData("image", "photo.$ext", body)
        }

        Log.d("Upload", "== ENVIANDO POST ==")

        val resp = api.sendFormularioConImagen(
            formId = formId,
            image = imagePart,
            metadata = metadataBody // ← usa el limpio
        )

        Log.d("Upload", "== RESPUESTA ==")
        Log.d("Upload", "code = ${resp.code()} message = ${resp.message()}")
        Log.d("Upload", "body = ${resp.body()}")

        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("Upload", "errorBody = $err")
        }

        if (resp.isSuccessful && resp.body() != null)
            Result.success(resp.body()!!)
        else
            Result.failure(HttpException(resp))

    } catch (t: Throwable) {
        Log.e("Upload", "EXCEPCIÓN EN POST", t)
        Result.failure(t)
    }
}