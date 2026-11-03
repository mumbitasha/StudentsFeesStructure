package com.example.studentsfeesstructure.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val studentId: Int = 0,
    val admissionNo: String,
    val fullName: String,
    val phone: String,
    val programme: String,
    val feeCharged: Double
)
