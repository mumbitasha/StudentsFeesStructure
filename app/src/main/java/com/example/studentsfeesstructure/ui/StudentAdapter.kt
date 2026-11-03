package com.example.studentsfeesstructure.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsfeesstructure.data.StudentBalance
import com.example.studentsfeesstructure.databinding.ItemStudentBinding

class StudentAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<StudentBalance, StudentAdapter.VH>(Diff) {

    inner class VH(val b: ItemStudentBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: VH, position: Int) {
        val s = getItem(position)
        h.b.tvName.text = s.fullName
        h.b.tvInfo.text = "${s.admissionNo} · ${s.programme}"
        val status = when {
            s.balance <= 0 -> "Cleared"
            s.totalPaid > 0 -> "Partial"
            else -> "Owing"
        }
        h.b.tvBalance.text = "Balance: ${money(s.balance)}  ($status)"
        h.b.root.setOnClickListener { onClick(s.studentId) }
    }

    object Diff : DiffUtil.ItemCallback<StudentBalance>() {
        override fun areItemsTheSame(a: StudentBalance, b: StudentBalance) = a.studentId == b.studentId
        override fun areContentsTheSame(a: StudentBalance, b: StudentBalance) = a == b
    }
}
