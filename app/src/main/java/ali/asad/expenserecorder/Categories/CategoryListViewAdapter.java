package ali.asad.expenserecorder.Categories;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 17-Jul-17.
 */

public class CategoryListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";
    CheckBox checkBox;
    TextView hidden;
    TextView txtName;
    ImageView icon;

    public CategoryListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getViewTypeCount() {
        if (list.size() == 0) {
            return 1;
        }
        return list.size();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_category, null);

        }

        checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxCategory);
        hidden =(TextView) convertView.findViewById(R.id.hiddenCategory);
        txtName = (TextView) convertView.findViewById(R.id.txtName);
        icon = (ImageView) convertView.findViewById(R.id.imageIcon);

        HashMap<String, String> map = list.get(position);
        txtName.setText(map.get(FIRST_COLUMN));
        Picasso.with(activity).load(activity.getResources().getIdentifier(map.get(SECOND_COLUMN),
                "drawable", activity.getPackageName())).into(icon);
        hidden.setText(map.get(THIRD_COLUMN));//id comes in here
        return convertView;
    }


}
