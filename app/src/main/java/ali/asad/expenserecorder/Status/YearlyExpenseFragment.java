package ali.asad.expenserecorder.Status;

import android.graphics.Color;
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

import ali.asad.expenserecorder.Categories.CategoryDBhelper;
import ali.asad.expenserecorder.Expenses.ExpenseDBhelper;
import ali.asad.expenserecorder.R;

import static ali.asad.expenserecorder.Status.StatusTabFragment.adpYear;

/**
 * Created by AsadAli on 24-Aug-17.
 */

public class YearlyExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String months[] = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};
    PieChart pieChart;
    public static Spinner year;
    boolean onCategory = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.status_yearly_expense_fragment, container, false);
        System.out.println("Expense Summary Open");

        pieChart = (PieChart) rootView.findViewById(R.id.pieChartExpense);
        year = (Spinner) rootView.findViewById(R.id.yearSpinnerSummaryExpense);
        year.setAdapter(adpYear);
        year.setOnItemSelectedListener(this);

        pieChart.animateY(1000);
        pieChart.getLegend().setTextSize(12f);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(35f);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(false);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int position = (int)h.getX();
                int Amount = (int)h.getY();

                if(!onCategory){
                    String Month = months[position];
                    System.out.println("Month: " + Month);
                    System.out.println("Value: " + Amount);
                }
                else {
                    ExpenseDBhelper db = new ExpenseDBhelper(getActivity());
                    List<String> list = db.getCategoriesOfAllEntries();
                    String Category = list.get(position);
                    setUpPieChartForMonths(Category);

                    pieChart.highlightValue(null);
                    onCategory=false;
                }
            }

            @Override
            public void onNothingSelected() {
                if(!onCategory) {
                    setUpPieChartForCategories();
                    onCategory= true;
                }
            }
        });
        return rootView;
    }

    private void setUpPieChartForMonths(String Category) {

        ExpenseDBhelper db = new ExpenseDBhelper(getActivity());
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < months.length; i++) {
            String Val;
            if ((i + 1) < 10)
                Val = "0" + (i + 1);
            else
                Val = "" + (i + 1);

            int Expense = db.getExpensesOf(Category,Val, "" + year.getSelectedItem());
            if (Expense != 0) {
                PieEntry entry = new PieEntry(Expense, months[i]);
                pieEntries.add(entry);
            }
        }

        PieDataSet dataset = new PieDataSet(pieEntries, "Expenses in "+Category
                +" for all the Months of Year " + year.getSelectedItem());
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(13f);
        dataset.setSliceSpace(2);

        PieData data = new PieData(dataset);
        pieChart.setData(data);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(Category);
        pieChart.setCenterTextSize(15);
        pieChart.animateY(1000);

        pieChart.invalidate();
    }

    public void setUpPieChartForCategories() {
        ExpenseDBhelper db = new ExpenseDBhelper(getActivity());
        List<String> list = db.getCategoriesOfAllEntries();

        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int Expense=db.getExpensesOfCategory(list.get(i));
            if (Expense != 0) {
                PieEntry entry = new PieEntry(Expense, list.get(i));
                pieEntries.add(entry);
            }
        }
        PieDataSet dataset = new PieDataSet(pieEntries, "Expenses for all Categories of Year " + year.getSelectedItem());
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(13f);
        dataset.setSliceSpace(2);

        PieData data = new PieData(dataset);
        pieChart.setData(data);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("All\nCategories");
        pieChart.setCenterTextSize(15);
        pieChart.animateY(1000);

        pieChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setUpPieChartForCategories();
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
