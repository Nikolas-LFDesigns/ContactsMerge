package ru.lfdesigns.contacts.model

import androidx.room.ColumnInfo

/**
 * A wrapper class for a phone number.
 * Allows to freely parse and present data in a convenient way
 */
data class PhoneNumber(@ColumnInfo(name = "country_code") val countryCode: String = "",
                       @ColumnInfo(name = "operator_number") val operatorNumber: String = "",
                       @ColumnInfo(name = "subscriber_number") val subscriberNumber: String = "",
                       val normalized: String) {
    override fun toString(): String {
        return if (countryCode.isEmpty() || operatorNumber.isEmpty() || subscriberNumber.isEmpty()){
            normalized
        }else {
            "$countryCode ($operatorNumber) $subscriberNumber"
        }
    }
}