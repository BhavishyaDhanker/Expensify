package com.example.expensify.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensify.User
import com.example.expensify.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {

    private val fbrepo = FirebaseRepository()

    private val _user = MutableStateFlow<User?>(User())
    private val _isSignUpSuccessful = MutableStateFlow<Boolean?>(null)
    private val _errorMsg = MutableStateFlow<String?>(null)

    val user = _user.asStateFlow()
    val isSignUpSuccessful = _isSignUpSuccessful.asStateFlow()
    val errorMsg = _errorMsg.asStateFlow()

    fun signUp(email: String, name: String, pass: String){
        viewModelScope.launch {
            _isSignUpSuccessful.value = null
            _errorMsg.value = null

            _user.value =  User(name = name ,email = email)
            val userModel = User(name = name , email = email)


            val result = fbrepo.signUpAndCreateProfile(userModel, pass)

            result.onSuccess {
                _isSignUpSuccessful.value = true
            }

            result.onFailure { e->
                _isSignUpSuccessful.value = false
                _errorMsg.value = e.message

            }
        }
    }



}