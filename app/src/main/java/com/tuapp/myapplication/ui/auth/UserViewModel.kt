package com.tuapp.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.authModels.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.RegisterRequestDomain
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangePasswordRequestDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangeProfileRequestDomain
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import com.tuapp.myapplication.data.repository.user.UserRepository
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val sensitiveInfoRepository: SensitiveInfoRepository,
    private val userRepository: UserRepository
):ViewModel() {
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
                        is Resourse.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resourse.Success -> {
                            //Manejen el "success"
                            resource.data.message
                        }
                        is Resourse.Error -> {
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
                        is Resourse.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resourse.Success -> {
                            //Manejen el "success"
                            resource.data.message
                        }
                        is Resourse.Error -> {
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
                        is Resourse.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resourse.Success -> {
                            //Manejen el "success"
                            resource.data.message
                        }
                        is Resourse.Error -> {
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
                        is Resourse.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resourse.Success -> {
                            //Manejen el "success"
                            resource.data.message
                        }
                        is Resourse.Error -> {
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

    val token: StateFlow<String?> = sensitiveInfoRepository.authenticationToken.map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
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