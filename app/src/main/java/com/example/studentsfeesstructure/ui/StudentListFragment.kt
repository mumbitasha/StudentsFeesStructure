package com.example.studentsfeesstructure.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentsfeesstructure.R
import com.example.studentsfeesstructure.databinding.FragmentStudentListBinding

class StudentListFragment : Fragment() {
    private var _b: FragmentStudentListBinding? = null
    private val b get() = _b!!
    private val vm: FeeViewModel by activityViewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentStudentListBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = StudentAdapter { id ->
            findNavController().navigate(R.id.action_list_to_detail, bundleOf("studentId" to id))
        }
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter

        vm.students.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            val charged = list.sumOf { it.feeCharged }
            val paid = list.sumOf { it.totalPaid }
            b.tvCharged.text = "Charged: ${money(charged)}"
            b.tvPaid.text = "Collected: ${money(paid)}"
            b.tvOutstanding.text = "Outstanding: ${money(charged - paid)}"
            b.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        b.etSearch.doAfterTextChanged { vm.setQuery(it?.toString()?.trim().orEmpty()) }

        b.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_form, bundleOf("studentId" to 0))
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
