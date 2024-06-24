package com.technicaltest.bffproducts.model

import com.fasterxml.jackson.annotation.JsonProperty

class Product (
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val stock: Int,
)

data class ProductsResponse (
    val products: List<Product>,
)