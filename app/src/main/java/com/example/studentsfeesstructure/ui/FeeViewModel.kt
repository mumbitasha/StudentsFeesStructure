package com.example.studentsfeesstructure.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.studentsfeesstructure.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Holds screen state and talks to the repository. Survives rotation.
 * All three fragments share this one instance through activityViewModels().
 * Room's Flows are turned into LiveData with asLiveData(), so screens update by themselves.
 */
class FeeViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val repo = FeeRepository(db.studentDao(), db.paymentDao())

    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val students: LiveData<List<StudentBalance>> =
        query.flatMapLatest { repo.balances(it) }.asLiveData()

    /** Every student, ignoring the search box. Used by the Summary tab. */
    val allStudents: LiveData<List<StudentBalance>> = repo.balances("").asLiveData()

    fun setQuery(q: String) { query.value = q }
    fun student(id: Int): LiveData<Student?> = repo.student(id).asLiveData()
    fun payments(id: Int): LiveData<List<Payment>> = repo.payments(id).asLiveData()

    fun save(s: Student) = viewModelScope.launch {
        if (s.studentId == 0) repo.add(s) else repo.update(s)
    }
    fun delete(s: Student) = viewModelScope.launch { repo.delete(s) }
    fun addPayment(p: Payment) = viewModelScope.launch { repo.addPayment(p) }
    fun updatePayment(p: Payment) = viewModelScope.launch { repo.updatePayment(p) }
    fun deletePayment(p: Payment) = viewModelScope.launch { repo.deletePayment(p) }
}
