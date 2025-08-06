import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.shadowconnect.shared.model.LoginRequest
import com.shadowconnect.shared.model.LoginResponse

object ApiClient {
    private val httpClient = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        
        // Enable automatic cookie handling for session management
        install(HttpCookies)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return httpClient.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
    }

    suspend fun logout(): LoginResponse {
        return httpClient.post("/logout") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getCurrentUser(): LoginResponse {
        return httpClient.get("/me") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}