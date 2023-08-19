package com.example.offersatyoursteps.utilities

import android.text.Editable
import android.text.TextWatcher

class OfferTextWatcher : TextWatcher {
    override fun beforeTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {
        TODO("Not yet implemented")
    }
    
    override fun onTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {
        TODO("Not yet implemented")
    }
    
    override fun afterTextChanged(s: Editable?) {
        TODO("Not yet implemented")
        s?.append(RUPEE_SYMBOL)
    }
}