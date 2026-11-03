package com.example.studentsfeesstructure.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsfeesstructure.data.Payment
import com.example.studentsfeesstructure.databinding.ItemPaymentBinding

class PaymentAdapter : ListAdapter<Payment, PaymentAdapter.VH>(Diff) {

    inner class VH(val b: ItemPaymentBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: VH, position: Int) {
        val p = getItem(position)
        h.b.tvAmount.text = money(p.amount)
        h.b.tvMeta.text = "${dateStr(p.date)} · ${p.method} · ${p.reference}"
    }

    object Diff : DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(a: Payment, b: Payment) = a.paymentId == b.paymentId
        override fun areContentsTheSame(a: Payment, b: Payment) = a == b
    }
}
