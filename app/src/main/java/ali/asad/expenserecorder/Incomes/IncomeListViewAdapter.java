package ali.asad.expenserecorder.Incomes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 17-Jul-17.
 */

public class IncomeListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;


    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";

    CheckBox checkBox;
    TextView txtDate;
    TextView txtDetail;
    TextView txtAmount;
    TextView hidden;
    public IncomeListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
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

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){
            convertView=inflater.inflate(R.layout.item_income, null);
        }

        checkBox=(CheckBox) convertView.findViewById(R.id.checkBoxIncome);
        hidden = (TextView) convertView.findViewById(R.id.hiddenIncome);
        txtDate=(TextView) convertView.findViewById(R.id.txtDateIncome);
        txtDetail=(TextView) convertView.findViewById(R.id.txtDetailIncome);
        txtAmount=(TextView) convertView.findViewById(R.id.txtAmountIncome);

        HashMap<String, String> map=list.get(position);
        txtDate.setText(map.get(FIRST_COLUMN)+":");
        txtDetail.setText(map.get(SECOND_COLUMN));
        txtAmount.setText(map.get(THIRD_COLUMN));
        hidden.setText(map.get(FOURTH_COLUMN));

        return convertView;
    }

}