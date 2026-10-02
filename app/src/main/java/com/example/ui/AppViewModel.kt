package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

data class UiState(
    val machines: List<MachineEntity> = emptyList(),
    val records: List<RecordEntity> = emptyList(),
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true,
    val currentTime: Long = System.currentTimeMillis(),
    val selectedTrendMachineId: String? = null
)

private data class BaseData(
    val machines: List<MachineEntity>,
    val records: List<RecordEntity>,
    val sound: Boolean,
    val haptic: Boolean
)

class AppViewModel(
    private val repository: AppRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _currentTime = MutableStateFlow(System.currentTimeMillis())
    private val _selectedTrendMachineId = MutableStateFlow<String?>(null)

    private val baseState = combine(
        repository.allMachines,
        repository.allRecords,
        settingsRepository.soundEnabledFlow,
        settingsRepository.hapticEnabledFlow
    ) { machines, records, sound, haptic ->
        BaseData(machines, records, sound, haptic)
    }

    val uiState: StateFlow<UiState> = combine(
        baseState,
        _currentTime,
        _selectedTrendMachineId
    ) { base, time, selectedMachineId ->
        UiState(base.machines, base.records, base.sound, base.haptic, time, selectedMachineId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    val weeklyTrend: StateFlow<WeeklyTrendData> = uiState.map { state ->
        calculateWeeklyTrend(
            machines = state.machines,
            records = state.records,
            currentTime = state.currentTime,
            selectedMachineId = state.selectedTrendMachineId
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        WeeklyTrendData(
            days = emptyList(),
            historicalAverageHours = 4.5f,
            currentWeekAverageHours = 0f,
            totalWeekHours = 0f,
            peakDay = null,
            deltaVsAverageHours = 0f,
            deltaPercentVsAverage = 0f,
            selectedMachineId = null
        )
    )

    init {
        // Ticker for updating current time
        viewModelScope.launch {
            while (true) {
                _currentTime.value = System.currentTimeMillis()
                delay(1000)
            }
        }
        
        // Check date for daily reset
        viewModelScope.launch {
            val lastDate = settingsRepository.lastDateFlow.first()
            val currentDate = settingsRepository.getCurrentDateString()
            if (lastDate.isNotEmpty() && lastDate != currentDate) {
                repository.resetDailyStats()
                settingsRepository.setLastDate(currentDate)
            }
            
            // Check if machines is empty (default setup)
            val initialMachines = repository.allMachines.first()
            if (initialMachines.isEmpty() && lastDate.isEmpty()) {
                seedDefaultFleetAndHistory(currentDate)
            }
        }
    }

    private suspend fun seedDefaultFleetAndHistory(currentDate: String) {
        val m1Id = UUID.randomUUID().toString()
        val m2Id = UUID.randomUUID().toString()
        val m3Id = UUID.randomUUID().toString()

        val defaults = listOf(
            MachineEntity(m1Id, "Dunder Singh", "John Deere 5310", 6300L, false, null),
            MachineEntity(m2Id, "Vikram Singh", "Sonalika DI 745", 3600L, false, null),
            MachineEntity(m3Id, "Sakur Khan", "Swaraj 855 FE", 1800L, false, null)
        )
        defaults.forEach { repository.insertMachine(it) }

        // Seed realistic historical records for past 6 days
        val now = System.currentTimeMillis()
        val dayMillis = 24L * 60L * 60L * 1000L

        val historicalLogs = listOf(
            // 6 days ago
            RecordEntity(UUID.randomUUID().toString(), m1Id, "Dunder Singh", "Field Operation", 11400L, now - 6 * dayMillis + 14 * 3600000L),
            RecordEntity(UUID.randomUUID().toString(), m2Id, "Vikram Singh", "Tillage Harrow", 8400L, now - 6 * dayMillis + 18 * 3600000L),
            // 5 days ago
            RecordEntity(UUID.randomUUID().toString(), m1Id, "Dunder Singh", "Rotavator Run", 14400L, now - 5 * dayMillis + 11 * 3600000L),
            RecordEntity(UUID.randomUUID().toString(), m3Id, "Sakur Khan", "Water Pumping", 11700L, now - 5 * dayMillis + 16 * 3600000L),
            // 4 days ago
            RecordEntity(UUID.randomUUID().toString(), m2Id, "Vikram Singh", "Laser Leveling", 13500L, now - 4 * dayMillis + 10 * 3600000L),
            RecordEntity(UUID.randomUUID().toString(), m3Id, "Sakur Khan", "Haulage Transit", 5400L, now - 4 * dayMillis + 17 * 3600000L),
            // 3 days ago
            RecordEntity(UUID.randomUUID().toString(), m1Id, "Dunder Singh", "Cultivator Prep", 12600L, now - 3 * dayMillis + 12 * 3600000L),
            RecordEntity(UUID.randomUUID().toString(), m2Id, "Vikram Singh", "Sowing Seed Drill", 10200L, now - 3 * dayMillis + 15 * 3600000L),
            // 2 days ago
            RecordEntity(UUID.randomUUID().toString(), m1Id, "Dunder Singh", "Plow Deep Furrow", 15300L, now - 2 * dayMillis + 9 * 3600000L),
            RecordEntity(UUID.randomUUID().toString(), m3Id, "Sakur Khan", "Threshing Work", 10800L, now - 2 * dayMillis + 14 * 3600000L),
            // 1 day ago
            RecordEntity(UUID.randomUUID().toString(), m2Id, "Vikram Singh", "Trailer Transport", 12000L, now - 1 * dayMillis + 11 * 3600000L),
            RecordEntity(UUID.randomUUID().toString(), m3Id, "Sakur Khan", "Sprayer Broadcast", 9600L, now - 1 * dayMillis + 16 * 3600000L),
            // Today completed sessions
            RecordEntity(UUID.randomUUID().toString(), m1Id, "Dunder Singh", "Morning Shift", 6300L, now - 2 * 3600000L),
            RecordEntity(UUID.randomUUID().toString(), m2Id, "Vikram Singh", "Field Irrigation", 3600L, now - 4 * 3600000L)
        )

        historicalLogs.forEach { repository.insertRecord(it) }
        settingsRepository.setLastDate(currentDate)
    }

    fun selectTrendMachine(machineId: String?) {
        _selectedTrendMachineId.value = machineId
    }

    private fun calculateWeeklyTrend(
        machines: List<MachineEntity>,
        records: List<RecordEntity>,
        currentTime: Long,
        selectedMachineId: String?
    ): WeeklyTrendData {
        val daysList = mutableListOf<DayRuntimeItem>()

        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance().apply {
                timeInMillis = currentTime
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DAY_OF_YEAR, -i)
            }
            val startOfDay = cal.timeInMillis
            val endOfDay = startOfDay + (24L * 60L * 60L * 1000L) - 1L
            val isToday = (i == 0)

            val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
            val dayOfWeek = dayFormat.format(Date(startOfDay)).uppercase()
            val dateLabel = dateFormat.format(Date(startOfDay))

            val dayRecords = records.filter { it.time in startOfDay..endOfDay }
            val filteredRecords = if (selectedMachineId != null) {
                dayRecords.filter { it.machineId == selectedMachineId }
            } else {
                dayRecords
            }

            val recordsSec = filteredRecords.sumOf { it.duration }

            // For today, add active live running machine durations
            val liveSec = if (isToday) {
                if (selectedMachineId == null) {
                    machines.filter { it.isRunning && it.startAt != null }.sumOf {
                        (currentTime - it.startAt!!) / 1000L
                    }
                } else {
                    machines.filter { it.id == selectedMachineId && it.isRunning && it.startAt != null }.sumOf {
                        (currentTime - it.startAt!!) / 1000L
                    }
                }
            } else {
                0L
            }

            val totalSec = recordsSec + liveSec
            val hours = totalSec / 3600f

            // Breakdown by tractor
            val breakdown = machines.mapNotNull { m ->
                if (selectedMachineId != null && m.id != selectedMachineId) {
                    null
                } else {
                    val mRecSec = dayRecords.filter { it.machineId == m.id }.sumOf { it.duration }
                    val mLiveSec = if (isToday && m.isRunning && m.startAt != null) (currentTime - m.startAt!!) / 1000L else 0L
                    val mHours = (mRecSec + mLiveSec) / 3600f
                    if (mHours > 0.05f) {
                        MachineHoursBreakdown(
                            machineId = m.id,
                            machineName = m.name,
                            brand = m.brand,
                            hours = mHours
                        )
                    } else null
                }
            }

            daysList.add(
                DayRuntimeItem(
                    dayIndex = 6 - i,
                    dayOfWeek = dayOfWeek,
                    dateLabel = dateLabel,
                    isToday = isToday,
                    runtimeSeconds = totalSec,
                    runtimeHours = hours,
                    sessionCount = filteredRecords.size + (if (isToday && liveSec > 0) 1 else 0),
                    machineBreakdown = breakdown
                )
            )
        }

        // Calculate historical average (benchmark)
        val pastDays = daysList.take(6)
        val validPastDays = pastDays.filter { it.runtimeHours > 0f }
        val historicalAverage = if (validPastDays.isNotEmpty()) {
            (validPastDays.sumOf { it.runtimeHours.toDouble() } / validPastDays.size).toFloat()
        } else {
            if (selectedMachineId == null) 5.2f else 2.5f
        }

        val totalHours = daysList.sumOf { it.runtimeHours.toDouble() }.toFloat()
        val currentWeekAvg = totalHours / 7f
        val peakDay = daysList.maxByOrNull { it.runtimeHours }
        val deltaVsAverage = currentWeekAvg - historicalAverage
        val deltaPercent = if (historicalAverage > 0f) (deltaVsAverage / historicalAverage) * 100f else 0f

        return WeeklyTrendData(
            days = daysList,
            historicalAverageHours = (Math.round(historicalAverage * 10f) / 10f),
            currentWeekAverageHours = (Math.round(currentWeekAvg * 10f) / 10f),
            totalWeekHours = (Math.round(totalHours * 10f) / 10f),
            peakDay = peakDay,
            deltaVsAverageHours = (Math.round(deltaVsAverage * 10f) / 10f),
            deltaPercentVsAverage = (Math.round(deltaPercent * 10f) / 10f),
            selectedMachineId = selectedMachineId
        )
    }

    fun toggleMachine(machine: MachineEntity) {
        viewModelScope.launch {
            if (machine.isRunning) {
                // Stop it
                val now = System.currentTimeMillis()
                val durationMs = now - (machine.startAt ?: now)
                val durationSec = durationMs / 1000
                
                val updated = machine.copy(
                    isRunning = false,
                    startAt = null,
                    totalTime = machine.totalTime + durationSec
                )
                repository.insertMachine(updated)
                
                // Add record
                val record = RecordEntity(
                    id = UUID.randomUUID().toString(),
                    machineId = machine.id,
                    machineName = machine.name,
                    action = "Engine Stop",
                    duration = durationSec,
                    time = now
                )
                repository.insertRecord(record)
            } else {
                // Start it
                val updated = machine.copy(
                    isRunning = true,
                    startAt = System.currentTimeMillis()
                )
                repository.insertMachine(updated)
            }
        }
    }

    fun addMachine(name: String, brand: String) {
        viewModelScope.launch {
            val m = MachineEntity(UUID.randomUUID().toString(), name, brand.ifBlank { "Tractor" }, 0L, false, null)
            repository.insertMachine(m)
        }
    }

    fun updateMachine(id: String, name: String, brand: String) {
        viewModelScope.launch {
            val machine = uiState.value.machines.find { it.id == id } ?: return@launch
            repository.insertMachine(machine.copy(name = name, brand = brand.ifBlank { "Tractor" }))
        }
    }

    fun deleteMachine(id: String) {
        viewModelScope.launch {
            val machine = uiState.value.machines.find { it.id == id }
            if (machine?.isRunning == true) {
                toggleMachine(machine) // stop first to save log
            }
            repository.deleteMachine(id)
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setSoundEnabled(enabled) }
    }

    fun toggleHaptic(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setHapticEnabled(enabled) }
    }

    fun factoryReset() {
        viewModelScope.launch {
            repository.resetAllData()
            val currentDate = settingsRepository.getCurrentDateString()
            seedDefaultFleetAndHistory(currentDate)
        }
    }
}
