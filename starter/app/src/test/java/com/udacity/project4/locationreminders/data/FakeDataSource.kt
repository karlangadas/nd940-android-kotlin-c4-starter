package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>?) : ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(remindersNotFound)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
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
        private const val remindersNotFound = "Reminder not found"
    }


}