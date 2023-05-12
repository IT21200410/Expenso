package com.example.expenso.models

import android.os.Parcel
import android.os.Parcelable

data class ExpensesType(
    var id : String = "",
    var expensesName:String = "",

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(expensesName)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExpensesType> {
        override fun createFromParcel(parcel: Parcel): ExpensesType {
            return ExpensesType(parcel)
        }

        override fun newArray(size: Int): Array<ExpensesType?> {
            return arrayOfNulls(size)
        }
    }
}
