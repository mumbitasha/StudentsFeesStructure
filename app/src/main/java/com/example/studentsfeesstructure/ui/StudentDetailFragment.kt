package com.example.studentsfeesstructure.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentsfeesstructure.R
import com.example.studentsfeesstructure.data.Payment
import com.example.studentsfeesstructure.data.Student
import com.example.studentsfeesstructure.databinding.DialogPaymentBinding
import com.example.studentsfeesstructure.databinding.FragmentStudentDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StudentDetailFragment : Fragment() {
    private var _b: FragmentStudentDetailBinding? = null
    private val b get() = _b!!
    private val vm: FeeViewModel by activityViewModels()
    private val studentId get() = requireArguments().getInt("studentId")

    private var student: Student? = null
    private var totalPaid = 0.0

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentStudentDetailBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PaymentAdapter { p -> showPaymentOptions(p) }
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter

        vm.student(studentId).observe(viewLifecycleOwner) { s ->
            student = s
            if (s != null) {
                b.tvName.text = s.fullName
                b.tvDetails.text = "${s.admissionNo} · ${s.programme}\nPhone: ${s.phone}"
                refreshSummary()
            }
        }

        vm.payments(studentId).observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            totalPaid = list.sumOf { it.amount }
            refreshSummary()
        }

        b.btnPay.setOnClickListener { showPaymentDialog() }
        b.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.action_detail_to_form, bundleOf("studentId" to studentId))
        }
        b.btnDelete.setOnClickListener {
            val s = student ?: return@setOnClickListener
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete student?")
                .setMessage("This also deletes all their payment records.")
                .setPositiveButton("Delete") { _, _ ->
                    vm.delete(s)
                    findNavController().popBackStack()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun refreshSummary() {
        val s = student ?: return
        b.tvCharged.text = "Fee charged: ${money(s.feeCharged)}"
        b.tvPaid.text = "Total paid: ${money(totalPaid)}"
        b.tvBalance.text = "Balance: ${money(s.feeCharged - totalPaid)}"
    }

    private fun showPaymentDialog(existing: Payment? = null) {
        val d = DialogPaymentBinding.inflate(layoutInflater)
        if (existing != null) {
            d.etAmount.setText(existing.amount.toString())
            d.etMethod.setText(existing.method)
            d.etReference.setText(existing.reference)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "Record payment" else "Edit payment")
            .setView(d.root)
            .setPositiveButton("Save") { _, _ ->
                val amount = d.etAmount.text.toString().toDoubleOrNull()
                if (amount == null || amount <= 0) return@setPositiveButton
                val method = d.etMethod.text.toString().ifBlank { "Cash" }
                val ref = d.etReference.text.toString().ifBlank { "-" }
                if (existing == null) {
                    vm.addPayment(Payment(studentId = studentId, amount = amount,
                        date = System.currentTimeMillis(), method = method, reference = ref))
                } else {
                    vm.updatePayment(existing.copy(amount = amount, method = method, reference = ref))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPaymentOptions(p: Payment) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(money(p.amount))
            .setItems(arrayOf("Edit", "Delete")) { _, which ->
                if (which == 0) showPaymentDialog(p) else confirmDeletePayment(p)
            }
            .show()
    }

    private fun confirmDeletePayment(p: Payment) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete payment?")
            .setPositiveButton("Delete") { _, _ -> vm.deletePayment(p) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
