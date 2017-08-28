package ali.asad.expenserecorder.Expenses;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import ali.asad.expenserecorder.Categories.CategoryDBhelper;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 28-Aug-17.
 */

public class ExpenseRecyclerViewAdapterCat extends RecyclerView.Adapter<ExpenseViewHolderCat> {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";

    public ExpenseRecyclerViewAdapterCat(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity = activity;
        this.list=list;
    }

    @Override
    public ExpenseViewHolderCat onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.item_expense_by_category, parent, false);
        ExpenseViewHolderCat vh = new ExpenseViewHolderCat(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolderCat holder, int position) {
        HashMap<String, String> map = list.get(position);
        holder.name.setText(map.get(FIRST_COLUMN)+":");
        holder.amount.setText(map.get(SECOND_COLUMN));
        //Setting Category icon to image view icon using lib Picasso..
        CategoryDBhelper db = new CategoryDBhelper(activity);
        String iconName = db.getIconOf(map.get(FIRST_COLUMN));
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
