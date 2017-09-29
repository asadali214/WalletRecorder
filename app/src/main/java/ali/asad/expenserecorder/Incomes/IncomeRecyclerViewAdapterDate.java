package ali.asad.expenserecorder.Incomes;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ali.asad.expenserecorder.Expenses.ExpenseViewHolderDate;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class IncomeRecyclerViewAdapterDate extends RecyclerView.Adapter<IncomeViewHolderDate>{
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";

    public IncomeRecyclerViewAdapterDate(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public IncomeViewHolderDate onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.item_income_by_date, parent, false);
        IncomeViewHolderDate vh = new IncomeViewHolderDate(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(IncomeViewHolderDate holder, int position) {
        HashMap<String, String> map = list.get(position);
        holder.date.setText(map.get(FIRST_COLUMN) + ":");
        holder.amount.setText(map.get(SECOND_COLUMN));
        holder.activity=activity;
        List<String> list = new ArrayList<>();
        IncomeRecyclerViewAdapterItem adapter = new IncomeRecyclerViewAdapterItem(activity, list, holder);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        holder.isShowing=false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
