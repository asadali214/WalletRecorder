package ali.asad.expenserecorder.Status;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ali.asad.expenserecorder.R;

import static ali.asad.expenserecorder.Status.StatusTabFragment.adpYear;

/**
 * Created by AsadAli on 24-Aug-17.
 */

public class YearlyIncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    String months[] = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};
    PieChart pieChart;
    public static Spinner year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.status_yearly_income_fragment,container,false);
        System.out.println("Income Summary Open");

        pieChart = (PieChart) rootView.findViewById(R.id.pieChartIncome);
        year = (Spinner) rootView.findViewById(R.id.yearSpinnerSummaryIncome);
        year.setAdapter(adpYear);
        year.setOnItemSelectedListener(this);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int position = (int)h.getX();
                int Amount = (int)h.getY();
                String Month = months[position];
                System.out.println("Month: "+Month);
                System.out.println("Value: "+Amount);

            }

            @Override
            public void onNothingSelected() {

            }
        });

        return rootView;
    }
    private void setUpPieChart() {

        StatusDBhelper db = new StatusDBhelper(getActivity());
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < months.length; i++) {
            String ValMonth;
            if ((i+1) < 10)
                ValMonth = "0" + (i+1);
            else
                ValMonth = "" + (i+1);

            int Incomes= db.getIncomes(ValMonth,""+year.getSelectedItem());
            if(Incomes!=0) {
                PieEntry entry = new PieEntry(Incomes, months[i]);
                pieEntries.add(entry);
            }
        }

        PieDataSet dataset = new PieDataSet(pieEntries, "Incomes of Year "+year.getSelectedItem());
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setValueTextSize(13f);
        dataset.setSliceSpace(2);

        pieChart.getLegend().setTextSize(12f);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        PieData data = new PieData(dataset);
        pieChart.setData(data);
        pieChart.animateY(1000);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(35f);
        Description description=  new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(false);

        pieChart.invalidate();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setUpPieChart();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
