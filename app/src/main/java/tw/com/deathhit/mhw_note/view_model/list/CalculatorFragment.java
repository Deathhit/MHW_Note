package tw.com.deathhit.mhw_note.view_model.list;

import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import tw.com.deathhit.mhw_note.R;
import tw.com.deathhit.mhw_note.view_model.adapter.recycler_view.CalculatorAdapter;
import tw.com.deathhit.mhw_note.utility.adapter.RecyclerObjectAdapter;

public final class CalculatorFragment extends BaseFragment {
    private static final int NUMBER_OF_TABS = 2;

    private static final int INDEX_PRIMARY = 0;
    private static final int INDEX_ADVANCED = 1;

    @Override
    protected RecyclerObjectAdapter<String> getAdapter(ArrayList<String> items){
        return new CalculatorAdapter(dataHandler, items);
    }

    @Override
    protected ArrayList<String> getDataForTab(int index) {
        ArrayList<String> items = new ArrayList<>();

        String path;

        switch (index){
            case INDEX_PRIMARY :
                path = "/Equipment/Armor/初階";

                items = dataHandler.getChildrenPaths(path);

                break;
            case INDEX_ADVANCED :
                path = "/Equipment/Armor/進階";

                items = dataHandler.getChildrenPaths(path);
                break;
        }

        Collections.sort(items);

        return items;
    }

    @Override
    protected void configureSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_calculator));
    }

    @Override
    protected void configureTabForIndex(TextView tab, int index) {
        String text = null;

        switch (index){
            case INDEX_PRIMARY :
                text = getString(R.string.primary);
                break;
            case INDEX_ADVANCED :
                text = getString(R.string.advanced);
                break;
        }

        tab.setText(text);
    }

    @Override
    public int getNumberOfTabs() {
        return NUMBER_OF_TABS;
    }
}
