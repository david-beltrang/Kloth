package com.example.kloth.data.datasource.services

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRemoteDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadImage(path: String, uri: Uri, contentType: String? = null): String {
        val imageRef = storage.reference.child(path)
        val metadata = contentType?.let {
            StorageMetadata.Builder().setContentType(it).build()
        }
        if (metadata == null) imageRef.putFile(uri).await()
        else imageRef.putFile(uri, metadata).await()
        return imageRef.downloadUrl.await().toString()
    }

    suspend fun deleteImage(url: String) {
        storage.getReferenceFromUrl(url).delete().await()
    }
}
