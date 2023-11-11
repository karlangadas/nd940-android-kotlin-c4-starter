package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO> = mutableListOf()) : ReminderDataSource {

    var shouldReturnError = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if(shouldReturnError){
            return Result.Error(exceptionString)
        }
        return Result.Success(ArrayList(reminders))
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error(exceptionString)
        }
        val reminder = reminders.find{ it.id == id }
        return if (reminder != null){
            Result.Success(reminder)
        }
        else {
            Result.Error(reminderNotFound)
        }
    }

    override suspend fun deleteAllReminders() {
        reminders.clear()
    }

    companion object {
        const val reminderNotFound = "Reminder not found!"
        const val exceptionString = "Exception thrown while retrieving the data"
    }


}