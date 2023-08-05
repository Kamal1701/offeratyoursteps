package com.example.offersatyoursteps.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.offersatyoursteps.activities.utilities.SetTextColorSpan
import com.example.offersatyoursteps.databinding.ActivityLoginBinding
import com.example.offersatyoursteps.fragments.OfferNearMeFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    private lateinit var userEmail : TextView
    private lateinit var userPassword : TextView
    private lateinit var loginProBar: ProgressBar
    private lateinit var loginBtn: Button

    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val loginView = loginBinding.root
        setContentView(loginView)

        userEmail = loginBinding.loginUserEmailTxt
        userPassword = loginBinding.loginPasswordTxt
        loginProBar = loginBinding.loginProgressBar
        loginBtn = loginBinding.loginUserLoginBtn
        mAuth = FirebaseAuth.getInstance()

        loginProBar.visibility = View.INVISIBLE
        
        val registerBtn = loginBinding.loginRegisterBtn
        val colorSpan = SetTextColorSpan(registerBtn.text.toString())
        registerBtn.text = colorSpan.setTextColorSpan()

    }

    fun onUserLoginBtnClicked(view:View){
        loginProBar.visibility = View.VISIBLE
        loginBtn.visibility = View.INVISIBLE

        val email = userEmail.text.toString()
        val password = userPassword.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter your credential", Toast.LENGTH_LONG).show()
            loginBtn.visibility = View.VISIBLE
            loginProBar.visibility = View.INVISIBLE

        } else{
            userSignIn(email, password)
        }
    }

    fun userSignIn(email:String, password:String){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task:Task<AuthResult> ->
            if(task.isSuccessful){
                loginProBar.visibility = View.INVISIBLE
                loginBtn.visibility = View.VISIBLE
                updateUI()
            } else{
                Toast.makeText(this, "Login failed, invalid credential", Toast.LENGTH_LONG).show()
//                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                loginProBar.visibility = View.INVISIBLE
                loginBtn.visibility = View.VISIBLE
            }
        }

    }
    
    fun onRegisterBtnClicked(view : View){
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
        finish()
    }


    private fun updateUI(){
        val homeActivityIntent = Intent(this, HomePageActivity::class.java)
        startActivity(homeActivityIntent)
        finish()
    }

//    override fun onStart() {
//        super.onStart()
//        val user = mAuth.currentUser
//        if(user != null){
//            updateUI()
//        }
//    }
}