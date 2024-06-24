package com.technicaltest.bffproducts.repository

import com.technicaltest.bffproducts.model.Product
import org.springframework.stereotype.Repository

@Repository
class InMemoryRepository {

    private val products = mutableMapOf<Long, Product>()

    fun findById(id: Long): Product? = products[id]

    fun save(product: Product) {
        products[product.id] = product
    }

    fun delete(id: Long): Boolean = products.remove(id) != null


    fun getAll(): List<Product> = products.map { it.value }


    fun findByCategory(category: String): List<Product> = products.filter { it.value.category == category }.map { it.value }

}