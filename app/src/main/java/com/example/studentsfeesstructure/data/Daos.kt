package com.example.studentsfeesstructure.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert suspend fun insert(student: Student)
    @Update suspend fun update(student: Student)
    @Delete suspend fun delete(student: Student)

    @Query("SELECT * FROM students WHERE studentId = :id")
    fun getById(id: Int): Flow<Student?>

    @Query(
        """SELECT s.studentId, s.admissionNo, s.fullName, s.programme, s.feeCharged,
                  COALESCE(SUM(p.amount), 0) AS totalPaid
           FROM students s LEFT JOIN payments p ON p.studentId = s.studentId
           WHERE s.fullName LIKE '%' || :q || '%' OR s.admissionNo LIKE '%' || :q || '%'
           GROUP BY s.studentId
           ORDER BY s.fullName"""
    )
    fun getBalances(q: String): Flow<List<StudentBalance>>
}

@Dao
interface PaymentDao {
    @Insert suspend fun insert(payment: Payment)

    @Query("SELECT * FROM payments WHERE studentId = :id ORDER BY date DESC")
    fun getForStudent(id: Int): Flow<List<Payment>>
}
