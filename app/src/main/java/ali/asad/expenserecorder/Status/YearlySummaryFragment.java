package ali.asad.expenserecorder.Status;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import ali.asad.expenserecorder.R;

import static ali.asad.expenserecorder.Status.StatusTabFragment.adpYear;

/**
 * Created by AsadAli on 03-Aug-17.
 */

public class YearlySummaryFragment extends Fragment {
    public static Spinner year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.status_yearly_fragment, container, false);
        System.out.println("Yearly Summary Open");

        year = (Spinner) rootView.findViewById(R.id.yearSpinnerSummary);
        year.setAdapter(adpYear);
        return rootView;
    }

}
