package ali.asad.expenserecorder.Status;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ali.asad.expenserecorder.R;

import static ali.asad.expenserecorder.Status.StatusTabFragment.adpYear;

/**
 * Created by AsadAli on 03-Aug-17.
 */

public class YearlySummaryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String monthsLables[] = {"January", "↑", "", "February", "↑", "", "March", "↑", "", "April", "↑", "", "May"
            , "↑", "", "June", "↑", "", "July", "↑", "", "August", "↑", "", "September", "↑", "",
            "October", "↑", "", "November", "↑", "", "December", "↑", ""};
    BarChart barChart;
    public static Spinner year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.status_yearly_fragment, container, false);
        System.out.println("Yearly Summary Open");

        barChart = (BarChart) rootView.findViewById(R.id.barChart);
        year = (Spinner) rootView.findViewById(R.id.yearSpinnerSummary);
        year.setAdapter(adpYear);
        year.setOnItemSelectedListener(this);
        barChart.zoom(6, 1, 0, 0);

        return rootView;
    }

    private void setUpBarChart() {

        StatusDBhelper db = new StatusDBhelper(getActivity());
        List<BarEntry> barEntriesExpenses = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String Val;
            if ((i + 1) < 10)
                Val = "0" + (i + 1);
            else
                Val = "" + (i + 1);
            int Expense = db.getExpenses(Val, "" + year.getSelectedItem());
            BarEntry entry = new BarEntry((i * 3), Expense);
            barEntriesExpenses.add(entry);
        }
        List<BarEntry> barEntriesIncomes = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String MonthVal;
            if ((i + 1) < 10)
                MonthVal = "0" + (i + 1);
            else
                MonthVal = "" + (i + 1);
            int Incomes = db.getIncomes(MonthVal, "" + year.getSelectedItem());
            BarEntry entry = new BarEntry((1 + (i * 3)), Incomes);
            barEntriesIncomes.add(entry);
        }

        BarDataSet dataSetExpense = new BarDataSet(barEntriesExpenses, "Expenses of " + year.getSelectedItem());
        dataSetExpense.setColors(Color.RED);
        dataSetExpense.setValueTextSize(12f);

        BarDataSet dataSetIncomes = new BarDataSet(barEntriesIncomes, "Incomes of " + year.getSelectedItem());
        dataSetIncomes.setColors(Color.GREEN);
        dataSetIncomes.setValueTextSize(12f);

        BarData data = new BarData(dataSetExpense, dataSetIncomes);

        barChart.getLegend().setTextSize(12f);
        barChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        barChart.setData(data);
        barChart.animateY(1000);
        barChart.setFitBars(true);

        barChart.setBackgroundColor(Color.WHITE);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.setScaleEnabled(false);
        XAxis xval = barChart.getXAxis();
        xval.setPosition(XAxis.XAxisPosition.BOTTOM);
        xval.setDrawLabels(true);
        xval.setTextSize(12f);
        xval.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return monthsLables[Math.round(value)];
            }
        });
        xval.setDrawGridLines(false);
        YAxis yRight = barChart.getAxisRight();
        yRight.setEnabled(false);

        Calendar cal = Calendar.getInstance();
        barChart.moveViewToX((cal.get(Calendar.MONTH) * 3)-1);

        barChart.invalidate();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setUpBarChart();
        //YearlyRunningFragment.year.setSelection(year.getSelectedItemPosition());
        //YearlyExpenseFragment.year.setSelection(year.getSelectedItemPosition());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
