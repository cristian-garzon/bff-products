package com.technicaltest.bffproducts.service

import com.technicaltest.bffproducts.config.exceptions.ProductNotFoundException
import com.technicaltest.bffproducts.model.Product
import com.technicaltest.bffproducts.repository.InMemoryRepository
import com.technicaltest.bffproducts.service.ExternalApiClient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ProductServiceTest {

    private lateinit var productService: ProductService
    private lateinit var productRepository: InMemoryRepository
    private lateinit var externalApiClient: ExternalApiClient


    @BeforeEach
    fun setup() {
        productRepository = mock()
        externalApiClient = mock()
        whenever(externalApiClient.getAllProducts()).thenReturn(Flux.empty())

        productService = ProductService(productRepository, externalApiClient)
    }

    @Test
    fun `test findById returns product when it exists in repository`() {
        val product = Product(1L, "Test Product", "Description", "category", 10.4, 3)
        whenever(productRepository.findById(1L)).thenReturn(product)

        val result = productService.findById(1L)

        StepVerifier.create(result)
            .expectNext(product)
            .verifyComplete()
    }

    @Test
    fun `test findById fetches product from external API when not in repository`() {
        val product = Product(1L, "Test Product", "Description", "category", 10.4, 3)
        whenever(productRepository.findById(1L)).thenReturn(null)
        whenever(externalApiClient.getProductsById(1L)).thenReturn(Mono.just(product))

        val result = productService.findById(1L)

        StepVerifier.create(result)
            .expectNext(product)
            .verifyComplete()

        verify(productRepository).save(product)
    }

    @Test
    fun `test findById throws ProductNotFoundException when product does not exist`() {
        whenever(productRepository.findById(1L)).thenReturn(null)
        whenever(externalApiClient.getProductsById(1L)).thenReturn(Mono.error(ProductNotFoundException("product not found with id 1")))

        val result = productService.findById(1L)

        StepVerifier.create(result)
            .expectError(ProductNotFoundException::class.java)
            .verify()
    }

    @Test
    fun `test save calls external API and saves product in repository`() {
        val product = Product(1L, "Test Product", "Description", "category", 10.4, 3)
        whenever(externalApiClient.saveProduct(any())).thenReturn(Mono.just(product))

        val result = productService.save(product)

        StepVerifier.create(result)
            .expectNext(product)
            .verifyComplete()

        verify(productRepository).save(product)
    }

    @Test
    fun `test delete calls external API and removes product from repository`() {
        val product =  Product(1L, "Test Product", "Description", "category", 10.4, 3)
        whenever(externalApiClient.deleteProduct(1L)).thenReturn(Mono.just(product))
        whenever(productRepository.delete(1L)).thenReturn(true)

        val result = productService.delete(1L)

        StepVerifier.create(result)
            .expectNext(true)
            .verifyComplete()

        verify(productRepository).delete(1L)
    }

    @Test
    fun `test delete throws ProductNotFoundException when product does not exist`() {
        whenever(externalApiClient.deleteProduct(1L)).thenReturn(Mono.error(ProductNotFoundException("product not found with id 1")))

        val result = productService.delete(1L)

        StepVerifier.create(result)
            .expectError(ProductNotFoundException::class.java)
            .verify()
    }

    @Test
    fun `test list returns all products from repository`() {
        val products = listOf(
            Product(1L, "Test Product", "Description", "category", 10.4, 3),
            Product(2L, "Test Product", "Description", "category", 10.4, 3)

        )
        whenever(productRepository.getAll()).thenReturn(products)

        val result = productService.list()

        StepVerifier.create(result)
            .expectNextSequence(products)
            .verifyComplete()
    }

    @Test
    fun `test findByCategory returns products of given category`() {
        val products = listOf(
            Product(1L, "Test Product", "Description", "category", 10.4, 3),
            Product(2L, "Test Product", "Description", "category", 10.4, 3)
        )
        whenever(productRepository.findByCategory("Category")).thenReturn(products)

        val result = productService.findByCategory("Category")

        StepVerifier.create(result)
            .expectNextSequence(products)
            .verifyComplete()
    }
}
