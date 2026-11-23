
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

    inner class VH(val b: ItemStudentBinding) :
        RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: VH, position: Int) {
        val s = getItem(position)

        h.b.tvInitials.text = initials(s.fullName)
        h.b.tvName.text = s.fullName
        h.b.tvInfo.text = "${s.admissionNo} · ${s.programme}"

        // Balance in green (cleared) or red (still owing), plus a status pill
        h.b.tvBalance.showBalance(s.balance)
        h.b.tvStatus.showStatus(FeeStatus.of(s.balance, s.totalPaid))

        h.b.root.setOnClickListener {
            onClick(s.studentId)
        }
    }

    object Diff : DiffUtil.ItemCallback<StudentBalance>() {
        override fun areItemsTheSame(a: StudentBalance, b: StudentBalance) =
            a.studentId == b.studentId

        override fun areContentsTheSame(a: StudentBalance, b: StudentBalance) =
            a == b
    }
}
