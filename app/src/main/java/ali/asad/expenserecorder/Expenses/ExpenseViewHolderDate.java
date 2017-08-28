package ali.asad.expenserecorder.Expenses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class ExpenseViewHolderDate extends RecyclerView.ViewHolder {
    TextView date,amount;

    public ExpenseViewHolderDate(View convertView) {
        super(convertView);
        date = (TextView) convertView.findViewById(R.id.dateExpenseByDate);
        amount = (TextView) convertView.findViewById(R.id.amountExpenseByDate);

        convertView.setOnTouchListener(ExpenseShowFragment.expenseSwipeListener);
    }
}
