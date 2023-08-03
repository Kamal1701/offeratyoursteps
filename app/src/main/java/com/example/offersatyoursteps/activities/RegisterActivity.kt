package com.example.offersatyoursteps.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.offersatyoursteps.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
//    private val binding get() = _binding
    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userPassword : TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var registerBtn: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userName = binding.regUserNameTxt
        userEmail = binding.regUserEmailTxt
        userPassword = binding.regPasswordTxt
        registerBtn = binding.regUserRegisterBtn
        progressBar = binding.regProgressBar

        progressBar.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()

    }

    fun onUserRegisterBtnClicked(view : View){
        registerBtn.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        val email = userEmail.text.toString()
        val password = userPassword.text.toString()
        val username = userName.text.toString()

        if(email.isEmpty()||password.isEmpty()||username.isEmpty()){
            Toast.makeText(this, "Please fill the required details", Toast.LENGTH_LONG).show()
            registerBtn.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        } else{
            createUserAccount(username, email, password)
        }
    }

    fun createUserAccount(name:String, email:String, password:String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task:Task<AuthResult> ->
            if(task.isSuccessful){
                Toast.makeText(this, "User registration success", Toast.LENGTH_SHORT).show()

//                updateUserInfo(name, mAuth.currentUser)
                updateUI()

            } else{
                Toast.makeText(this, "User registration failed", Toast.LENGTH_SHORT).show()
                registerBtn.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }

        }
    }

//    fun updateUserInfo(name: String, currentUser: FirebaseUser?){
//
//    }

    private fun updateUI(){
        val homeActivityIntent = Intent(this, HomePageActivity::class.java)
        startActivity(homeActivityIntent)
        finish()
    }
}