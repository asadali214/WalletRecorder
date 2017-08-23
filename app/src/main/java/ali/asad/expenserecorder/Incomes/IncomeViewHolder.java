package ali.asad.expenserecorder.Incomes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import ali.asad.expenserecorder.Expenses.ExpenseShowFragment;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 22-Aug-17.
 */

public class IncomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CheckBox checkBox;
    TextView hidden;
    TextView txtDate;
    TextView txtDetail;
    TextView txtAmount;

    public IncomeViewHolder(View convertView) {
        super(convertView);
        checkBox=(CheckBox) convertView.findViewById(R.id.checkBoxIncome);
        hidden = (TextView) convertView.findViewById(R.id.hiddenIncome);
        txtDate=(TextView) convertView.findViewById(R.id.txtDateIncome);
        txtDetail=(TextView) convertView.findViewById(R.id.txtDetailIncome);
        txtAmount=(TextView) convertView.findViewById(R.id.txtAmountIncome);

        convertView.setOnTouchListener(IncomeShowFragment.incomeSwipeListener);
        convertView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (checkBox.isChecked()) {
            LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(0, 0);
            checkBox.setLayoutParams(pram);
            checkBox.setChecked(false);
            IncomeShowFragment.deleteListID.remove(hidden.getText().toString());
            //Removing clicked item from deleteArray
        } else {
            LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(100, 100);
            checkBox.setLayoutParams(pram);
            checkBox.setChecked(true);
            IncomeShowFragment.deleteListID.add(hidden.getText().toString());
            //Adding clicked item's id to deleteArray
        }
    }
}
