package tw.com.deathhit.mhw_note.view_model.list;

import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import tw.com.deathhit.mhw_note.R;
import tw.com.deathhit.mhw_note.utils.comparator.JewelComparator;

public final class EquipmentFragment extends BaseFragment {
    private static final int NUMBER_OF_TABS = 4;

    private static final int INDEX_PRIMARY_EQUIPMENT = 0;
    private static final int INDEX_ADVANCED_EQUIPMENT = 1;
    private static final int INDEX_GUARD_STONE = 2;
    private static final int INDEX_JEWEL = 3;

    @Override
    protected ArrayList<String> getDataForTab(int index) {
        String path = null;

        switch (index){
            case INDEX_PRIMARY_EQUIPMENT :
                path = "/Equipment/Armor/初階";
                break;
            case INDEX_ADVANCED_EQUIPMENT :
                path = "/Equipment/Armor/進階";
                break;
            case INDEX_GUARD_STONE :
                path = "/Equipment/GuardStone";
                break;
            case INDEX_JEWEL :
                path = "/Equipment/Jewelry";
                break;
        }

        assert path != null;
        ArrayList<String> items = dataHandler.getChildrenPaths(path);

        if(index != INDEX_JEWEL)
            Collections.sort(items);
        else
            Collections.sort(items, new JewelComparator(dataHandler));

        return items;
    }

    @Override
    protected void configureSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_equipment_guard_stone_jewel));
    }

    @Override
    protected void configureTabForIndex(TextView tab, int index) {
        String text = null;

        switch (index){
            case INDEX_PRIMARY_EQUIPMENT :
                text = getString(R.string.primary);
                break;
            case INDEX_ADVANCED_EQUIPMENT :
                text = getString(R.string.advanced);
                break;
            case INDEX_GUARD_STONE :
                text = getString(R.string.guard_stone);
                break;
            case INDEX_JEWEL :
                text = getString(R.string.jewel);
                break;
        }

        tab.setText(text);
    }

    @Override
    public int getNumberOfTabs() {
        return NUMBER_OF_TABS;
    }
}
