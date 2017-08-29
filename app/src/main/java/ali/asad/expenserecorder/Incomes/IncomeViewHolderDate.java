package ali.asad.expenserecorder.Incomes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ali.asad.expenserecorder.Expenses.ExpenseShowFragment;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class IncomeViewHolderDate extends RecyclerView.ViewHolder {
    TextView date, amount;

    public IncomeViewHolderDate(View convertView) {
        super(convertView);
        date = (TextView) convertView.findViewById(R.id.dateIncomeByDate);
        amount = (TextView) convertView.findViewById(R.id.amountIncomeByDate);

        convertView.setOnTouchListener(IncomeShowFragment.incomeSwipeListener);
    }
}