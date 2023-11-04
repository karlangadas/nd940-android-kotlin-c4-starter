package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var dataSource: FakeDataSource
    private val reminders = mutableListOf<ReminderDTO>()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        stopKoin()
        // Given a fresh ViewModel
        dataSource = FakeDataSource(reminders)
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @Test
    fun validateEnteredData_validData() {
        // WHEN reminder is valid
        val reminder = ReminderDataItem(
            "Reminder",
            "Description",
            "Location 1",
            25.0,
            50.0
        )
        // THEN reminder is validated
        assertThat(viewModel.validateEnteredData(reminder)).isEqualTo(true)
    }

    @Test
    fun validateEnteredData_invalidData() {
        // WHEN reminder is valid
        val reminder = ReminderDataItem(
            "",
            "Description",
            "Location 1",
            25.0,
            50.0
        )
        // THEN reminder is invalidated
        assertThat(viewModel.validateEnteredData(reminder)).isEqualTo(false)
    }
}