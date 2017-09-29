package ali.asad.expenserecorder.Expenses;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 29-Aug-17.
 * This class is same as the IncomeRecyclerViewAdapterItem
 */

public class ExpenseRecyclerViewAdapterItem extends RecyclerView.Adapter<ExpenseViewHolderItem> {
    List<String> list;
    Activity activity;
    View.OnClickListener clickListener;

    public ExpenseRecyclerViewAdapterItem(Activity activity, List<String> list,View.OnClickListener clickListener) {
        this.activity = activity;
        this.list = list;
        this.clickListener = clickListener;
    }

    @Override
    public ExpenseViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.item, parent, false);
        v.setOnClickListener(clickListener);
        ExpenseViewHolderItem vh = new ExpenseViewHolderItem(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolderItem holder, int position) {
        holder.detail.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
