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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ali.asad.expenserecorder.R;

import static ali.asad.expenserecorder.Status.StatusTabFragment.adpYear;

/**
 * Created by AsadAli on 24-Aug-17.
 */

public class YearlyRunningFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String months[] = {"January", "", "February", "", "March", "", "April", "", "May", "",
            "June", "", "July", "", "August", "", "September", "", "October", "", "November", "", "December",""};
    LineChart lineChart;
    public static Spinner year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.status_yearly_running_fragment, container, false);
        System.out.println("Running Summary Open");

        lineChart = (LineChart) rootView.findViewById(R.id.lineChartIncome);
        year = (Spinner) rootView.findViewById(R.id.yearSpinnerSummaryIncome);
        year.setAdapter(adpYear);
        year.setOnItemSelectedListener(this);
        lineChart.zoom(3, 1, 0, 0);

        return rootView;
    }

    private void setUpLineChart() {

        StatusDBhelper db = new StatusDBhelper(getActivity());
        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String ValMonth;
            if ((i + 1) < 10)
                ValMonth = "0" + (i + 1);
            else
                ValMonth = "" + (i + 1);

            int Running = db.getRunning(ValMonth, "" + year.getSelectedItem());
            Entry entry = new Entry(i*2, Running);
            lineEntries.add(entry);
        }

        LineDataSet dataset = new LineDataSet(lineEntries, "Balance at the end of each Month in Year " + year.getSelectedItem());
        dataset.setColors(Color.BLUE);
        dataset.setValueTextSize(13f);

        lineChart.getLegend().setTextSize(12f);
        lineChart.getLegend().setForm(Legend.LegendForm.LINE);
        lineChart.getLegend().setWordWrapEnabled(true);
        LineData data = new LineData(dataset);
        lineChart.setData(data);

        lineChart.animateY(1000);
        lineChart.setScaleEnabled(false);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        Calendar cal = Calendar.getInstance();
        lineChart.moveViewToX((cal.get(Calendar.MONTH) * 2)-7);

        XAxis xval = lineChart.getXAxis();
        xval.setPosition(XAxis.XAxisPosition.BOTTOM);
        xval.setDrawLabels(true);
        xval.setTextSize(12f);
        xval.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[Math.round(value)];
            }
        });
        xval.setDrawGridLines(false);
        YAxis yRight = lineChart.getAxisRight();
        yRight.setDrawGridLines(false);
        YAxis yLeft = lineChart.getAxisLeft();
        yLeft.setDrawGridLines(false);


        lineChart.invalidate();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setUpLineChart();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
