package com.technicaltest.bffproducts

import com.technicaltest.bffproducts.configuration.JwtProperties
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition
class BffProductsApplication

fun main(args: Array<String>) {
	runApplication<BffProductsApplication>(*args)
}
