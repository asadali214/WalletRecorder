package ali.asad.expenserecorder.Categories;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 15-Jul-17.
 */

public class CategoryShowFragment extends Fragment {
    ArrayList<HashMap<String, String>> list;
    RecyclerView recylerView;
    View rootView;
    CategoryRecyclerViewAdapter adapter;

    static List<String> deleteListID;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.show_category_fragment, container, false);
        recylerView = (RecyclerView) rootView.findViewById(R.id.listViewCategory);
        getActivity().setTitle("Expense Categories");
        MakeList();
        
        return rootView;
    }

    public void MakeList() {

        list = new ArrayList<HashMap<String, String>>();
        System.out.println("ID Category Icon");
        CategoryDBhelper db = new CategoryDBhelper(getActivity());
        List<String[]> categoryList = db.getAllEntries();
        for (int i = 0; i < categoryList.size(); i++) {

            String id = categoryList.get(i)[0];
            String cat = categoryList.get(i)[1];
            String icon = categoryList.get(i)[2];//PUT THIS IN LIST VIEW TOO in an imageview

            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put(CategoryRecyclerViewAdapter.FIRST_COLUMN, cat);
            temp.put(CategoryRecyclerViewAdapter.SECOND_COLUMN, icon);
            temp.put(CategoryRecyclerViewAdapter.THIRD_COLUMN, id);

            list.add(temp);
            System.out.println(id + " " + cat + " " + icon);
        }
        adapter = new CategoryRecyclerViewAdapter(getActivity(), list);
        adapter.notifyDataSetChanged();
        recylerView.setAdapter(adapter);
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
