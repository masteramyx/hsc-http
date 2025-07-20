import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import com.shadowconnect.shared.auth.LoginRequest

@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Form(attrs = {
        onSubmit { event ->
            event.preventDefault()
            isLoading = true
            errorMessage = null
            
            // TODO: Implement HTTP client call to /login
            val loginRequest = LoginRequest(email, password)
            console.log("Login attempt: ${loginRequest.email}")
        }
    }) {
        Div(attrs = {
            style {
                margin(16.px)
                padding(16.px)
                maxWidth(400.px)
                border(1.px, LineStyle.Solid, Color.lightgray)
                borderRadius(8.px)
            }
        }) {
            H2 { Text("Login") }
            
            errorMessage?.let { error ->
                Div(attrs = {
                    style {
                        color(Color.red)
                        marginBottom(16.px)
                        padding(8.px)
                        backgroundColor(Color("#ffebee"))
                        borderRadius(4.px)
                    }
                }) {
                    Text(error)
                }
            }
            
            Div(attrs = { style { marginBottom(16.px) } }) {
                Label(forId = "email") { Text("Email:") }
                Input(type = InputType.Email) {
                    id("email")
                    value(email)
                    onInput { email = it.value }
                    required()
                    style {
                        width(100.percent)
                        padding(8.px)
                        marginTop(4.px)
                        border(1.px, LineStyle.Solid, Color.lightgray)
                        borderRadius(4.px)
                    }
                }
            }
            
            Div(attrs = { style { marginBottom(16.px) } }) {
                Label(forId = "password") { Text("Password:") }
                Input(type = InputType.Password) {
                    id("password")
                    value(password)
                    onInput { password = it.value }
                    required()
                    style {
                        width(100.percent)
                        padding(8.px)
                        marginTop(4.px)
                        border(1.px, LineStyle.Solid, Color.lightgray)
                        borderRadius(4.px)
                    }
                }
            }
            
            Button(attrs = {
                type(ButtonType.Submit)
                disabled(isLoading || email.isBlank() || password.isBlank())
                style {
                    padding(12.px, 24.px)
                    backgroundColor(Color("#007bff"))
                    color(Color.white)
                    border(0.px)
                    borderRadius(4.px)
                    cursor("pointer")
                    if (isLoading || email.isBlank() || password.isBlank()) {
                        backgroundColor(Color.lightgray)
                        cursor("not-allowed")
                    }
                }
            }) {
                Text(if (isLoading) "Logging in..." else "Login")
            }
        }
    }
}