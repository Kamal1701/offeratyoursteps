package com.example.offersatyoursteps.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.offersatyoursteps.adapters.ViewPageAdapter
import com.example.offersatyoursteps.databinding.ActivityRegistrationBinding
import com.example.offersatyoursteps.fragments.CustomerRegisterFragment
import com.example.offersatyoursteps.fragments.MerchantRegisterFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RegistrationActivity : AppCompatActivity() {
    
    private lateinit var binding:ActivityRegistrationBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewerPage: ViewPager2
    private lateinit var adapter: ViewPageAdapter
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root as View
        setContentView(view)
    
        tabLayout = binding.tablayoutid
        viewerPage = binding.viewerpage
    
        adapter = ViewPageAdapter(this)
        adapter.addFragment(CustomerRegisterFragment(), "Customer")
        adapter.addFragment(MerchantRegisterFragment(), "Merchant")
    
        binding.viewerpage.adapter = adapter
        binding.viewerpage.currentItem = 0
        TabLayoutMediator(tabLayout, binding.viewerpage){tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }
}