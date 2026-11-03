package com.example.studentsfeesstructure.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studentsfeesstructure.data.Student
import com.example.studentsfeesstructure.databinding.FragmentStudentFormBinding

class StudentFormFragment : Fragment() {
    private var _b: FragmentStudentFormBinding? = null
    private val b get() = _b!!
    private val vm: FeeViewModel by activityViewModels()
    private val studentId get() = requireArguments().getInt("studentId", 0)
    private var loaded = false

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentStudentFormBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (studentId != 0) {
            vm.student(studentId).observe(viewLifecycleOwner) { s ->
                if (s != null && !loaded) {
                    loaded = true
                    b.etAdmission.setText(s.admissionNo)
                    b.etName.setText(s.fullName)
                    b.etPhone.setText(s.phone)
                    b.etProgramme.setText(s.programme)
                    b.etFee.setText(s.feeCharged.toString())
                }
            }
        }

        b.btnSave.setOnClickListener {
            val adm = b.etAdmission.text.toString().trim()
            val name = b.etName.text.toString().trim()
            val phone = b.etPhone.text.toString().trim()
            val prog = b.etProgramme.text.toString().trim()
            val fee = b.etFee.text.toString().trim().toDoubleOrNull()

            when {
                adm.isEmpty() -> b.etAdmission.error = "Required"
                name.isEmpty() -> b.etName.error = "Required"
                prog.isEmpty() -> b.etProgramme.error = "Required"
                fee == null || fee < 0 -> b.etFee.error = "Enter a valid amount"
                else -> {
                    vm.save(Student(studentId, adm, name, phone, prog, fee))
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
