package com.example.kloth.repository

import com.example.kloth.data.datasource.services.TryOnRemoteDataSource
import com.example.kloth.data.dto.TryOnResultDto
import com.example.kloth.data.repository.TryOnRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TryOnRepositoryTest {
    private val dataSource = mockk<TryOnRemoteDataSource>()
    private val repository = TryOnRepository(dataSource)

    @Test
    fun generateTryOnMapsCallableDtoToResult() = runTest {
        coEvery { dataSource.generateTryOn("article-1", "base-1") } returns
            TryOnResultDto(
                modelPicId = "generated-1",
                imageUrl = "https://storage.example/generated-1.jpg"
            )

        val result = repository.generateTryOn("article-1", "base-1")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.modelPicId).isEqualTo("generated-1")
        assertThat(result.getOrNull()?.imageUrl)
            .isEqualTo("https://storage.example/generated-1.jpg")
    }

    @Test
    fun generateTryOnReturnsFailureWhenCallableFails() = runTest {
        coEvery { dataSource.generateTryOn("article-1", "base-1") } throws
            IllegalStateException("FASHN no pudo generar la imagen")

        val result = repository.generateTryOn("article-1", "base-1")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message)
            .isEqualTo("FASHN no pudo generar la imagen")
    }
}
