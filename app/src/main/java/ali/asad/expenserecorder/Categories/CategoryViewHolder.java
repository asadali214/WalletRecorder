package ali.asad.expenserecorder.Categories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 22-Aug-17.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CheckBox checkBox;
    TextView hidden;
    TextView txtName;
    ImageView icon;

    public CategoryViewHolder(View convertView) {
        super(convertView);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxCategory);
        hidden =(TextView) convertView.findViewById(R.id.hiddenCategory);
        txtName = (TextView) convertView.findViewById(R.id.txtName);
        icon = (ImageView) convertView.findViewById(R.id.imageIcon);
        convertView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (checkBox.isChecked()) {
            LinearLayout.LayoutParams pram=new LinearLayout.LayoutParams(0,0);
            checkBox.setLayoutParams(pram);
            checkBox.setChecked(false);
            CategoryShowFragment.deleteListID.remove(hidden.getText().toString());//Removing clicked item from deleteArray
        } else {
            LinearLayout.LayoutParams pram=new LinearLayout.LayoutParams(100,100);
            checkBox.setLayoutParams(pram);
            checkBox.setChecked(true);
            CategoryShowFragment.deleteListID.add(hidden.getText().toString());//Adding clicked item's id to deleteArray

        }
    }
}
