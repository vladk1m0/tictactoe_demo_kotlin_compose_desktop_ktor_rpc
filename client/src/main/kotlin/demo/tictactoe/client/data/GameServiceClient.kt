package demo.tictactoe.client.data

import demo.tictactoe.api.GameService
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.rpc.krpc.ktor.client.KtorRPCClient
import kotlinx.rpc.krpc.ktor.client.installRPC
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

val ktorClient = HttpClient {
    installRPC {
        waitForServices = true
    }
}

fun newGameServiceClient(host: String = "localhost", port: Int = 8080): GameService {
    val client: KtorRPCClient = runBlocking {
        ktorClient.rpc {
            url {
                it.host = host
                it.port = port
                path("/api/v1")
            }

            rpcConfig {
                serialization {
                    json()
                }
            }
        }
    }
    val serviceClient = client.withService<GameService>()
    return serviceClient
}