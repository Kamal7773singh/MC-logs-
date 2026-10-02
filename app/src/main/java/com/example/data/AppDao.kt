package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM machines")
    fun getAllMachines(): Flow<List<MachineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachine(machine: MachineEntity)

    @Query("DELETE FROM machines WHERE id = :id")
    suspend fun deleteMachineById(id: String)
    
    @Query("DELETE FROM machines")
    suspend fun deleteAllMachines()

    @Query("SELECT * FROM records ORDER BY time DESC")
    fun getAllRecords(): Flow<List<RecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: RecordEntity)
    
    @Query("DELETE FROM records")
    suspend fun deleteAllRecords()
    
    @Query("UPDATE machines SET totalTime = 0, isRunning = 0, startAt = null")
    suspend fun resetDailyStats()
}
