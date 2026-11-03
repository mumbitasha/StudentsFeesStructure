package com.example.studentsfeesstructure.data

/** Result of the JOIN query: a student plus how much they have paid so far. */
data class StudentBalance(
    val studentId: Int,
    val admissionNo: String,
    val fullName: String,
    val programme: String,
    val feeCharged: Double,
    val totalPaid: Double
) {
    val balance: Double get() = feeCharged - totalPaid
}
