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
    private val sensitiveInfoRepository: SensitiveInfoRepository,
    private val userRepository: UserRepository
):ViewModel() {

    //CREEN USTEDES LOS ESTADOS QUE SERAN NECESARIOS A MOSTRAR EN LA VISTA
    // (estados de cargando, mensajes de error, etc.)

    //ESTE ES UN EJEMPLO DE PRUEBA
    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    //Esta funcion sera usada para traer el nombre y el correo y usarlo en la vista de cambiar perfil,
    //en los campos nombre y correo, para que se puedan modificar y al darle guardar se llama la
    // funcion change profile
    val userCredential: StateFlow<UserDataDomain> = userRepository.getCredentials().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserDataDomain(
            id = 1,
            finanzaId = 1,
            nombre = "",
            correo = "",
        )
    )

    fun registerUser(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            userRepository.registerUser(RegisterRequestDomain(nombre, correo, contrasena))
                .collect({ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data.message
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                })
        }
    }

    fun loginUser(correo: String, contrasena: String){
        viewModelScope.launch {
            userRepository.loginUser(LoginRequestDomain(correo, contrasena))
                .collect({ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _isLoggedIn.value = resource.data.success
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                })
        }
    }

    fun changeProfile(nombre: String, correo: String){
        viewModelScope.launch {
            userRepository.changeProfile(ChangeProfileRequestDomain(nombre, correo))
                .collect({ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data.message
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                })
        }
    }

    fun changePassword(contrasenaActual: String, nuevaContrasena: String, confirmarContrasena: String){
        viewModelScope.launch {
            userRepository.changePassword(ChangePasswordRequestDomain(contrasenaActual, nuevaContrasena, confirmarContrasena))
                .collect({ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data.message
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                })
        }
    }

    fun closeSession() {
        viewModelScope.launch {
            userRepository.closeSession()
        }
    }

    val token: StateFlow<TokenState> = sensitiveInfoRepository.authenticationToken.map { TokenState.Loaded(it) as TokenState }
        .onStart { emit(TokenState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TokenState.Loading,
        )

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