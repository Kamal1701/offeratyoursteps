package com.example.offersatyoursteps.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class ForgotPasswordActivity : AppCompatActivity() {
    
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var emailTxt : EditText
    private lateinit var cancelBtn : Button
    private lateinit var resetBtn : Button
    
    private lateinit var mAuth : FirebaseAuth
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        emailTxt = binding.forgotEmail
        cancelBtn = binding.forgotCancel
        resetBtn = binding.resetBtn
        
        mAuth = FirebaseAuth.getInstance()
        
        setContentView(binding.root)
    }
    
    fun onCancelBtnClicked(view:View) {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
    
    fun onResetBtnClicked(view:View) {
        val emailAddress = emailTxt.text.toString()
        if (emailAddress.isNotEmpty() && isValidEmail(emailAddress)) {
            mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { Task ->
                if (Task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password Reset email sent to your email address, please check your inbox",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Please enter your valid email address", Toast.LENGTH_LONG).show()
        }
    }
    
    fun isValidEmail(email : String) : Boolean {
        val regex = Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$")
        return regex.matches(email)
        
    }
    
}