package com.example.expensify.sign_up

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.expensify.MainScreen.MainScreenActivity
import com.example.expensify.R
import com.example.expensify.databinding.ActivitySignUpBinding
import com.example.expensify.log_in.LogInActivity
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    lateinit var binding : ActivitySignUpBinding

    private val viewModel : SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupListeners()
        observeStates()
    }

    fun setupListeners(){
        binding.signUp.setOnClickListener {
            val email = binding.emailInputET.text.toString().trim()
            val name = binding.nameInputET.text.toString().trim()
            val pass = binding.passwordInputET.text.toString().trim()

            if (email.isNotEmpty() && name.isNotEmpty() && pass.isNotEmpty()){
                lifecycleScope.launch {
                    viewModel.signUp(email, name, pass)
                }
            }else{
                Toast.makeText(this@SignUpActivity, "Please fill all Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.llLogIn.setOnClickListener {
            val Intent = Intent(this, LogInActivity::class.java)
            startActivity(Intent)
            finish()
        }
    }

    fun observeStates(){
        lifecycleScope.launch {
            viewModel.isSignUpSuccessful.collect { result->
                if (result == true){
                    val Intent = Intent(this@SignUpActivity, MainScreenActivity::class.java)
                    startActivity(Intent)
                    finish()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorMsg.collect { msg->
                if(msg != null){
                    Toast.makeText(this@SignUpActivity, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
