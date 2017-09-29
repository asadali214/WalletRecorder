package ali.asad.expenserecorder.Incomes;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ali.asad.expenserecorder.Expenses.ExpenseViewHolderItem;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 31-Aug-17.
 * This class is same as the ExpenseRecyclerViewAdapterItem
 */

public class IncomeRecyclerViewAdapterItem extends RecyclerView.Adapter<IncomeViewHolderItem> {
    List<String> list;
    Activity activity;
    View.OnClickListener clickListener;

    public IncomeRecyclerViewAdapterItem(Activity activity, List<String> list, View.OnClickListener clickListener) {
        this.activity = activity;
        this.list = list;
        this.clickListener = clickListener;
    }

    @Override
    public IncomeViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.item, parent, false);
        v.setOnClickListener(clickListener);
        IncomeViewHolderItem vh = new IncomeViewHolderItem(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(IncomeViewHolderItem holder, int position) {
        holder.detail.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
