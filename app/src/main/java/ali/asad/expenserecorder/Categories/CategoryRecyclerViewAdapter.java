package ali.asad.expenserecorder.Categories;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 22-Aug-17.
 */

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;


    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";

    public CategoryRecyclerViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity = activity;
        this.list = list;
    }
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.item_category, parent, false);
        CategoryViewHolder vh = new CategoryViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        HashMap<String, String> map = list.get(position);
        holder.txtName.setText(map.get(FIRST_COLUMN));
        Picasso.with(activity).load(activity.getResources().getIdentifier(map.get(SECOND_COLUMN),
                "drawable", activity.getPackageName())).into(holder.icon);
        holder.hidden.setText(map.get(THIRD_COLUMN));//id comes in here
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
