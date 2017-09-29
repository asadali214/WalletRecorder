package ali.asad.expenserecorder.Expenses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ali.asad.expenserecorder.R;

/*
 * Created by AsadAli on 29-Aug-17.
 * This Class is same as IncomeViewHolderItem
 */

public class ExpenseViewHolderItem extends RecyclerView.ViewHolder {
    TextView detail;

    public ExpenseViewHolderItem(View itemView) {
        super(itemView);
        detail = (TextView) itemView.findViewById(R.id.itemDetail);
    }
}
