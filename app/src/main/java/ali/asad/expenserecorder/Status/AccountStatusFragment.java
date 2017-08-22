package ali.asad.expenserecorder.Status;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import ali.asad.expenserecorder.OnSwipeTouchListener;
import ali.asad.expenserecorder.R;

import static ali.asad.expenserecorder.Status.StatusTabFragment.adpMonth;
import static ali.asad.expenserecorder.Status.StatusTabFragment.adpYear;

/**
 * Created by AsadAli on 03-Aug-17.
 */

public class AccountStatusFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public static TextView StartingBalance, Income, TotalExpenses, RunningBalance, Savings, Budget;
    public static Spinner month, year;
    String months[] = {"nothing","January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.status_account_fragment, container, false);
        System.out.println("Account Status Open");

        StartingBalance = (TextView) rootView.findViewById(R.id.startingBalance);
        Income = (TextView) rootView.findViewById(R.id.income);
        TotalExpenses = (TextView) rootView.findViewById(R.id.expenses);
        RunningBalance = (TextView) rootView.findViewById(R.id.runningBalance);
        Savings = (TextView) rootView.findViewById(R.id.savings);
        Budget = (TextView) rootView.findViewById(R.id.budgets);
        month = (Spinner) rootView.findViewById(R.id.monthSpinner);
        year = (Spinner) rootView.findViewById(R.id.yearSpinner);

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);//as jan=0 feb=1 mar=2 ...

        month.setAdapter(adpMonth);
        year.setAdapter(adpYear);
        month.setOnItemSelectedListener(this);
        month.setSelection(currentMonth);
        year.setOnItemSelectedListener(this);
        rootView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
                if (month.getSelectedItemPosition() + 1 < months.length-1) {
                    int newPosition = month.getSelectedItemPosition() + 1;
                    month.setSelection(newPosition);
                }
                else
                    month.setSelection(0);
            }
            public void onSwipeBottom() {
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

    public static void SetValuesToTextViews
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
        AccountStatusFragment.SetValuesToTextViews(dbHome.getStarting(Month, Year), dbHome.getRunning(Month, Year),
                dbHome.getIncomes(Month, Year), dbHome.getExpenses(Month, Year),
                dbHome.getSavings(Month, Year), dbHome.getBudget(Month, Year));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
