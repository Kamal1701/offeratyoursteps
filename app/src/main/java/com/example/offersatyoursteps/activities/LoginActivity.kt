package com.example.offersatyoursteps.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.hardware.input.InputManager
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.offersatyoursteps.utilities.SetTextColorSpan
import com.example.offersatyoursteps.databinding.ActivityLoginBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    
    private lateinit var loginBinding : ActivityLoginBinding
    
    private lateinit var userEmail : TextView
    private lateinit var userPassword : TextView
    private lateinit var loginProBar : ProgressBar
    private lateinit var loginBtn : Button
    private lateinit var registerBtn : Button
    private lateinit var forgotPassword : Button
    
    private lateinit var mAuth : FirebaseAuth
//    private lateinit var fStore : FirebaseFirestore
    
    private var userModel = UserModel("", "", "", "", "", "", "", "", "")
    
    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(USER_INFO, userModel)
    }
    
    override fun onCreate(savedInstanceState : Bundle?) {
        
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val loginView = loginBinding.root
        if (!App.sharedPrefs.isLoggedIn) {
            setContentView(loginView)
            
            
            userEmail = loginBinding.loginUserEmailTxt
            userPassword = loginBinding.loginPasswordTxt
            loginProBar = loginBinding.loginProgressBar
            loginBtn = loginBinding.loginUserLoginBtn
            registerBtn = loginBinding.loginRegisterBtn
            forgotPassword = loginBinding.loginForgotPwdBtn
            
            mAuth = FirebaseAuth.getInstance()
            
            loginProBar.visibility = View.INVISIBLE
            
            userEmail.requestFocus()
            
            hideKeyboard()
            
            val registerBtn = loginBinding.loginRegisterBtn
            val colorSpan = SetTextColorSpan(registerBtn.text.toString())
            registerBtn.text = colorSpan.setTextColorSpan()
        }

//        forgotPassword.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            val dialogView = layoutInflater.inflate(R.layout.activity_forgot_password, null)
//            builder.setView(dialogView)
//                .setPositiveButton("Reset") { _, _ ->
//                    val resetEmailAddressField = dialogView.findViewById<EditText>(R.id.forgotEmail)
//                    val resetEmailAddress = resetEmailAddressField.text.toString()
//                    if (resetEmailAddress.isNotEmpty() && isValidEmail(resetEmailAddress)) {
//                        mAuth.sendPasswordResetEmail(resetEmailAddress).addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(
//                                    this,
//                                    "Password Reset email sent to your email address, please check your inbox",
//                                    Toast.LENGTH_LONG
//                                ).show()
////                                finish()
//                            }
//                        }.addOnFailureListener {
//                            Toast.makeText(this, it.localizedMessage.toString(), Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Toast.makeText(this, "Please enter your valid email address", Toast.LENGTH_LONG).show()
//                    }
//                }
//                .setNegativeButton("Cancel"){_,_ ->
//
//                }
//                .show()
//        }

//        val callback = object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                val alertDialog = AlertDialog.Builder(this@LoginActivity)
//                    .setTitle("Offers At Your Step")
//                    .setMessage("Do you want to exit?")
//                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
//                        finish()
//                    })
//                    .setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> })
//                    .create()
//                alertDialog.show()
//            }
//        }
//
//        onBackPressedDispatcher.addCallback(this, callback)
        
    }
    
    override fun onRestoreInstanceState(savedInstanceState : Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            userModel = savedInstanceState.getParcelable(USER_INFO)!!
        }
    }
    
    fun onUserLoginBtnClicked(view : View) {
        
        enableSpinner(true)
        
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your credential", Toast.LENGTH_LONG).show()
            enableSpinner(false)
            
        } else {
            if (isValidEmail(email)) {
                userSignIn(email, password)
            } else {
                enableSpinner(false)
                userEmail.requestFocus()
                Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_LONG).show()
                
            }
        }
    }
    
    private fun userSignIn(email : String, password : String) {
        
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task : Task<AuthResult> ->
                if (task.isSuccessful) {
                    enableSpinner(false)
                    
                    val userId = mAuth.currentUser!!.uid
                    App.sharedPrefs.isLoggedIn = true
                    App.sharedPrefs.userId = userId
                    println("Login success")
                    DatabaseServices.getCustomerInfoRecord(userId, userModel) { dbSuccess ->
                        
                        if (dbSuccess) {
                            loadHomePage()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login failed, invalid credential", Toast.LENGTH_LONG)
                        .show()
//                    loginProBar.visibility = View.INVISIBLE
//                    loginBtn.visibility = View.VISIBLE
//                    registerBtn.visibility = View.VISIBLE
                    enableSpinner(false)
                }
            }
        
    }
    
    fun onRegisterBtnClicked(view : View) {
        
        val registerIntent = Intent(this, RegistrationActivity::class.java)
        startActivity(registerIntent)
        finish()
    }
    
    private fun loadHomePage() {
        val homeActivityIntent = Intent(this, HomePageActivity::class.java)
        homeActivityIntent.putExtra(USER_INFO, userModel)
        startActivity(homeActivityIntent)
        finish()
    }
    
    fun onForgotPwdBtnClicked(view : View) {
        val forgotIntent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(forgotIntent)
    }
    
    fun isValidEmail(email : String) : Boolean {
        val regex = Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$")
        return regex.matches(email)
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
    
    private fun enableSpinner(enabled : Boolean) {
        if (enabled) {
            loginProBar.visibility = View.VISIBLE
        } else {
            loginProBar.visibility = View.INVISIBLE
        }
        loginBtn.isEnabled = !enabled
        registerBtn.isEnabled = !enabled
        forgotPassword.isEnabled = !enabled
    }
    
    override fun onStart() {
        super.onStart()
        
        if (App.sharedPrefs.isLoggedIn) {
            if (App.sharedPrefs.userId != null) {
                DatabaseServices.getCustomerInfoRecord(
                    App.sharedPrefs.userId.toString(),
                    userModel
                ) { dbSuccess ->
                    if (dbSuccess) {
                        loadHomePage()
                    }
                }
            }
        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//    }
}