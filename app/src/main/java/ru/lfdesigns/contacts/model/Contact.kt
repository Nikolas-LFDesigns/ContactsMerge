package ru.lfdesigns.contacts.model

import androidx.room.*
import java.util.*

// TODO: remove @Embedded in favor to @Relation
@Entity(tableName = "contacts", indices = [Index(value = ["name", "phone_normalized"]), Index(value = ["server_id"], unique = true)])
data class Contact(@ColumnInfo(name = "server_id") val id: String = "",
                   val name: String = "",
                   @Embedded(prefix = "phone_")  val phone: PhoneNumber = PhoneNumber(normalized = ""),
                   val height: Double = 0.0,
                   val biography: String = "",
                   val temperament: Temperament = Temperament.choleric,
                   @Embedded(prefix = "education_period_") val educationPeriod: EducationPeriod = EducationPeriod(
                       Date(0), Date(0))
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") var localId: Int = 0

    val isValid: Boolean
     get() = id.isNotEmpty()
}
