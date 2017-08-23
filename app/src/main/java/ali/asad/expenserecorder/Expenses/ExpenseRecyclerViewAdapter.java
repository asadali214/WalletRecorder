package ali.asad.expenserecorder.Expenses;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import ali.asad.expenserecorder.Categories.CategoryDBhelper;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 23-Aug-17.
 */

public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseViewHolder>{
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";
    public static final String FOURTH_COLUMN = "Fourth";
    public static final String FIFTH_COLUMN = "Fifth";

    public ExpenseRecyclerViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity = activity;
        this.list=list;
    }
    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.item_expense, parent, false);
        ExpenseViewHolder vh = new ExpenseViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        HashMap<String, String> map = list.get(position);
        holder.txtDate.setText(map.get(FIRST_COLUMN) + ":");
        holder.txtDetail.setText(map.get(SECOND_COLUMN));
        holder.txtAmount.setText(map.get(THIRD_COLUMN));
        holder.txtCategory.setText(map.get(FOURTH_COLUMN));
        holder.hidden.setText(map.get(FIFTH_COLUMN));

        for (int i = 0; i < ExpenseShowFragment.deleteListID.size(); i++) {
            if(holder.hidden.getText().toString().equals(ExpenseShowFragment.deleteListID.get(i))) {
                holder.checkBox.setChecked(true);
                LinearLayout.LayoutParams pram=new LinearLayout.LayoutParams(100,100);
                holder.checkBox.setLayoutParams(pram);
            }
            else {
                holder.checkBox.setChecked(false);
                LinearLayout.LayoutParams pram=new LinearLayout.LayoutParams(0,0);
                holder.checkBox.setLayoutParams(pram);
            }
        }

        //Setting Category icon to image view icon using lib Picasso..
        CategoryDBhelper db = new CategoryDBhelper(activity);
        String iconName = db.getIconOf(map.get(FOURTH_COLUMN));
        if (!iconName.equals("")) {
            Picasso.with(activity).load(activity.getResources().getIdentifier(iconName,
                    "drawable", activity.getPackageName())).into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
