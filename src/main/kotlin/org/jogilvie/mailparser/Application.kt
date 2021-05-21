package org.jogilvie.mailparser

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    routing {
        get("/health") {
            call.respondText("It's alive!")
        }
        post("/parse") {
            val payload = call.receive<String>()
            call.respond(EmailHeaders.fromString(payload))
        }
    }
}

