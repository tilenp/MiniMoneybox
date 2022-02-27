package com.example.minimoneybox.mapper.local

import android.content.Context
import com.example.minimoneybox.R
import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.Amount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmountMapper @Inject constructor(
    private val context: Context
) : Mapper<Double?, Amount> {

    override fun map(objectToMap: Double?): Amount {
       return Amount(
           amount = objectToMap,
           formattedAmount = objectToMap?.let { "£$it" } ?: context.getString(R.string.No_data)
       )
    }
}