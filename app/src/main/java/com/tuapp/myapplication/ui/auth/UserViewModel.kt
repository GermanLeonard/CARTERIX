package com.tuapp.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.authModels.request.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.request.RegisterRequestDomain
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.request.ChangePasswordRequestDomain
import com.tuapp.myapplication.data.models.authModels.request.ChangeProfileRequestDomain
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import com.tuapp.myapplication.data.repository.user.UserRepository
import com.tuapp.myapplication.helpers.Resource
import com.tuapp.myapplication.helpers.TokenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    sensitiveInfoRepository: SensitiveInfoRepository,
    private val userRepository: UserRepository
):ViewModel() {

    //CREEN USTEDES LOS ESTADOS QUE SERAN NECESARIOS A MOSTRAR EN LA VISTA
    // (estados de cargando, mensajes de error, etc.)

    //ESTE ES UN EJEMPLO DE PRUEBA
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    //Esta funcion sera usada para traer el nombre y el correo y usarlo en la vista de cambiar perfil,
    //en los campos nombre y correo, para que se puedan modificar y al darle guardar se llama la
    // funcion change profile
    val userCredential: StateFlow<UserDataDomain> = userRepository.getCredentials().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserDataDomain(
            id = 0,
            finanzaId = 0,
            nombre = "",
            correo = "",
        )
    )

    fun checkUser(){
        viewModelScope.launch {
            userRepository.checkUserAndDeleteToken()
        }
    }

    private var _registerLoading = MutableStateFlow(false)
    val registerLoading: StateFlow<Boolean> = _registerLoading

    private var _registerEmailError = MutableStateFlow("")
    val registerEmailError: StateFlow<String> = _registerEmailError

    private var _registerApiError = MutableStateFlow("")
    val registerApiError: StateFlow<String> = _registerApiError

    private var _registerMessage = MutableStateFlow("")
    val registerMessage: StateFlow<String> = _registerMessage

    fun registerUser(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            userRepository.registerUser(RegisterRequestDomain(nombre, correo, contrasena))
                .collect{ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _registerLoading.value = true
                            _registerEmailError.value = ""
                            _registerApiError.value = ""
                        }
                        is Resource.Success -> {
                            _registerMessage.value = resource.data.message
                            _registerLoading.value = false
                        }
                        is Resource.Error -> {
                            _registerLoading.value = false
                            when(resource.httpCode) {
                                409 -> _registerEmailError.value = resource.message
                                else -> _registerApiError.value = resource.message
                            }
                        }
                    }
                }
        }
    }

    private var _loginLoading = MutableStateFlow(false)
    val loginLoading: StateFlow<Boolean> = _loginLoading

    private var _emailError = MutableStateFlow("")
    val emailError: StateFlow<String> = _emailError

    private var _passwordError = MutableStateFlow("")
    val passwordError: StateFlow<String> = _passwordError

    private var _apiError = MutableStateFlow("")
    val apiError: StateFlow<String> = _apiError

    fun loginUser(correo: String, contrasena: String){
        viewModelScope.launch {
            userRepository.loginUser(LoginRequestDomain(correo, contrasena))
                .collect{ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loginLoading.value = true
                            _emailError.value = ""
                            _passwordError.value = ""
                            _apiError.value = ""
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _isLoggedIn.value = resource.data.success
                            _loginLoading.value = false
                        }
                        is Resource.Error -> {
                            _loginLoading.value = false
                            when(resource.httpCode){
                                404 -> {
                                    _emailError.value = resource.message
                                }
                                409 -> {
                                    _passwordError.value = resource.message
                                }
                                else -> {
                                    _apiError.value = resource.message
                                }
                            }
                        }
                    }
                }
        }
    }

    private var _loadingChangeProfile = MutableStateFlow(false)
    val loadingChangeProfile: StateFlow<Boolean> = _loadingChangeProfile

    private var _changeProfileError = MutableStateFlow("")
    val changeProfileError: StateFlow<String> = _changeProfileError

    fun changeProfile(nombre: String, correo: String){
        viewModelScope.launch {
            userRepository.changeProfile(ChangeProfileRequestDomain(nombre, correo))
                .collect{ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingChangeProfile.value = true
                            _changeProfileError.value = ""
                        }
                        is Resource.Success -> {
                            _loadingChangeProfile.value = false
                        }
                        is Resource.Error -> {
                            _changeProfileError.value = resource.message
                            _loadingChangeProfile.value = false
                        }
                    }
                }
        }
    }

    private var _loadingChangePassword = MutableStateFlow(false)
    val loadingChangePassword: StateFlow<Boolean> = _loadingChangePassword

    private var _changePasswordError = MutableStateFlow("")
    val changePasswordError: StateFlow<String> = _changePasswordError

    fun changePassword(contrasenaActual: String, nuevaContrasena: String, confirmarContrasena: String){
        viewModelScope.launch {
            userRepository.changePassword(ChangePasswordRequestDomain(contrasenaActual, nuevaContrasena, confirmarContrasena))
                .collect{ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingChangePassword.value = true
                            _changePasswordError.value = ""
                        }
                        is Resource.Success -> {
                            _loadingChangePassword.value = false
                        }
                        is Resource.Error -> {
                            _changePasswordError.value = resource.message
                            _loadingChangePassword.value = false
                        }
                    }
                }
        }
    }

    fun closeSession() {
        viewModelScope.launch {
            userRepository.closeSession()
            _isLoggedIn.value = false
        }
    }

    private val _token: StateFlow<TokenState> = sensitiveInfoRepository.authenticationToken.map { TokenState.Loaded(it) as TokenState }
        .onStart { emit(TokenState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TokenState.Loading,
        )

    val token: StateFlow<TokenState> = _token

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CarterixApplication
                UserViewModel(
                    application.appProvider.provideSensitiveInfoRepository(),
                    application.appProvider.provideUserRepository()
                )
            }
        }
    }
}