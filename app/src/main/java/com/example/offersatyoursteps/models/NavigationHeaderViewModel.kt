package com.example.offersatyoursteps.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationHeaderViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName : LiveData<String> get() = _userName
    
    fun setUserName(newUserName : String){
        _userName.value = newUserName
    }

}