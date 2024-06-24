package com.technicaltest.bffproducts.repository

import com.technicaltest.bffproducts.model.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class InMemoryRepositoryTest {

    private lateinit var inMemoryRepository: InMemoryRepository

    @BeforeEach
    fun setup() {
        inMemoryRepository = InMemoryRepository()
    }

    @Test
    fun `test findById returns product when it exists`() {
        val product = Product(1L, "Test Product", "Description", "category", 10.4, 3)
        inMemoryRepository.save(product)

        val foundProduct = inMemoryRepository.findById(1L)
        assertNotNull(foundProduct)
        assertEquals(product, foundProduct)
    }

    @Test
    fun `test findById returns null when product does not exist`() {
        val foundProduct = inMemoryRepository.findById(1L)
        assertNull(foundProduct)
    }

    @Test
    fun `test save stores the product`() {
        val product = Product(1L, "Test Product", "Description", "category", 10.4, 3)
        inMemoryRepository.save(product)

        val foundProduct = inMemoryRepository.findById(1L)
        assertNotNull(foundProduct)
        assertEquals(product, foundProduct)
    }

    @Test
    fun `test delete removes the product`() {
        val product = Product(1L, "Test Product", "Description", "category", 10.4, 3)
        inMemoryRepository.save(product)

        val deleteResult = inMemoryRepository.delete(1L)
        assertTrue(deleteResult)

        val foundProduct = inMemoryRepository.findById(1L)
        assertNull(foundProduct)
    }

    @Test
    fun `test delete returns false when product does not exist`() {
        val deleteResult = inMemoryRepository.delete(1L)
        assertFalse(deleteResult)
    }

    @Test
    fun `test getAll returns all products`() {
        val product1 = Product(1L, "Test Product", "Description", "category", 10.4, 3)
        val product2 = Product(2L, "Test Product", "Description", "category", 10.4, 3)
        inMemoryRepository.save(product1)
        inMemoryRepository.save(product2)

        val products = inMemoryRepository.getAll()
        assertEquals(2, products.size)
        assertTrue(products.contains(product1))
        assertTrue(products.contains(product2))
    }

    @Test
    fun `test findByCategory returns products of given category`() {
        val product1 = Product(1L, "Test Product", "Description", "TestCategory", 10.4, 3)
        val product2 = Product(2L, "Test Product", "Description", "TestCategory", 10.4, 3)
        val product3 = Product(3L, "Test Product", "Description", "category", 10.4, 3)
        inMemoryRepository.save(product1)
        inMemoryRepository.save(product2)
        inMemoryRepository.save(product3)

        val products = inMemoryRepository.findByCategory("TestCategory")
        assertEquals(2, products.size)
        assertTrue(products.contains(product1))
        assertTrue(products.contains(product2))
    }
}
