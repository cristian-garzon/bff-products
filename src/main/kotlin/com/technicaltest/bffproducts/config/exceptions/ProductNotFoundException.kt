package com.technicaltest.bffproducts.config.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ProductNotFoundException(message: String): ResponseStatusException(HttpStatus.NOT_FOUND, message)