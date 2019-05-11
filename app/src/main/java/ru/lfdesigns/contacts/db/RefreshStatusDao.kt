package ru.lfdesigns.contacts.db

import androidx.room.*
import ru.lfdesigns.contacts.model.RefreshStatus


@Dao
abstract class RefreshStatusDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(status: RefreshStatus): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(status: RefreshStatus): Int

    @Transaction
    open fun insertOrUpdate(status: RefreshStatus) {
        if (insert(status) < 0)
            update(status)
    }

    @Query("SELECT * FROM refresh_status")
    abstract fun status(): RefreshStatus?

}