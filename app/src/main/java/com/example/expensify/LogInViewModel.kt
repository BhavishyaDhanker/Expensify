package com.example.expensify

import androidx.lifecycle.ViewModel
import com.google.android.play.core.integrity.p
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LogInViewModel: ViewModel() {

    private val fbrepo = FirebaseRepository()

    private val _user =  MutableStateFlow<User>(User())

    val user = _user.asStateFlow()

    init {
        storeUserData()
    }


    private fun getUserData(email: String, pass: String){

    }

    private fun storeUserData(uid: String){
        viewModelScope.launch{
            val result = fbrepo.getUser(uid)
            if (result == null){

            }else{
                _user.value = result
            }

        }


    }
}

