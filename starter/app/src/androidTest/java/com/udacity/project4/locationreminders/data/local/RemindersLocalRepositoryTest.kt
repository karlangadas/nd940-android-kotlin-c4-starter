package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var remindersDatabase: RemindersDatabase

    @Before
    fun initDB(){
        remindersDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java).allowMainThreadQueries().build()
        remindersLocalRepository =
            RemindersLocalRepository(
                remindersDatabase.reminderDao(),
                Dispatchers.Main
            )
    }

    @After
    fun closeDB(){
        remindersDatabase.close()
    }

    @Test fun saveReminder_retrievesReminder() = runTest {

        // GIVEN - A new task saved in the database.
        val newReminder =  ReminderDTO("Reminder 1", "description 1", "location 1", 15.0, 15.0)
        remindersLocalRepository.saveReminder(newReminder)

        // WHEN  - Task retrieved by ID.
        val result = remindersLocalRepository.getReminder(newReminder.id)

        // THEN - Success results with same reminder is returned.
        Truth.assertThat(result is Result.Success).isNotNull()
        val data = (result as Result.Success).data
        Truth.assertThat(data.id).isEqualTo(newReminder.id)
        Truth.assertThat(data.title).isEqualTo(newReminder.title)
        Truth.assertThat(data.description).isEqualTo(newReminder.description)
        Truth.assertThat(data.location).isEqualTo(newReminder.location)
        Truth.assertThat(data.latitude).isEqualTo(newReminder.latitude)
        Truth.assertThat(data.longitude).isEqualTo(newReminder.longitude)
    }

    @Test fun emptyRepository_retrievesError() = runTest {

        // GIVEN - Aan empty repository
        remindersLocalRepository.deleteAllReminders()

        // WHEN  - Task retrieved by ID.
        val result = remindersLocalRepository.getReminder("a")

        // THEN - Error results is returned.
        Truth.assertThat(result is Result.Error).isNotNull()
    }

}