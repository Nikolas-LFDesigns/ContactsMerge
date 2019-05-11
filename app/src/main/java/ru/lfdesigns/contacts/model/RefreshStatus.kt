package ru.lfdesigns.contacts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "refresh_status", indices = [Index(value = ["rowid"], unique = true)])
data class RefreshStatus(
    @PrimaryKey @ColumnInfo(name = "rowid") var id: Int = 0,
    @ColumnInfo(name = "contacts") var forContacts: Date
)