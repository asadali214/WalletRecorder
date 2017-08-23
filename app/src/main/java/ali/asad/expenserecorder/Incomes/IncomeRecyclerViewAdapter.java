package ali.asad.expenserecorder.Incomes;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import ali.asad.expenserecorder.Expenses.ExpenseShowFragment;
import ali.asad.expenserecorder.Expenses.ExpenseViewHolder;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 23-Aug-17.
 */

public class IncomeRecyclerViewAdapter extends RecyclerView.Adapter<IncomeViewHolder> {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;


    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";
    public static final String FOURTH_COLUMN = "Fourth";

    public IncomeRecyclerViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public IncomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.item_income, parent, false);
        IncomeViewHolder vh = new IncomeViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(IncomeViewHolder holder, int position) {
        HashMap<String, String> map = list.get(position);
        holder.txtDate.setText(map.get(FIRST_COLUMN) + ":");
        holder.txtDetail.setText(map.get(SECOND_COLUMN));
        holder.txtAmount.setText(map.get(THIRD_COLUMN));
        holder.hidden.setText(map.get(FOURTH_COLUMN));
        for (int i = 0; i < IncomeShowFragment.deleteListID.size(); i++) {
            if (holder.hidden.getText().toString().equals(IncomeShowFragment.deleteListID.get(i))) {
                holder.checkBox.setChecked(true);
                LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(100, 100);
                holder.checkBox.setLayoutParams(pram);
            } else {
                holder.checkBox.setChecked(false);
                LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(0, 0);
                holder.checkBox.setLayoutParams(pram);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
