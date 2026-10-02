package com.example.ui

data class DayRuntimeItem(
    val dayIndex: Int, // 0 = 6 days ago, ..., 6 = today
    val dayOfWeek: String, // e.g. "Mon", "Tue"
    val dateLabel: String, // e.g. "26 Sep"
    val isToday: Boolean,
    val runtimeSeconds: Long,
    val runtimeHours: Float, // in hours, e.g. 5.4f
    val sessionCount: Int,
    val machineBreakdown: List<MachineHoursBreakdown> = emptyList()
)

data class MachineHoursBreakdown(
    val machineId: String,
    val machineName: String,
    val brand: String,
    val hours: Float
)

data class WeeklyTrendData(
    val days: List<DayRuntimeItem>,
    val historicalAverageHours: Float,
    val currentWeekAverageHours: Float,
    val totalWeekHours: Float,
    val peakDay: DayRuntimeItem?,
    val deltaVsAverageHours: Float,
    val deltaPercentVsAverage: Float,
    val selectedMachineId: String? // null = all fleet
)
