package tw.com.deathhit.view_model.list;

import android.support.v7.widget.SearchView;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import tw.com.deathhit.R;

public final class MonsterFragment extends BaseFragment {
    private static final int NUMBER_OF_TABS = 3;

    private static final int INDEX_BIG_CREATURES = 0;
    private static final int INDEX_SMALL_CREATURES = 1;
    private static final int INDEX_ENVIRONMENT_CREATURES = 2;

    @Override
    protected ArrayList<String> getDataForTab(int index) {
        String path = null;

        switch (index){
            case INDEX_BIG_CREATURES :
                path = "/Creatures/BigCreatures";
                break;
            case INDEX_SMALL_CREATURES :
                path = "/Creatures/SmallCreatures";
                break;
            case INDEX_ENVIRONMENT_CREATURES :
                path = "/Creatures/EnvironmentCreatures";
                break;
        }

        ArrayList<String> items;

        assert path != null;
        items = dataHandler.getSiblingsPaths(path);

        Collections.sort(items);

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);

            items.remove(i);

            if (dataHandler.getValue(item) == null)
                items.add(i, dataHandler.getKey(item));
            else
                items.add(i, "/MonsterDetail/" + dataHandler.getValue(item));
        }

        return items;
    }

    @Override
    protected void configureSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_monster));
    }

    @Override
    protected void configureTabForIndex(TextView tab, int index) {
        switch (index){
            case INDEX_BIG_CREATURES :
                tab.setText(getString(R.string.big_creatures));
                break;
            case INDEX_SMALL_CREATURES :
                tab.setText(getString(R.string.small_creatures));
                break;
            case INDEX_ENVIRONMENT_CREATURES :
                tab.setText(getString(R.string.environment_creatures));
                break;
        }
    }

    @Override
    public int getNumberOfTabs() {
        return NUMBER_OF_TABS;
    }
}
