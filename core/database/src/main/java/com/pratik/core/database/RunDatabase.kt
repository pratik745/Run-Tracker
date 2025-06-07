package com.pratik.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pratik.core.database.dao.RunDao
import com.pratik.core.database.dao.RunPendingSyncDao
import com.pratik.core.database.entity.DeletedRunSyncEntity
import com.pratik.core.database.entity.RunEntity
import com.pratik.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
    ],
    version = 1
)
abstract class RunDatabase: RoomDatabase() {

    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
}
