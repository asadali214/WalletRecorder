package ali.asad.expenserecorder.Expenses;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class ExpenseViewHolderCat extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView name, amount;
    ImageView icon;
    RecyclerView recyclerView;
    Activity activity;
    View view;
    boolean isShowing;


    public ExpenseViewHolderCat(View convertView) {
        super(convertView);
        view = convertView;
        name = (TextView) convertView.findViewById(R.id.nameExpenseByCat);
        amount = (TextView) convertView.findViewById(R.id.amountExpenseByCat);
        icon = (ImageView) convertView.findViewById(R.id.iconExpenseByCat);
        recyclerView = (RecyclerView) convertView.findViewById(R.id.itemExpenseCat);
        isShowing = false;

        convertView.setOnTouchListener(ExpenseShowFragment.expenseSwipeListener);
        convertView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        List<String> list = new ArrayList<>();
        if (isShowing) {
            System.out.println("Clicked on view holder in catExpense EMPTY");
            isShowing = false;
            //if we clicked on a view which is already being shown
            //make adapter with empty list
            ExpenseRecyclerViewAdapterItem adapter = new ExpenseRecyclerViewAdapterItem(activity, list, this);
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        } else {

            isShowing = true;
            //fill the list..
            String category = name.getText().toString();
            String Category = category.substring(0, category.length() - 1);
            String Year = ExpenseShowFragment.year.getSelectedItem().toString();
            String Month;
            int month = ExpenseShowFragment.month.getSelectedItemPosition();
            if (month < 10)
                Month = "0" + month;
            else
                Month = "" + month;
            ExpenseDBhelper db = new ExpenseDBhelper(activity);
            if (!Month.equals("00")) {
                List<String[]> listExpenses = db.getAllEntriesIn(Category, Month, Year);
                for (int i = 0; i < listExpenses.size(); i++) {
                    String details = listExpenses.get(i)[2];
                    String amount = listExpenses.get(i)[3];
                    list.add(details + ":     " + amount);
                }
            } else {
                List<String[]> listExpenses = db.getAllEntriesIn(Category, Year);
                for (int i = 0; i < listExpenses.size(); i++) {
                    String details = listExpenses.get(i)[2];
                    String amount = listExpenses.get(i)[3];
                    list.add(details + ":     " + amount);
                }
            }
            ExpenseRecyclerViewAdapterItem adapter = new ExpenseRecyclerViewAdapterItem(activity, list, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        }
    }
}
