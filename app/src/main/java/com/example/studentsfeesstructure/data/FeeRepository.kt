package com.example.studentsfeesstructure.data

import kotlinx.coroutines.flow.Flow

class FeeRepository(private val students: StudentDao, private val payments: PaymentDao) {
    fun balances(q: String): Flow<List<StudentBalance>> = students.getBalances(q)
    fun student(id: Int): Flow<Student?> = students.getById(id)
    fun payments(studentId: Int): Flow<List<Payment>> = payments.getForStudent(studentId)

    suspend fun add(s: Student) = students.insert(s)
    suspend fun update(s: Student) = students.update(s)
    suspend fun delete(s: Student) = students.delete(s)
    suspend fun addPayment(p: Payment) = payments.insert(p)
}
