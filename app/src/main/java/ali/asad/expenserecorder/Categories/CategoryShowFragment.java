package ali.asad.expenserecorder.Categories;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 15-Jul-17.
 */

public class CategoryShowFragment extends Fragment {
    ArrayList<HashMap<String, String>> list;
    RecyclerView listView;
    View rootView;
    CategoryListViewAdapter adapter;

    List<String> deleteListID;
    public static List<String[]> Categorylist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.show_category_fragment, container, false);
        getActivity().setTitle("Expense Categories");
        MakeList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                System.out.println("Item # " + pos + " pressed");
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxCategory);
                TextView hidden = (TextView) view.findViewById(R.id.hiddenCategory);
                if (checkBox.isChecked()) {
                    LinearLayout.LayoutParams pram=new LinearLayout.LayoutParams(0,0);
                    checkBox.setLayoutParams(pram);
                    checkBox.setChecked(false);
                    deleteListID.remove(hidden.getText().toString());//Removing clicked item from deleteArray
                } else {
                    LinearLayout.LayoutParams pram=new LinearLayout.LayoutParams(100,100);
                    checkBox.setLayoutParams(pram);
                    checkBox.setChecked(true);
                    deleteListID.add(hidden.getText().toString());//Adding clicked item's id to deleteArray

                }
            }
        });
        return rootView;
    }

    public void MakeList() {
        listView = (RecyclerView) rootView.findViewById(R.id.listViewCategory);
        list = new ArrayList<HashMap<String, String>>();
        System.out.println("ID Category Icon");
        CategoryDBhelper db = new CategoryDBhelper(getActivity());
        List<String[]> categoryList = db.getAllEntries();
        for (int i = 0; i < categoryList.size(); i++) {

            String id = categoryList.get(i)[0];
            String cat = categoryList.get(i)[1];
            String icon = categoryList.get(i)[2];//PUT THIS IN LIST VIEW TOO in an imageview

            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put(CategoryListViewAdapter.FIRST_COLUMN, cat);
            temp.put(CategoryListViewAdapter.SECOND_COLUMN, icon);
            temp.put(CategoryListViewAdapter.THIRD_COLUMN, id);

            list.add(temp);
            System.out.println(id + " " + cat + " " + icon);
        }
        adapter = new CategoryListViewAdapter(getActivity(), list);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        deleteListID = new ArrayList<String>();
    }

    public void onClearCategories() {
        CategoryDBhelper db = new CategoryDBhelper(getActivity());
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        db.onUpgrade(dbWrite, 1, 1);
        MakeList();
    }

    public void onClearSelectedCategories() {
        int deleted = 0;
        for (int i = 0; i < deleteListID.size(); i++) {
            CategoryDBhelper db = new CategoryDBhelper(getActivity());
            db.deleteEntry(Integer.parseInt(deleteListID.get(i)));
            deleted++;
        }
        System.out.println("Deleted Items: " + deleted);
        MakeList();
    }
}
