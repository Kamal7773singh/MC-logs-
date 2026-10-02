package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {
    val allMachines: Flow<List<MachineEntity>> = dao.getAllMachines()
    val allRecords: Flow<List<RecordEntity>> = dao.getAllRecords()

    suspend fun insertMachine(machine: MachineEntity) = dao.insertMachine(machine)
    suspend fun deleteMachine(id: String) = dao.deleteMachineById(id)
    suspend fun insertRecord(record: RecordEntity) = dao.insertRecord(record)
    
    suspend fun resetAllData() {
        dao.deleteAllMachines()
        dao.deleteAllRecords()
    }
    
    suspend fun resetDailyStats() {
        dao.resetDailyStats()
    }
}
