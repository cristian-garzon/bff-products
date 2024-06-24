package com.technicaltest.bffproducts.controller

import com.technicaltest.bffproducts.model.Product
import com.technicaltest.bffproducts.service.ProductService
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProductControllerTest {

    @Mock
    private lateinit var productService: ProductService

    @InjectMocks
    private lateinit var productController: ProductController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun `testFindByIdSuccess`() {
        val productId = 1L
        val mockProduct = Product(productId, "Test Product", "Description", "category", 10.4, 3)

        given(productService.findById(productId)).willReturn(Mono.just(mockProduct))

        val responseMono = productController.findById(productId)

        val response = responseMono.block()

        assertEquals(HttpStatus.OK, response?.statusCode)
        assertEquals(mockProduct, response?.body)
    }

    @Test
    fun `testFindByIdNotFound`() {
        val productId = 1L

        given(productService.findById(productId)).willReturn(Mono.empty())

        val responseMono = productController.findById(productId)

        val response = responseMono.block()

        assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }

    @Test
    fun `testGetAllProducts`() {
        val mockProducts = Flux.just(
            Product(1L, "Test Product", "Description", "category", 10.4, 3),
            Product(2L, "Test Product", "Description", "category", 10.4, 3)
        )

        given(productService.list()).willReturn(mockProducts)

        val responseMono = productController.getAllProducts()

        val response = responseMono.body

        assertTrue { response?.collectList()?.block()?.equals(mockProducts.collectList().block()) ?: false }
    }

    @Test
    fun `testCreateProductSuccess`() {
        val newProduct =  Product(1L, "Test Product", "Description", "category", 10.4, 3)

        given(productService.save(newProduct)).willReturn(Mono.just(newProduct))

        val responseMono = productController.createProduct(newProduct)

        val response = responseMono.block()

        assertEquals(HttpStatus.OK, response?.statusCode)
        assertEquals(newProduct, response?.body)
    }

    @Test
    fun `testDeleteByIdSuccess`() {
        val productId = 1L

        given(productService.delete(productId)).willReturn(Mono.just(true))

        val responseMono = productController.deleteById(productId)

        val response = responseMono.block()

        assertEquals(HttpStatus.NO_CONTENT, response?.statusCode)
    }

    @Test
    fun `testDeleteByIdNotFound`() {
        val productId = 1L

        given(productService.delete(productId)).willReturn(Mono.just(false))

        val responseMono = productController.deleteById(productId)

        val response = responseMono.block()

        assertEquals(HttpStatus.NOT_FOUND, response?.statusCode)
    }
}
