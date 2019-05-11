package ru.lfdesigns.contacts.db

import androidx.room.TypeConverter
import ru.lfdesigns.contacts.model.Temperament

class TemperamentConverter {

    @TypeConverter
    fun toTemperament(ordinal: Int): Temperament {
        return Temperament.values()[ordinal]
    }

    @TypeConverter
    fun toOrdinal(temperament: Temperament): Int {
        return temperament.ordinal
    }

}