
package com.example.fingerprintvotingsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fingerprintvotingsystem.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth=  FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()
            if (userName.isEmpty() || password.isEmpty()){

                Toast.makeText(this, " please Fill all details", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener {  task ->
                        if (task.isSuccessful){
                            Toast.makeText(this,"Sign is successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        }else{
                            Toast.makeText(this, "Sign is failed: @{task.exception?.message}" , Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

    }
}
