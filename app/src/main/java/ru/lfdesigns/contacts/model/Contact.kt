package ru.lfdesigns.contacts.model

import androidx.room.*

@Entity(tableName = "contacts", indices = [Index(value = ["name", "phone_normalized"]), Index(value = ["server_id"], unique = true)])
data class Contact(@ColumnInfo(name = "server_id") val id: String,
                   val name: String,
                   @Embedded(prefix = "phone_")  val phone: PhoneNumber,
                   val height: Double,
                   val biography: String,
                   val temperament: Temperament,
                   @Embedded(prefix = "education_period_") val educationPeriod: EducationPeriod) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") var localId: Int = 0
}
