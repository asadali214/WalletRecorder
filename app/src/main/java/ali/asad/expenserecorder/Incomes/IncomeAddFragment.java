package ali.asad.expenserecorder.Incomes;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import ali.asad.expenserecorder.Expenses.ExpenseShowFragment;
import ali.asad.expenserecorder.Status.StatusDBhelper;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 12-Jul-17.
 */

public class IncomeAddFragment extends Fragment {
    public static EditText date;
    EditText detail, amount;//income table entries

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.income_fragment, container, false);
        getActivity().setTitle("Add Incomes");
        date = (EditText) rootView.findViewById(R.id.ETdateIncome);
        detail = (EditText) rootView.findViewById(R.id.ETdetailIncome);
        amount = (EditText) rootView.findViewById(R.id.ETamountIncome);

        date.setEnabled(false);

        System.out.println("IncomeAddFragment Created");

        return rootView;
    }

    public boolean onGoIncome() {
        Calendar cal = Calendar.getInstance();
        int monthS = cal.get(Calendar.MONTH)+1;
        int yearS = cal.get(Calendar.YEAR);
        //checks on edit texts shouldn't be empty
        //date check
        //amount check
        xyz:
        {
            if (!fieldCheck()) {
                System.out.println("Fields: " + fieldCheck());
                Toast.makeText(getActivity().getApplicationContext(), "Please insert correct data", Toast.LENGTH_SHORT).show();
                break xyz;
            }
            String Date = date.getText().toString();
            String Detail = detail.getText().toString();
            String Amount = amount.getText().toString();
            IncomeDBhelper db = new IncomeDBhelper(getActivity());
            db.insertEntry(Date, Detail, Amount);

            StatusDBhelper dbHome = new StatusDBhelper(getActivity());
            String month = GetMonthNumber(Date);
            String year = GetYearNumber(Date);

            int starting;

            if(monthS == Integer.parseInt(month) && yearS == Integer.parseInt(year)) {
                //As the starting balance of current month can be changed by user
                starting = dbHome.getStarting(month, year);
            }
            else{
                //Get Running of previous month for the starting of this month if its not current month
                starting =dbHome.getRunning(getPrevMonth(month), getPrevYear(month,year));
            }
            int incomes = dbHome.getIncomes(month, year) + Integer.parseInt(amount.getText().toString());
            int expenses = dbHome.getExpenses(month, year);
            int running = starting + incomes - expenses;
            int savings = incomes - expenses;
            int budget = dbHome.getBudget(month, year);
            dbHome.insertOrAlterEntry(month, year, starting, running, incomes, expenses, savings, budget);

            IncomeShowFragment.currentMonth=Integer.parseInt(month);
            IncomeShowFragment.currentYear = Integer.parseInt(year);
            Toast.makeText(getActivity(), "Income added!", Toast.LENGTH_SHORT).show();
            detail.setText("");
            amount.setText("");
            return true;
        }
        detail.setText("");
        amount.setText("");
        return false;
    }


    public boolean fieldCheck() {
        if (date.getText().toString().equals(""))
            return false;
        if (detail.getText().toString().equals(""))
            return false;
        if (amount.getText().toString().equals(""))
            return false;

        return true;
    }

    public String getPrevMonth(String month) {
        String prevMonth;
        int monthNum = Integer.parseInt(month) - 1;
        if (monthNum == 0)
            prevMonth = "" + 12;
        else {
            if (monthNum < 10)
                prevMonth = "0" + monthNum;
            else
                prevMonth = "" + monthNum;
        }

        return prevMonth;
    }

    public String getPrevYear(String month, String year) {
        String prevYear;
        int yearNum = Integer.parseInt(year) - 1;
        int monthNum = Integer.parseInt(month) - 1;
        if (monthNum == 0)
            prevYear = "" + yearNum;
        else
            prevYear = year;
        return prevYear;
    }

    public String GetMonthNumber(String fullDate) {
        String month = "";
        for (int i = 0; i < fullDate.length(); i++) {
            if (fullDate.charAt(i) == '-') {
                break;
            }
            month += fullDate.charAt(i);
        }
        return month;
    }

    public String GetYearNumber(String fullDate) {
        int count = 0;
        String year = "";
        for (int i = 0; i < fullDate.length(); i++) {
            if (fullDate.charAt(i) == '-') {
                count++;
                continue;
            }
            if (count == 2) {
                year += fullDate.charAt(i);
            }
        }
        return year;
    }
}


