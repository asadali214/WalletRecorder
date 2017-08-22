package ali.asad.expenserecorder.Expenses;

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

import ali.asad.expenserecorder.Categories.CategoryDBhelper;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 17-Jul-17.
 */

public class ExpenseListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;


    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";
    public static final String FOURTH_COLUMN = "Fourth";
    public static final String FIFTH_COLUMN = "Fifth";


    CheckBox checkBox;
    TextView hidden;
    TextView txtDate;
    TextView txtDetail;
    TextView txtAmount;
    TextView txtCategory;
    ImageView icon;

    public ExpenseListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expense, null);
        }

        checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxExpense);
        hidden = (TextView) convertView.findViewById(R.id.hiddenExpense);
        txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        txtDetail = (TextView) convertView.findViewById(R.id.txtDetail);
        txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
        txtCategory = (TextView) convertView.findViewById(R.id.txtCategory);
        icon = (ImageView) convertView.findViewById(R.id.iconExpense);

        HashMap<String, String> map = list.get(position);
        txtDate.setText(map.get(FIRST_COLUMN) + ":");
        txtDetail.setText(map.get(SECOND_COLUMN));
        txtAmount.setText(map.get(THIRD_COLUMN));
        txtCategory.setText(map.get(FOURTH_COLUMN));
        hidden.setText(map.get(FIFTH_COLUMN));

        //Setting Category icon to image view icon using lib Picasso..
        CategoryDBhelper db = new CategoryDBhelper(activity);
        String iconName = db.getIconOf(map.get(FOURTH_COLUMN));
        if (!iconName.equals("")) {
            Picasso.with(activity).load(activity.getResources().getIdentifier(iconName,
                    "drawable", activity.getPackageName())).into(icon);
        }
        return convertView;
    }

}