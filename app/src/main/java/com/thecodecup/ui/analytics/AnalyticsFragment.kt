package com.thecodecup.ui.analytics

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.thecodecup.R
import com.thecodecup.data.local.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_analytics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        barChart = view.findViewById(R.id.barChart)
        pieChart = view.findViewById(R.id.pieChart)
        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lifecycleScope.launch {
            val allEntries = AppDatabase.getInstance(requireContext())
                .orderDao()
                .getOrdersByStatus("history")

            val uniqueOrderTimes = allEntries
                .map { it.timestamp }
                .distinct()

            val sdf = SimpleDateFormat("EEE", Locale.getDefault())
            val days = (6 downTo 0).map {
                Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -it) }
                    .time
            }.map { sdf.format(it) }

            val ordersPerDay = days.associateWith { 0 }.toMutableMap()

            uniqueOrderTimes.forEach { ts ->
                val dayLabel = sdf.format(Date(ts))
                if (dayLabel in ordersPerDay) {
                    ordersPerDay[dayLabel] = ordersPerDay[dayLabel]!! + 1
                }
            }

            val barEntries = ordersPerDay.entries.mapIndexed { index, entry ->
                BarEntry(index.toFloat(), entry.value.toFloat())
            }
            val barDataSet = BarDataSet(barEntries, "Orders per Day")
            barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
            barChart.data = BarData(barDataSet)

            barChart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(days)
                granularity = 1f
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
            }
            barChart.axisRight.isEnabled = false
            barChart.description.isEnabled = false
            barChart.invalidate()

            val rewardDao = AppDatabase.getInstance(requireContext()).rewardDao()
            val rewardHistory = rewardDao.getAll()
            val rewardMap = rewardHistory.groupingBy { it.rewardName }.eachCount()
            val pieEntries = rewardMap.map { PieEntry(it.value.toFloat(), it.key) }

            if (pieEntries.isNotEmpty()) {
                val pieDataSet = PieDataSet(pieEntries, "")
                pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
                pieChart.data = PieData(pieDataSet)
                pieChart.description.isEnabled = false
                pieChart.invalidate()
            }
        }
    }

}
