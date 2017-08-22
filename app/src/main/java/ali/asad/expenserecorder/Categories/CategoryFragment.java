package ali.asad.expenserecorder.Categories;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 12-Jul-17.
 */

public class CategoryFragment extends Fragment {
    EditText category;
    Spinner iconSpinner;
    String icons[] = {"food", "cars", "education",
            "entertainment", "gifts", "health",
            "security","travels","misc"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.category_fragment, container, false);
        getActivity().setTitle("Add Categories");
        category = (EditText) rootView.findViewById(R.id.ETnewCategory);
        iconSpinner = (Spinner) rootView.findViewById(R.id.iconSpinner);
        System.out.println("CategoryFragment Created");
        iconSpinner.setAdapter(new MySpinnerAdapter(getActivity(),icons));

        return rootView;
    }

    public void onGoCatgory(View view) {
        //Apply All Checks

        if (!category.getText().toString().equals("") && checkAlreadyPresent()) {

            String Category = category.getText().toString();
            String Icon = ""+icons[iconSpinner.getSelectedItemPosition()];
            CategoryDBhelper db = new CategoryDBhelper(getActivity());
            db.insertEntry(Category,Icon);
            Toast.makeText(getActivity().getApplicationContext(), "A new category added!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "Please insert correct data", Toast.LENGTH_SHORT).show();
        category.setText("");

    }

    public boolean checkAlreadyPresent() {
        CategoryDBhelper db = new CategoryDBhelper(getActivity());
        List<String[]> categoryList = db.getAllEntries();
        for (int i = 0; i < categoryList.size(); i++) {
            String cat = categoryList.get(i)[1];
            if (cat.equalsIgnoreCase(category.getText().toString()))
                return false;
        }
        return true;
    }
}
