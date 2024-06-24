package com.technicaltest.bffproducts.controller

import com.technicaltest.bffproducts.model.Product
import com.technicaltest.bffproducts.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Mono<ResponseEntity<Product>> =
        productService.findById(id)
            .map { product -> ResponseEntity.ok(product) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @GetMapping
    fun getAllProducts(): ResponseEntity<Flux<Product>> = ResponseEntity.ok(productService.list())


    @PostMapping
    fun createProduct(@RequestBody product: Product): Mono<ResponseEntity<Product>> =
        productService.save(product)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): Mono<ResponseEntity<Void>> =
        productService.delete(id)
            .map { deleted ->
                if (deleted) ResponseEntity.noContent().build()
                else ResponseEntity.notFound().build()
            }

    @GetMapping("category/{category}")
    fun findAllByCategory(@PathVariable category: String): ResponseEntity<Flux<Product>> =
        ResponseEntity.ok(productService.findByCategory(category))
}