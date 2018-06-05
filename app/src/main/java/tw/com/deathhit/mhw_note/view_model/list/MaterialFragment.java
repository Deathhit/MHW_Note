package tw.com.deathhit.mhw_note.view_model.list;

import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import tw.com.deathhit.mhw_note.R;

public final class MaterialFragment extends BaseFragment {
    private static final int NUMBER_OF_TABS = 1;

    private static final int INDEX_ALL_MATERIAL = 0;

    @Override
    protected ArrayList<String> getDataForTab(int index) {
        String path = null;

        switch (index){
            case INDEX_ALL_MATERIAL :
                path = "/MonsterAssetsDetail";
                break;
        }

        assert path != null;
        ArrayList<String> items = dataHandler.getChildrenPaths(path);

        Collections.sort(items);

        return items;
    }

    @Override
    protected void configureSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_material));
    }

    @Override
    protected void configureTabForIndex(TextView tab, int index) {
        switch (index){
            case INDEX_ALL_MATERIAL :
                tab.setText(getString(R.string.all_materials));
                break;
        }
    }

    @Override
    public int getNumberOfTabs() {
        return NUMBER_OF_TABS;
    }
}
