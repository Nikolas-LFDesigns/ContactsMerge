package ru.lfdesigns.contacts.model

import androidx.room.ColumnInfo

/**
 * A wrapper class for a phone number.
 * Allows to freely parse and present data in a convenient way
 * @param countryCode international country code with + sign, e.g. +XXX
 * @param operatorNumber part of the number in brackets which is usually an operator code e.g. (XXX)
 * @param subscriberNumber remaining part of the number e.g. XXX-XXXX
 * @param normalized a normalized representation for phone number without nondialable characters, e.g. +XXXXXXXX[..]
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