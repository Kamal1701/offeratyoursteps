package com.example.offersatyoursteps.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.offersatyoursteps.utilities.SetTextColorSpan
import com.example.offersatyoursteps.databinding.ActivityLoginBinding
import com.example.offersatyoursteps.fragments.OfferNearMeFragment
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    
    private lateinit var loginBinding : ActivityLoginBinding
    
    private lateinit var userEmail : TextView
    private lateinit var userPassword : TextView
    private lateinit var loginProBar : ProgressBar
    private lateinit var loginBtn : Button
    
    private lateinit var mAuth : FirebaseAuth
    private lateinit var fStore : FirebaseFirestore
    
    private var userModel = UserModel("", "", "", "", "", "")
    
    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(USER_INFO, userModel)
    }
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val loginView = loginBinding.root
        setContentView(loginView)
        
        userEmail = loginBinding.loginUserEmailTxt
        userPassword = loginBinding.loginPasswordTxt
        loginProBar = loginBinding.loginProgressBar
        loginBtn = loginBinding.loginUserLoginBtn
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        
        loginProBar.visibility = View.INVISIBLE
        
        val registerBtn = loginBinding.loginRegisterBtn
        val colorSpan = SetTextColorSpan(registerBtn.text.toString())
        registerBtn.text = colorSpan.setTextColorSpan()
        
    }
    
    override fun onRestoreInstanceState(savedInstanceState : Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            userModel = savedInstanceState.getParcelable(USER_INFO)!!
        }
    }
    
    fun onUserLoginBtnClicked(view : View) {
        loginProBar.visibility = View.VISIBLE
        loginBtn.visibility = View.INVISIBLE
        
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your credential", Toast.LENGTH_LONG).show()
            loginBtn.visibility = View.VISIBLE
            loginProBar.visibility = View.INVISIBLE
            
        } else {
            userSignIn(email, password)
        }
    }
    
    fun userSignIn(email : String, password : String) {
        
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task : Task<AuthResult> ->
                if (task.isSuccessful) {
                    loginProBar.visibility = View.INVISIBLE
                    loginBtn.visibility = View.VISIBLE
                    
                    getDatafromFirestore { returnSuccess ->
                        if (returnSuccess) {
                            loadHomePage()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login failed, invalid credential", Toast.LENGTH_LONG)
                        .show()
//                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                    loginProBar.visibility = View.INVISIBLE
                    loginBtn.visibility = View.VISIBLE
                }
            }
        
    }
    
    fun onRegisterBtnClicked(view : View) {
//        val registerIntent = Intent(this, RegisterActivity::class.java)
        val registerIntent = Intent(this, RegistrationActivity::class.java)
        startActivity(registerIntent)
        finish()
    }
    
    
    private fun loadHomePage() {
        val homeActivityIntent = Intent(this, HomePageActivity::class.java)
        homeActivityIntent.putExtra(USER_INFO, userModel)
//        Log.d("DEBUG", "LoginActivity")
//        Log.d("DEBUG", userModel.cName.toString())
//        Log.d("DEBUG", userModel.cEmail.toString())
        startActivity(homeActivityIntent)
        finish()
    }
    
    private fun getDatafromFirestore(complete : (Boolean) -> Unit) {
        val userId = mAuth.currentUser!!.uid
        fStore.collection("CustomerInfo").document(userId)
            .get().addOnSuccessListener { customerDocument ->
                if (customerDocument != null) {
//                    val name = customerDocument.get("User_Name")
                    userModel.cName = customerDocument.get("User_Name").toString()
                    userModel.cEmail = customerDocument.get("User_EmailId").toString()
                    userModel.isMerchant = customerDocument.get("IsMerchant").toString()
                    userModel.cCity = customerDocument.get("City").toString()
                    userModel.cState = customerDocument.get("State").toString()
//                    Log.d("DEBUG", name.toString())
//                    Log.d("DEBUG", customerDocument.get("User_EmailId").toString())
//                    Log.d("DEBUG", customerDocument.get("IsMerchant").toString())
                    complete(true)
                } else {
                    Log.d("DEBUG", "No such document")
                    complete(true)
                }
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }

//    override fun onStart() {
//        super.onStart()
//        val user = mAuth.currentUser
//        if(user != null){
//            updateUI()
//        }
//    }
}