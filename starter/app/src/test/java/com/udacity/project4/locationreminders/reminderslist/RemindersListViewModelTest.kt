package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.FakeDataSource.Companion.remindersNotFound
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var viewModel: RemindersListViewModel
    private lateinit var dataSource: FakeDataSource
    private val reminders = mutableListOf<ReminderDTO>()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // Given a fresh ViewModel
        dataSource = FakeDataSource(reminders)
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @Test
    fun loadReminders_notEmptyList() =
        runTest {
            val reminder1 = ReminderDTO("Reminder 1", "description 1", "location 1", 15.0, 15.0)
            val reminder2 = ReminderDTO("Reminder 2", "description 2", "location 2", 20.0, 20.0)
            reminders.add(reminder1)
            reminders.add(reminder2)

            // When loading reminders
            viewModel.loadReminders()
            assertThat(viewModel.showLoading.value).isEqualTo(true)

            advanceUntilIdle()
            assertThat(viewModel.showLoading.value).isEqualTo(false)
            assertThat(viewModel.showNoData.value).isEqualTo(false)
        }

    @Test
    fun loadReminders_emptyList() =
        runTest {
            dataSource.deleteAllReminders()
            // When loading reminders
            viewModel.loadReminders()
            assertThat(viewModel.showLoading.value).isEqualTo(true)

            advanceUntilIdle()
            assertThat(viewModel.showLoading.value).isEqualTo(false)
            assertThat(viewModel.showNoData.value).isEqualTo(true)
        }

    @Test
    fun loadReminders_showError() =
        runTest {
            dataSource.shouldReturnError = true
            // When loading reminders
            viewModel.loadReminders()
            assertThat(viewModel.showLoading.value).isEqualTo(true)

            advanceUntilIdle()
            assertThat(viewModel.showLoading.value).isEqualTo(false)
            assertThat(viewModel.showSnackBar.value).isEqualTo(remindersNotFound)
        }

}