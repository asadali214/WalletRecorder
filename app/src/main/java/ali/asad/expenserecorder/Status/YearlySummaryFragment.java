package ali.asad.expenserecorder.Status;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ali.asad.expenserecorder.R;

import static ali.asad.expenserecorder.Status.StatusTabFragment.adpYear;

/**
 * Created by AsadAli on 03-Aug-17.
 */

public class YearlySummaryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String months[] = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};
    PieChart pieChart;
    public static Spinner year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.status_yearly_fragment, container, false);
        System.out.println("Yearly Summary Open");

        pieChart = (PieChart) rootView.findViewById(R.id.pieChart);
        year = (Spinner) rootView.findViewById(R.id.yearSpinnerSummary);
        year.setAdapter(adpYear);
        year.setOnItemSelectedListener(this);

        return rootView;
    }

    private void setUpPieChart() {
        StatusDBhelper db = new StatusDBhelper(getActivity());
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < months.length; i++) {
            String Val;
            if ((i+1) < 10)
                Val = "0" + (i+1);
            else
                Val = "" + (i+1);

            pieEntries.add(new PieEntry(db.getExpenses(Val,""+year.getSelectedItem()),months[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntries, "Expenses of Year "+year.getSelectedItem());
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataset);
        pieChart.setData(data);
        pieChart.animateY(1000);
        pieChart.invalidate();
        pieChart.setRotationAngle(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setUpPieChart();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
