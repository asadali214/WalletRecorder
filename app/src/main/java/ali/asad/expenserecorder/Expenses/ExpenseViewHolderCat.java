package ali.asad.expenserecorder.Expenses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class ExpenseViewHolderCat extends RecyclerView.ViewHolder {
    TextView name,amount;
    ImageView icon;


    public ExpenseViewHolderCat(View convertView) {
        super(convertView);
        name = (TextView) convertView.findViewById(R.id.nameExpenseByCat);
        amount = (TextView) convertView.findViewById(R.id.amountExpenseByCat);
        icon = (ImageView) convertView.findViewById(R.id.iconExpenseByCat);
        convertView.setOnTouchListener(ExpenseShowFragment.expenseSwipeListener);
    }
}
