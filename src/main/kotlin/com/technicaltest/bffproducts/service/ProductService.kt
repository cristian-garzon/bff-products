package com.technicaltest.bffproducts.service

import com.technicaltest.bffproducts.config.exceptions.ProductNotFoundException
import com.technicaltest.bffproducts.model.Product
import com.technicaltest.bffproducts.repository.InMemoryRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
    private val productRepository: InMemoryRepository,
    private val externalApiClient: ExternalApiClient
) {

    init {
        fetchAndSaveProducts()
    }

    fun findById(id: Long): Mono<Product> =
        Mono.defer {
            var product = productRepository.findById(id)
            if (product != null) Mono.just(product)
            else externalApiClient.getProductsById(id)
                .doOnSuccess { productRepository.save(it) }
                .onErrorResume { Mono.error(ProductNotFoundException("product not found with id $id")) }
        }

    fun save(product: Product): Mono<Product> =
        externalApiClient.saveProduct(product)
            .flatMap { product ->
                productRepository.save(product)
                Mono.just(product)
            }

    fun delete(id: Long): Mono<Boolean> =
        externalApiClient.deleteProduct(id)
            .onErrorResume { Mono.error(ProductNotFoundException("product not found with id $id")) }
            .flatMap { product ->
                val result = productRepository.delete(product.id)
                Mono.just(result)
            }

    fun list(): Flux<Product> = Flux.fromIterable(productRepository.getAll())

    fun findByCategory(category: String): Flux<Product> = Flux.fromIterable(productRepository.findByCategory(category))


    fun fetchAndSaveProducts() =
        externalApiClient.getAllProducts()
            .doOnNext { product ->
                productRepository.save(product)
            }
            .doOnError { error ->
                println("Error fetching or saving products: ${error.message}")
            }
            .subscribe()

}
