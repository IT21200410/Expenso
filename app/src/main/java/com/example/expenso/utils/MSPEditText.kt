package com.example.expenso.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MSPEditText(context: Context, attrs:AttributeSet):AppCompatEditText(context, attrs){

    init{
        applyFont()
    }

    private fun applyFont()
    {
        val boldtypeFace: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")

    }
}