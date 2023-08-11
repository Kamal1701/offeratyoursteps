package com.example.offersatyoursteps.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.ActivityHomePageBinding
import com.example.offersatyoursteps.fragments.AllOffersFragment
import com.example.offersatyoursteps.fragments.HomePageFragment
import com.example.offersatyoursteps.fragments.MerchantProfileFragment
import com.example.offersatyoursteps.fragments.OfferNearMeFragment
import com.example.offersatyoursteps.fragments.ProfileFragment
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomePageActivity : AppCompatActivity() {
    
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding : ActivityHomePageBinding
    
    
    
    private lateinit var mAuth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser
    
    private lateinit var userModel : UserModel
    
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
//        Log.d("DEBUG", "HomePageActivity")
//        Log.d("DEBUG", userModel.cName.toString())
//        Log.d("DEBUG", userModel.cEmail.toString())
        
        setSupportActionBar(binding.appBarHomePage.toolbar)
        
        val drawerLayout : DrawerLayout = binding.drawerLayout
        val navView : NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_page,
                R.id.nav_offer_near_me,
                R.id.nav_all_offers,
                R.id.nav_merchant_profile,
                R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        updateNavHeader()
        
        
        navView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item : MenuItem) : Boolean {
                val fragment : Fragment
                when (item!!.itemId) {
                    R.id.nav_offer_near_me -> {
                        
                        fragment = OfferNearMeFragment()
                         if (fragment != null) {
                            
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, fragment).commit()
                            drawerLayout.closeDrawer(GravityCompat.START)
                             supportActionBar?.title = "Offers Near Me"
                         }
//                        Toast.makeText(this@HomePageActivity, "Item 1 clicked", Toast.LENGTH_SHORT).show()
                    }
                    
                    R.id.nav_all_offers -> {
//                        Toast.makeText(this@HomePageActivity, "Item 2 clicked", Toast.LENGTH_SHORT).show()
                        fragment = AllOffersFragment()
                        if (fragment != null) {
                            
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, fragment).commit()
                            drawerLayout.closeDrawer(GravityCompat.START)
                            supportActionBar?.title = "All Offers"
                        }
                    }
                    
                    R.id.nav_profile -> {
//                        Toast.makeText(this@HomePageActivity, "Item 3 clicked", Toast.LENGTH_SHORT).show()
                        fragment = ProfileFragment.newInstance(userModel)
                        if (fragment != null) {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, fragment).commit()
                            drawerLayout.closeDrawer(GravityCompat.START)
                            supportActionBar?.title = "Profile"
                        }
                    }
                    
                    R.id.nav_merchant_profile -> {
                        fragment = MerchantProfileFragment()
                        if(fragment!=null){
                            supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment)
                                .commit()
                            drawerLayout.closeDrawer(GravityCompat.START)
                            supportActionBar?.title = "Profile"
                        }
                    }
                    
                }
                return true
            }
            
        })
//        drawerLayout.closeDrawer(GravityCompat.START)
        
    }
    
    override fun onCreateOptionsMenu(menu : Menu) : Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_page, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        
        when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }
        
        return super.onOptionsItemSelected(item)
    }
    
    
    override fun onSupportNavigateUp() : Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    fun updateNavHeader() {
        val navView : NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val navUserName = headerView.findViewById<TextView>(R.id.navHeaderUsernameTxt)
        val navUserEmail = headerView.findViewById<TextView>(R.id.navHeaderEmailTxt)
        
        navUserName.text = userModel.cName
        navUserEmail.text = userModel.cEmail
        
        val profileMenu = navView.menu.findItem(R.id.nav_profile)
        val merchantProfileMenu = navView.menu.findItem(R.id.nav_merchant_profile)
        if (userModel.isMerchant == "N") {
            profileMenu.setVisible(true)
        } else {
            merchantProfileMenu.setVisible(true)
        }
        
        
        Log.d("DEBUG", "HomePageActivity - menuitems")
//        Log.d("DEBUG", menuView.title.toString())
        
    }
}