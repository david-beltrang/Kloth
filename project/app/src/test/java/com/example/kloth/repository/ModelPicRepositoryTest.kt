package com.example.kloth.repository

import android.net.Uri
import com.example.kloth.data.datasource.services.ModelPicRemoteDataSource
import com.example.kloth.data.dto.ModelPicFirestoreDto
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.data.repository.ModelPicRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.data.repository.UploadedStorageImage
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ModelPicRepositoryTest {
    private val dataSource = mockk<ModelPicRemoteDataSource>()
    private val storageRepository = mockk<StorageRepository>()
    private val repository = ModelPicRepository(dataSource, storageRepository)

    @Test
    fun getModelPics_mapsFirestoreDtosToUiEntities() = runTest {
        coEvery { dataSource.getModelPics("user-1") } returns listOf(
            ModelPicFirestoreDto(
                modelPicId = "pic-1",
                name = "Foto base",
                imageUrl = "https://storage.example/base.jpg",
                storagePath = "modelPics/user-1/base.jpg",
                type = "base"
            )
        )

        val result = repository.getModelPics("user-1")

        assertThat(result.isSuccess).isTrue()
        val modelPic = result.getOrNull().orEmpty().single()
        assertThat(modelPic.id).isEqualTo("pic-1")
        assertThat(modelPic.ownerId).isEqualTo("user-1")
        assertThat(modelPic.type).isEqualTo(ModelPicType.BASE)
    }

    @Test
    fun getModelPics_returnsFailureWhenDataSourceThrows() = runTest {
        coEvery { dataSource.getModelPics("") } throws
            IllegalArgumentException("El ID del usuario es obligatorio")

        val result = repository.getModelPics("")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull())
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun uploadBaseModelPic_uploadsImageAndPersistsMetadata() = runTest {
        val uri = mockk<Uri>()
        coEvery {
            storageRepository.uploadModelPicImage("user-1", uri, "image/jpeg")
        } returns Result.success(
            UploadedStorageImage(
                imageUrl = "https://storage.example/base.jpg",
                storagePath = "modelPics/user-1/random-id"
            )
        )
        coEvery {
            dataSource.createBaseModelPic(
                "user-1",
                "Foto base",
                "https://storage.example/base.jpg",
                "modelPics/user-1/random-id"
            )
        } returns "pic-1"

        val result = repository.uploadBaseModelPic("user-1", "Foto base", uri, "image/jpeg")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("pic-1")
    }

    @Test
    fun uploadBaseModelPicDeletesStorageImageWhenFirestoreWriteFails() = runTest {
        val uri = mockk<Uri>()
        val uploadedImage = UploadedStorageImage(
            imageUrl = "https://storage.example/base.jpg",
            storagePath = "modelPics/user-1/random-id"
        )
        coEvery {
            storageRepository.uploadModelPicImage("user-1", uri, "image/jpeg")
        } returns Result.success(uploadedImage)
        coEvery {
            dataSource.createBaseModelPic("user-1", "Foto base", uploadedImage.imageUrl, uploadedImage.storagePath)
        } throws IllegalStateException("Firestore no disponible")
        coEvery { storageRepository.deleteImage(uploadedImage.imageUrl) } returns Result.success(Unit)

        val result = repository.uploadBaseModelPic("user-1", "Foto base", uri, "image/jpeg")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Firestore no disponible")
        coVerify(exactly = 1) { storageRepository.deleteImage(uploadedImage.imageUrl) }
    }
}
