package demo.tictactoe.server

import demo.tictactoe.api.GameService
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.rpc.krpc.ktor.server.RPC
import kotlinx.rpc.krpc.ktor.server.rpc
import kotlinx.rpc.krpc.serialization.json.json

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(RPC)

    routing {
        rpc("/api/v1") {
            rpcConfig {
                serialization {
                    json()
                }
            }

            registerService<GameService> { ctx -> GameServiceImpl(ctx) }
        }
    }
}

