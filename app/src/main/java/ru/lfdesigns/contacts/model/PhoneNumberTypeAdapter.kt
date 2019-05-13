package ru.lfdesigns.contacts.model

import android.os.Build
import android.telephony.PhoneNumberUtils
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class PhoneNumberTypeAdapter: JsonDeserializer<PhoneNumber> {

    @Throws(JsonParseException::class)
    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): PhoneNumber {
        val number = element.asString
        // split number into code, operator and subscriber parts
        val regex = Regex("(\\+?[\\d+]+) \\(?([\\d]+)\\)? +([\\d-]+)")
        val result = regex.find(number)
        val normalized = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PhoneNumberUtils.normalizeNumber(number)
        } else {
            PhoneNumberUtils.stripSeparators(number)
        }
        result?.let {
            return PhoneNumber(
                countryCode = it.groups[1]?.value ?: "",
                operatorNumber = it.groups[2]?.value ?: "",
                subscriberNumber = it.groups[3]?.value ?: "",
                normalized = normalized
            )
        }?: return PhoneNumber(normalized = normalized).also {
            Log.w(PhoneNumberTypeAdapter::class.java.simpleName, "Unidentified number: $number")
        }
    }

}