package com.technicaltest.bffproducts.service

import com.technicaltest.bffproducts.model.Product
import com.technicaltest.bffproducts.model.ProductsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ExternalApiClient (
    @Value("\${app.external-api-url}")
    private val externalApiUrl: String
) {
    private val webClient: WebClient = WebClient.create(externalApiUrl)

    fun getAllProducts(): Flux<Product> = webClient.get()
        .retrieve()
        .bodyToMono<ProductsResponse>()
        .flatMapMany { response -> Flux.fromIterable(response.products) }

    fun getProductsById(id: Long): Mono<Product> =
        webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .bodyToMono(Product::class.java)

    fun saveProduct(product: Product): Mono<Product> =
        webClient.post()
            .uri("/add")
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product::class.java)


    fun deleteProduct(id: Long): Mono<Product> =
        webClient.delete()
            .uri("/{id}", id)
            .retrieve()
            .bodyToMono(Product::class.java)
}