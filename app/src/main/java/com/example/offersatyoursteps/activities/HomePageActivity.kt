package com.example.offersatyoursteps.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.ActivityHomePageBinding
import com.example.offersatyoursteps.fragments.AddOfferFragment
import com.example.offersatyoursteps.fragments.AllOffersFragment
import com.example.offersatyoursteps.fragments.MerchantProfileFragment
import com.example.offersatyoursteps.fragments.OfferNearMeFragment
import com.example.offersatyoursteps.fragments.CustomerProfileFragment
import com.example.offersatyoursteps.fragments.EditOfferFragment
import com.example.offersatyoursteps.fragments.ProductSubcategoryFragment
import com.example.offersatyoursteps.models.NavigationHeaderViewModel
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomePageActivity : AppCompatActivity() {
    
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding : ActivityHomePageBinding
    private lateinit var navHeaderChangeNotify : NavigationHeaderViewModel
    
    private lateinit var mAuth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser
    
    private lateinit var userModel : UserModel
    
    private lateinit var drawerLayout : DrawerLayout
    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(USER_INFO, userModel)
    }
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!
        
        userModel = intent.getParcelableExtra<UserModel>(USER_INFO)!!
        
        setSupportActionBar(binding.appBarHomePage.toolbar)
        
        drawerLayout = binding.drawerLayout
        val navView : NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)
        
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_page,
                R.id.nav_offer_near_me,
                R.id.nav_all_offers,
                R.id.nav_merchant_profile,
                R.id.nav_profile,
                
                ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        
        if(App.sharedPrefs.isLoggedIn) {
            updateNavHeader()
    
            navView.setNavigationItemSelectedListener { item ->
                val fragment : Fragment
                when (item.itemId) {
                    R.id.nav_offer_near_me -> {
                        val fragment = OfferNearMeFragment.newInstance(userModel)
                        if (fragment != null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(
                                    R.id.nav_host_fragment_content_home_page,
                                    fragment,
                                    "NearMe"
                                )
                                addToBackStack("NearMe")
                            }
                        }
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
            
                    R.id.nav_all_offers -> {
                        val fragment = AllOffersFragment.newInstance(userModel)
                        if (fragment != null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(R.id.nav_host_fragment_content_home_page, fragment)
                                addToBackStack(null)
                            }
                        }
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
            
                    R.id.nav_profile -> {
                        fragment = CustomerProfileFragment.newInstance(userModel)
                        if (fragment != null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(R.id.nav_host_fragment_content_home_page, fragment)
                                addToBackStack(null)
                            }
                            drawerLayout.closeDrawer(GravityCompat.START)
                        }
                    }
            
                    R.id.nav_merchant_profile -> {
                        fragment = MerchantProfileFragment.newInstance(userModel)
                        if (fragment != null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(R.id.nav_host_fragment_content_home_page, fragment)
                                addToBackStack(null)
                            }
                            drawerLayout.closeDrawer(GravityCompat.START)
                        }
                    }
            
                    R.id.addOfferProduct -> {
                
                        fragment = AddOfferFragment.newInstance(userModel)
                        if (fragment != null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(R.id.nav_host_fragment_content_home_page, fragment)
                                addToBackStack(null)
                            }
                            drawerLayout.closeDrawer(GravityCompat.START)
                        }
                
                    }
            
                    R.id.editOfferProduct -> {
                
                        fragment = EditOfferFragment.newInstance(userModel)
                        if (fragment != null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(
                                    R.id.nav_host_fragment_content_home_page,
                                    fragment,
                                    "EditOffer"
                                )
                                addToBackStack("EditOffer")
                            }
                            drawerLayout.closeDrawer(GravityCompat.START)
                        }
                
                    }
                }
        
                true
            }
            navView.setCheckedItem(R.id.nav_offer_near_me)
        }
        
    }
    
    override fun onCreateOptionsMenu(menu : Menu) : Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_page, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        
        when (item.itemId) {
            R.id.action_logout -> {
                if (App.sharedPrefs.isLoggedIn) {
                    App.sharedPrefs.isLoggedIn = false
                    App.sharedPrefs.userId = ""
                    FirebaseAuth.getInstance().signOut()
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
        
    }
    
    
    override fun onSupportNavigateUp() : Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    private fun updateNavHeader() {
        val navView : NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val navUserEmail = headerView.findViewById<TextView>(R.id.navHeaderEmailTxt)
        
        navHeaderChangeNotify = ViewModelProvider(this)[NavigationHeaderViewModel::class.java]
        navHeaderChangeNotify.setUserName(userModel.customerName!!)
        navHeaderChangeNotify.userName.observe(this) {
            val navUserName = headerView.findViewById<TextView>(R.id.navHeaderUsernameTxt)
            navUserName.text = userModel.customerName
            
        }
        navUserEmail.text = userModel.customerEmail
        
        val profileMenu = navView.menu.findItem(R.id.nav_profile)
        val merchantProfileMenu = navView.menu.findItem(R.id.nav_merchant_profile)
        val merchantProductMenu = navView.menu.findItem(R.id.merchantProductMenu)
        if (userModel.isCustomerOrMerchant == "N") {
            profileMenu.isVisible = true
        } else {
            merchantProfileMenu.isVisible = true
            merchantProductMenu.isVisible = true
        }
        
        val fragment = OfferNearMeFragment.newInstance(userModel)
        if (fragment != null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment_content_home_page, fragment, "NearMe")
                addToBackStack("NearMe")
            }
        }
        
    }
    
    override fun onBackPressed() {
        
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onRestoreInstanceState(
        savedInstanceState : Bundle?,
        persistentState : PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if(savedInstanceState != null){
            userModel = savedInstanceState.getParcelable(USER_INFO)!!
        }
    }
    
}


