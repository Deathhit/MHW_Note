package tw.com.deathhit.view_model.list;

import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import tw.com.deathhit.R;

public final class SkillFragment extends BaseFragment {
    private static final int NUMBER_OF_TABS = 1;

    private static final int INDEX_ALL_SKILLS = 0;

    @Override
    protected ArrayList<String> getDataForTab(int index) {
        String path = null;

        switch (index){
            case INDEX_ALL_SKILLS :
                path = "/Skill";
                break;
        }

        assert path != null;
        ArrayList<String> items = dataHandler.getChildrenPaths(path);

        Collections.sort(items);

        return items;
    }

    @Override
    protected void configureSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_skill));
    }

    @Override
    protected void configureTabForIndex(TextView tab, int index) {
        String text = null;

        switch (index){
            case INDEX_ALL_SKILLS :
                text = getString(R.string.all_skills);
                break;
        }

        tab.setText(text);
    }

    @Override
    public int getNumberOfTabs() {
        return NUMBER_OF_TABS;
    }
}
