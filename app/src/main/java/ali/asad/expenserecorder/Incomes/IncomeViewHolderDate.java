package ali.asad.expenserecorder.Incomes;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ali.asad.expenserecorder.Expenses.ExpenseDBhelper;
import ali.asad.expenserecorder.Expenses.ExpenseRecyclerViewAdapterItem;
import ali.asad.expenserecorder.Expenses.ExpenseShowFragment;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class IncomeViewHolderDate extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView date, amount;
    RecyclerView recyclerView;
    Activity activity;
    boolean isShowing;

    public IncomeViewHolderDate(View convertView) {
        super(convertView);
        date = (TextView) convertView.findViewById(R.id.dateIncomeByDate);
        amount = (TextView) convertView.findViewById(R.id.amountIncomeByDate);
        recyclerView = (RecyclerView) convertView.findViewById(R.id.itemIncomeDate);
        isShowing=false;

        convertView.setOnClickListener(this);
        convertView.setOnTouchListener(IncomeShowFragment.incomeSwipeListener);
    }

    @Override
    public void onClick(View view) {
        List<String> list = new ArrayList<>();
        if (isShowing) {
            isShowing = false;
            //if we clicked on a view which is already being shown
            //make adapter with empty list
            IncomeRecyclerViewAdapterItem adapter = new IncomeRecyclerViewAdapterItem(activity, list, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        } else {

            isShowing = true;
            //fill the list..
            String day = date.getText().toString();
            String Day = day.substring(day.length() - 3, day.length() - 1);
            String Year = IncomeShowFragment.year.getSelectedItem().toString();
            String Month;
            int month = IncomeShowFragment.month.getSelectedItemPosition();
            if (month < 10)
                Month = "0" + month;
            else
                Month = "" + month;
            IncomeDBhelper db = new IncomeDBhelper(activity);
            if (!Month.equals("00")) {
                List<String[]> listIncomes = db.getAllEntriesOf(Month, Day, Year);
                for (int i = 0; i < listIncomes.size(); i++) {
                    String details = listIncomes.get(i)[2];
                    String amount = listIncomes.get(i)[3];
                    list.add(details + ":     " + amount);
                }
            } else {
                Month = GetMonthNumberFromMonth(day.substring(0, day.length() - 4));
                List<String[]> listIncomes = db.getAllEntriesOf(Month, Day, Year);
                for (int i = 0; i < listIncomes.size(); i++) {
                    String details = listIncomes.get(i)[2];
                    String amount = listIncomes.get(i)[3];
                    list.add(details + ":     " + amount);
                }
            }
            IncomeRecyclerViewAdapterItem adapter = new IncomeRecyclerViewAdapterItem(activity, list, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        }
    }

    public String GetMonthNumberFromMonth(String month) {
        String monthNum = "";
        String months[] = {"All", "January", "February", "March", "April",
                "May", "June", "July", "August", "September", "October",
                "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (month.equals(months[i])) {
                if (i < 10)
                    monthNum = "0" + i;
                else
                    monthNum = "" + i;
            }
        }
        return monthNum;
    }
}