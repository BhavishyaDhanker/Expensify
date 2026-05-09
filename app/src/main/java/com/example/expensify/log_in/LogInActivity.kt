package com.example.expensify.log_in

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.expensify.log_in.LogInViewModel
import com.example.expensify.MainScreen.MainScreenActivity
import com.example.expensify.sign_up.SignUpActivity
import com.example.expensify.databinding.ActivityLogInBinding
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {

    lateinit private var binding: ActivityLogInBinding

    private val viewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
        observeViewModel()
        }

    private fun setUpListeners(){


        binding.login.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val pass = binding.passwordInput.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty() ) {
                viewModel.loginUser(email, pass)
            }
            else{
                Toast.makeText(this, "Fill all the details !!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.llSignUp.setOnClickListener {
            val intent = Intent(this@LogInActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvForgot.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            if (email.isNotEmpty()){
                viewModel.forgotPass(email)
            }else {
                Toast.makeText(this@LogInActivity, "Please enter Email", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun observeViewModel(){

        lifecycleScope.launch {
            viewModel.isLogInSuccess.collect { success ->
                if (success == true) {
                    val intent = Intent(this@LogInActivity, MainScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        // every .collect is an infinite function it never finishes(until the activity dies) so anything written after that is just going to
        // be ignored     hence we need to create another lifecycleScope instance for any other .collect
        lifecycleScope.launch {
            viewModel.errorMsg.collect { msg->
                if (msg != null){
                    Toast.makeText(this@LogInActivity, msg, Toast.LENGTH_SHORT).show()
                    viewModel.resetStates()
                }

            }
        }

        lifecycleScope.launch {
            viewModel.isForgotPassSuccess.collect { success->
                if (success == true){
                    Toast.makeText(this@LogInActivity, "Password changed Successfully!!", Toast.LENGTH_SHORT).show()
                    viewModel.resetStates()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.passError.collect { error->
                if (error != null){
                    Toast.makeText(this@LogInActivity, error, Toast.LENGTH_SHORT).show()
                    viewModel.resetStates()
                }
            }
        }

    }
}