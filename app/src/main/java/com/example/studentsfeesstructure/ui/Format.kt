package com.example.studentsfeesstructure.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun money(v: Double): String = String.format(Locale.US, "KES %,.0f", v)
fun dateStr(t: Long): String = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(t))
