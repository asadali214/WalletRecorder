package ali.asad.expenserecorder.Incomes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ali.asad.expenserecorder.R;

/*
 * Created by AsadAli on 31-Aug-17.
 * This Class is same as ExpenseViewHolderItem
 */

public class IncomeViewHolderItem extends RecyclerView.ViewHolder {

    TextView detail;
    public IncomeViewHolderItem(View itemView) {
        super(itemView);
        detail = (TextView) itemView.findViewById(R.id.itemDetail);
    }
}
