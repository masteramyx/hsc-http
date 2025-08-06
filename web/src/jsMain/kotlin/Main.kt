import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.css.*
import kotlinx.coroutines.launch
import com.shadowconnect.shared.model.SessionState
import com.shadowconnect.shared.model.UserInfo

fun main() {
    renderComposable(rootElementId = "root") {
        App()
    }
}

@Composable
fun App() {
    var sessionState by remember { mutableStateOf(SessionState(isLoading = true)) }
    val coroutineScope = rememberCoroutineScope()

    // Check for existing session on app startup
    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.getCurrentUser()
            if (response.success && response.user != null) {
                sessionState = SessionState(
                    isLoggedIn = true,
                    user = response.user,
                    isLoading = false
                )
            } else {
                sessionState = SessionState(isLoading = false)
            }
        } catch (e: Exception) {
            console.log("No existing session found")
            sessionState = SessionState(isLoading = false)
        }
    }

    Div {
        H1 { Text("HSC Healthcare Platform") }
        
        when {
            sessionState.isLoading -> {
                Div(attrs = {
                    style {
                        textAlign("center")
                        padding(32.px)
                    }
                }) {
                    Text("Loading...")
                }
            }
            
            sessionState.isLoggedIn && sessionState.user != null -> {
                AuthenticatedContent(
                    user = sessionState.user!!,
                    onLogout = {
                        coroutineScope.launch {
                            try {
                                ApiClient.logout()
                                sessionState = SessionState(isLoading = false)
                            } catch (e: Exception) {
                                console.error("Logout failed:", e)
                                // Force logout anyway
                                sessionState = SessionState(isLoading = false)
                            }
                        }
                    }
                )
            }
            
            else -> {
                LoginForm(
                    onLoginSuccess = { user ->
                        sessionState = SessionState(
                            isLoggedIn = true,
                            user = user,
                            isLoading = false
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AuthenticatedContent(user: UserInfo, onLogout: () -> Unit) {
    Div(attrs = {
        style {
            margin(16.px)
            padding(16.px)
            border(1.px, LineStyle.Solid, Color.green)
            borderRadius(8.px)
            backgroundColor(Color("#e8f5e8"))
        }
    }) {
        H2 { Text("Welcome, ${user.email}!") }
        Text("User Type: ${user.userType}")
        Text("User ID: ${user.id}")
        
        Button(attrs = {
            onClick { onLogout() }
            style {
                marginTop(16.px)
                padding(8.px, 16.px)
                backgroundColor(Color("#dc3545"))
                color(Color.white)
                border(0.px)
                borderRadius(4.px)
                cursor("pointer")
            }
        }) {
            Text("Logout")
        }
    }
}