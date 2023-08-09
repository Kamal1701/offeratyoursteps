package com.example.offersatyoursteps.utilities

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

class SetTextColorSpan(val spanText:String) {
    
    fun setTextColorSpan():SpannableStringBuilder{
        val spanText = SpannableStringBuilder(spanText)
        spanText.setSpan(
            ForegroundColorSpan(Color.rgb(50, 168,160)),
            22,
            spanText.length,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return spanText
    }
    
}