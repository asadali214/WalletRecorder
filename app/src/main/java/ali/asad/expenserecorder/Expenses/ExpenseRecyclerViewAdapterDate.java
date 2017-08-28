package ali.asad.expenserecorder.Expenses;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class ExpenseRecyclerViewAdapterDate extends RecyclerView.Adapter<ExpenseViewHolderDate> {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";

    public ExpenseRecyclerViewAdapterDate(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ExpenseViewHolderDate onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.item_expense_by_date, parent, false);
        ExpenseViewHolderDate vh = new ExpenseViewHolderDate(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolderDate holder, int position) {
        HashMap<String, String> map = list.get(position);
        holder.date.setText(map.get(FIRST_COLUMN) + ":");
        holder.amount.setText(map.get(SECOND_COLUMN));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
