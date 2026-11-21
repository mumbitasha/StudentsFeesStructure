package com.example.studentsfeesstructure.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "students", indices = [Index(value = ["admissionNo"], unique = true)])
data class Student(
    @PrimaryKey(autoGenerate = true) val studentId: Int = 0,
    val admissionNo: String,
    val fullName: String,
    val phone: String,
    val programme: String,
    val feeCharged: Double
)