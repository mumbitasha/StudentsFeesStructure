package com.example.studentsfeesstructure.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payments",
    foreignKeys = [ForeignKey(
        entity = Student::class,
        parentColumns = ["studentId"],
        childColumns = ["studentId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("studentId")]
)
data class Payment(
    @PrimaryKey(autoGenerate = true) val paymentId: Int = 0,
    val studentId: Int,
    val amount: Double,
    val date: Long,
    val method: String,
    val reference: String
)
