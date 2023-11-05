package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

    var shouldReturnError = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if(shouldReturnError){
            return Result.Error(remindersNotFound)
        }
        reminders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(remindersNotFound)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error(remindersNotFound)
        }
        val reminder = reminders?.find{ it.id == id }
        return if (reminder != null){
            Result.Success(reminder)
        }
        else {
            Result.Error(remindersNotFound)
        }
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }

    companion object {
        const val remindersNotFound = "Reminder not found"
    }


}