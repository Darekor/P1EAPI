package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    install(CORS){
        anyHost()
    }
    routing {
    }
}
