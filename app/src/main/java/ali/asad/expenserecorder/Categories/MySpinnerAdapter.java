package ali.asad.expenserecorder.Categories;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 08-Aug-17.
 */

public class MySpinnerAdapter extends BaseAdapter {


    String icons[];
    Activity activity;
    ImageView icon;
    LayoutInflater inflater;

    public MySpinnerAdapter(Activity activity, String[] icons) {
        super();
        this.icons=icons;
        this.activity = activity;
        inflater=(LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return icons[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
            return 1;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_layout_image, viewGroup, false);
        }

        icon = (ImageView) view.findViewById(R.id.img);
        Picasso.with(activity).load(activity.getResources().getIdentifier(icons[i], "drawable", activity.getPackageName())).into(icon);
        //icon.setImageResource(activity.getResources().getIdentifier(icons[i], "drawable", activity.getPackageName()));
        return view;
    }
}
