package com.example.offersatyoursteps.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter(activity:FragmentActivity):FragmentStateAdapter(activity) {
    
    private val mFragmentManager: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList:MutableList<String> = ArrayList()
    
    fun getTabTitle(position : Int):String {
        return mFragmentTitleList[position]
    }
    
    fun addFragment(fragment : Fragment, title:String){
        mFragmentManager.add(fragment)
        mFragmentTitleList.add(title)
    }
    
    override fun getItemCount() : Int {
        return mFragmentManager.size
    }
    
    override fun createFragment(position : Int) : Fragment {
        return mFragmentManager[position]
    }
}