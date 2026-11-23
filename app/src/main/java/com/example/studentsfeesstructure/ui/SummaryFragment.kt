package com.example.studentsfeesstructure.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.studentsfeesstructure.R
import com.example.studentsfeesstructure.databinding.FragmentSummaryBinding

/** Summary tab: collection rate, how many students are cleared/partial/owing, and totals. */
class SummaryFragment : Fragment() {
    private var _b: FragmentSummaryBinding? = null
    private val b get() = _b!!
    private val vm: FeeViewModel by activityViewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSummaryBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.allStudents.observe(viewLifecycleOwner) { list ->
            val charged = list.sumOf { it.feeCharged }
            val paid = list.sumOf { it.totalPaid }
            val rate = if (list.isEmpty()) 0 else percentPaid(charged, paid)

            b.tvRate.text = "$rate%"
            b.progressRate.setProgressCompat(rate, true)
            b.tvRateCaption.text = getString(R.string.collected_of, money(paid), money(charged))

            val statuses = list.map { FeeStatus.of(it.balance, it.totalPaid) }
            b.tvClearedCount.text = statuses.count { it == FeeStatus.CLEARED }.toString()
            b.tvPartialCount.text = statuses.count { it == FeeStatus.PARTIAL }.toString()
            b.tvOwingCount.text = statuses.count { it == FeeStatus.OWING }.toString()

            b.tvTotalStudents.text = list.size.toString()
            b.tvTotalCharged.text = money(charged)
            b.tvTotalCollected.text = money(paid)
            b.tvTotalOutstanding.text = money(charged - paid)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
