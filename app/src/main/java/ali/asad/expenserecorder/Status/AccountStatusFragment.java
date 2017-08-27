package ali.asad.expenserecorder.Status;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ali.asad.expenserecorder.OnSwipeTouchListener;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 03-Aug-17.
 */

public class AccountStatusFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public TextView StartingBalance, Income, TotalExpenses, RunningBalance, Savings, Budget;
    public Spinner month, year;
    String months[] = {"nothing","January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    List<String> listMonth;
    List<String> listYear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.status_account_fragment, container, false);
        System.out.println("Account Status Opened");

        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.main_status_view);
        StartingBalance = (TextView) rootView.findViewById(R.id.startingBalance);
        Income = (TextView) rootView.findViewById(R.id.income);
        TotalExpenses = (TextView) rootView.findViewById(R.id.expenses);
        RunningBalance = (TextView) rootView.findViewById(R.id.runningBalance);
        Savings = (TextView) rootView.findViewById(R.id.savings);
        Budget = (TextView) rootView.findViewById(R.id.budgets);
        month = (Spinner) rootView.findViewById(R.id.monthSpinner);
        year = (Spinner) rootView.findViewById(R.id.yearSpinner);

        listMonth = new ArrayList<String>();
        listYear = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        listYear.add("" + currentYear);
        for (int i = 1; i < months.length; i++) {
            listMonth.add(months[i]);
        }

        StatusDBhelper db=new StatusDBhelper(getActivity());
        db.deleteEmptyRows();
        List<String> yearList =db.getAllYears();
        for (int i = 0; i < yearList.size(); i++) {
            String year= yearList.get(i);
            if (YearNotAlready(year))
                listYear.add(year);
        }

        ArrayAdapter<String> adpMonth = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_layout_text, listMonth);
        ArrayAdapter<String> adpYear = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_layout_text, listYear);


        month.setAdapter(adpMonth);
        year.setAdapter(adpYear);
        month.setOnItemSelectedListener(this);
        month.setSelection(currentMonth);
        year.setOnItemSelectedListener(this);

        rootView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {
                linearLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.swipe_to_left));
                if (month.getSelectedItemPosition() + 1 < months.length-1) {
                    int newPosition = month.getSelectedItemPosition() + 1;
                    month.setSelection(newPosition);
                }
                else
                    month.setSelection(0);

            }
            public void onSwipeRight() {
                linearLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.swipe_to_right));
                if (month.getSelectedItemPosition() - 1 >= 0) {
                    int newPosition = month.getSelectedItemPosition() - 1;
                    month.setSelection(newPosition);
                }
                else
                    month.setSelection(months.length-2);
            }
        });

        return rootView;
    }

    public void SetValuesToTextViews
            (int starting, int running, int incomes, int expenses, int savings, int budget) {
        StartingBalance.setText("" + starting);
        Income.setText("" + incomes);
        TotalExpenses.setText("" + expenses);
        RunningBalance.setText("" + running);
        Savings.setText("" + savings);
        Budget.setText("" + budget);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String Month = GetMonthNumberFromMonth(month.getSelectedItem().toString());
        String Year = year.getSelectedItem().toString();

        StatusDBhelper dbHome = new StatusDBhelper(getActivity());

        //refreshing the fields in AccountStatusFragment
        SetValuesToTextViews(dbHome.getStarting(Month, Year), dbHome.getRunning(Month, Year),
                dbHome.getIncomes(Month, Year), dbHome.getExpenses(Month, Year),
                dbHome.getSavings(Month, Year), dbHome.getBudget(Month, Year));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean YearNotAlready(String year) {
        for (int i = 0; i < listYear.size(); i++) {
            if (listYear.get(i).equals(year))
                return false;
        }
        return true;
    }

    public String GetMonthNumberFromMonth(String month) {
        String monthNum = "";
        for (int i = 0; i < months.length; i++) {
            if (month == months[i]) {
                if (i < 10)
                    monthNum = "0" + i;
                else
                    monthNum = "" + i;
            }
        }
        return monthNum;
    }
}
