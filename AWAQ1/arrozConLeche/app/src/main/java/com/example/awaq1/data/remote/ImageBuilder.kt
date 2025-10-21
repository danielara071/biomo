package com.example.awaq1.data.remote
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

private val JSON = "application/json; charset=utf-8".toMediaType()
private val PNG  = "image/png".toMediaType()

private val BLOCKED = setOf("id", "synced", "formId")

private class NameExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.name in BLOCKED
    }
    override fun shouldSkipClass(clazz: Class<*>): Boolean = false
}

// Gson que NUNCA serializa esos campos
private val safeGson: Gson = GsonBuilder()
    .addSerializationExclusionStrategy(NameExclusionStrategy())
    .create()

fun metadataBodyFromEntitySafe(entity: Any): RequestBody {
    val json = safeGson.toJson(entity)
    // Log para ver EXACTAMENTE lo que saldrá en el cuerpo
    android.util.Log.d("Upload", "metadata.clean = $json")
    return json.toRequestBody(JSON)
}

/**
 * Always returns a PNG image part, regardless of the original format (JPG/HEIC/etc).
 * This guarantees the backend receives the 'image' part and avoids 500s due to missing files.
 */
fun pngPartFromAnyUri(
    context: Context,
    imageUri: Uri,
    partName: String = "image",
    fileName: String = "photo.png"
): MultipartBody.Part {
    val resolver = context.contentResolver

    // Decode the source into a Bitmap
    val bmp: Bitmap = resolver.openInputStream(imageUri).use { input ->
        requireNotNull(input) { "Unable to open image stream" }
        requireNotNull(BitmapFactory.decodeStream(input)) { "Failed to decode image" }
    }

    // Re-encode as PNG into memory
    val bytes = ByteArrayOutputStream().use { out ->
        require(bmp.compress(Bitmap.CompressFormat.PNG, 100, out)) { "PNG compression failed" }
        out.toByteArray()
    }

    val body = bytes.toRequestBody(PNG)
    return MultipartBody.Part.createFormData(partName, fileName, body)
}