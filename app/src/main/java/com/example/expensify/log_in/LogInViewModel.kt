package com.example.expensify.log_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensify.FirebaseRepository
import com.example.expensify.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogInViewModel: ViewModel() {

    private val fbrepo = FirebaseRepository()

    private val _user = MutableStateFlow<User?>(User())
    private val _errorMsg = MutableStateFlow<String?>(null)
    private val _isLogInSuccess = MutableStateFlow<Boolean?>(null)
    private val _passError = MutableStateFlow<String?>(null)
    private val _isForgotPassSuccess = MutableStateFlow<Boolean?>(null)

    val user = _user.asStateFlow()
    val errorMsg = _errorMsg.asStateFlow()
    val isLogInSuccess = _isLogInSuccess.asStateFlow()
    val passError = _passError.asStateFlow()
    val isForgotPassSuccess = _isForgotPassSuccess.asStateFlow()


    fun loginUser(email: String, pass: String){
        viewModelScope.launch {
            _errorMsg.value = null              // because if a login attempt fails then the old data would
            _isLogInSuccess.value = null        // still be lingering in these states hence we need to reset them before any new attempt

            val result = fbrepo.logIn(email, pass)

            result.onSuccess { userP ->
                if (userP != null) {
                    _user.value = userP
                    _isLogInSuccess.value = true
                } else {
                    _errorMsg.value = "User data not found!! Please contact support"
                    _isLogInSuccess.value = false
                }

            }

            result.onFailure { exception ->
                _errorMsg.value = exception.message
                _isLogInSuccess.value = false

            }
        }

    }


    fun forgotPass(email: String){
        viewModelScope.launch {
            _passError.value = null
            _isForgotPassSuccess.value = null

            val result = fbrepo.resetPass(email)

            result.onSuccess {
                _isForgotPassSuccess.value = true
            }
                .onFailure { error->
                    _passError.value = error.message
                    _isForgotPassSuccess.value = false
                }
        }
    }

    fun resetStates(){
        _passError.value = null
        _errorMsg.value = null
        _isLogInSuccess.value = null
        _isForgotPassSuccess.value = null
    }
}