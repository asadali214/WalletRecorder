package ali.asad.expenserecorder.Expenses;

import android.support.v7.widget.RecyclerView;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 22-Aug-17.
 */

public class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CheckBox checkBox;
    TextView hidden;
    TextView txtDate;
    TextView txtDetail;
    TextView txtAmount;
    TextView txtCategory;
    ImageView icon;
    public ExpenseViewHolder(View convertView) {
        super(convertView);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxExpense);
        hidden = (TextView) convertView.findViewById(R.id.hiddenExpense);
        txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        txtDetail = (TextView) convertView.findViewById(R.id.txtDetail);
        txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
        txtCategory = (TextView) convertView.findViewById(R.id.txtCategory);
        icon = (ImageView) convertView.findViewById(R.id.iconExpense);
        convertView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxExpense);
        TextView hidden = (TextView) view.findViewById(R.id.hiddenExpense);
        if (checkBox.isChecked()) {
            LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(0, 0);
            checkBox.setLayoutParams(pram);
            checkBox.setChecked(false);
            ExpenseShowFragment.deleteListID.remove(hidden.getText().toString());
            //Removing clicked item from deleteArray
        } else {
            LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(100, 100);
            checkBox.setLayoutParams(pram);
            checkBox.setChecked(true);
            ExpenseShowFragment.deleteListID.add(hidden.getText().toString());
            //Adding clicked item's id to deleteArray
        }
    }
}
