package com.example.studentsfeesstructure.ui

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.studentsfeesstructure.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun money(v: Double): String = String.format(Locale.US, "KES %,.0f", v)
fun dateStr(t: Long): String = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(t))

/** Cleared (green), Partial (pink) or Owing (red), with the colours used to draw it. */
enum class FeeStatus(val label: String, val colorRes: Int, val bgRes: Int) {
    CLEARED("Cleared", R.color.status_cleared, R.color.status_cleared_bg),
    PARTIAL("Partial", R.color.status_partial, R.color.status_partial_bg),
    OWING("Owing", R.color.status_owing, R.color.status_owing_bg);

    companion object {
        fun of(balance: Double, totalPaid: Double) = when {
            balance <= 0 -> CLEARED
            totalPaid > 0 -> PARTIAL
            else -> OWING
        }
    }
}

/** Shows the status as a coloured pill. */
fun TextView.showStatus(status: FeeStatus) {
    text = status.label
    setTextColor(ContextCompat.getColor(context, status.colorRes))
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, status.bgRes))
}

/** Balance text: green when nothing is owed, red when money is still owed. */
fun TextView.showBalance(balance: Double) {
    text = money(balance)
    val color = if (balance <= 0) R.color.status_cleared else R.color.status_owing
    setTextColor(ContextCompat.getColor(context, color))
}

/** "Amina Otieno" -> "AO" */
fun initials(name: String): String =
    name.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }

/** Percent paid, 0..100. */
fun percentPaid(charged: Double, paid: Double): Int =
    if (charged <= 0) 100 else (paid / charged * 100).toInt().coerceIn(0, 100)
