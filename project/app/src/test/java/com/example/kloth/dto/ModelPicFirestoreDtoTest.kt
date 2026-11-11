package com.example.kloth.dto

import com.example.kloth.data.dto.ModelPicFirestoreDto
import com.example.kloth.data.local.ModelPicType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ModelPicFirestoreDtoTest {
    @Test
    fun mapsBaseModelPicAndUsesDocumentIdAsId() {
        val modelPic = ModelPicFirestoreDto(
            modelPicId = "pic-1",
            name = "Foto cuerpo completo",
            imageUrl = "https://storage.example/base.jpg",
            storagePath = "modelPics/user-1/base.jpg",
            type = "base"
        ).toModelPic(ownerId = "user-1")

        assertEquals("pic-1", modelPic?.id)
        assertEquals("user-1", modelPic?.ownerId)
        assertEquals("Foto cuerpo completo", modelPic?.name)
        assertEquals(ModelPicType.BASE, modelPic?.type)
    }

    @Test
    fun mapsGeneratedModelPicOriginFields() {
        val modelPic = ModelPicFirestoreDto(
            modelPicId = "pic-2",
            name = "Try-on - Vestido azul",
            imageUrl = "https://storage.example/result.jpg",
            storagePath = "tryon-results/user-1/result.jpg",
            type = "generated",
            sourcePublicationId = "article-7",
            baseModelPicId = "pic-1"
        ).toModelPic(ownerId = "user-1")

        assertEquals(ModelPicType.GENERATED, modelPic?.type)
        assertEquals("article-7", modelPic?.sourcePublicationId)
        assertEquals("pic-1", modelPic?.baseModelPicId)
    }

    @Test
    fun ignoresUnknownModelPicType() {
        val modelPic = ModelPicFirestoreDto(type = "unknown")
            .toModelPic(ownerId = "user-1")

        assertNull(modelPic)
    }
}
