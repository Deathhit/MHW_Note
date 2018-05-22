package tw.com.deathhit.view_model.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tw.com.deathhit.Constants;
import tw.com.deathhit.R;
import tw.com.deathhit.DataHandler;
import tw.com.deathhit.adapters.recycler_view.DataAdapter;
import tw.com.deathhit.utility.adapter.RecyclerObjectAdapter;
import tw.com.deathhit.utility.widget.TabBar;

abstract class BaseFragment extends tw.com.deathhit.core.BaseFragment implements View.OnClickListener, TabBar.OnItemClickListener, SearchView.OnQueryTextListener{
    private static final int LAYOUT_LIST_FRAGMENT = R.layout.fragment_list;

    private static final int ID_SEARCH_VIEW = R.id.searchView;
    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;
    private static final int ID_TAB_BAR = R.id.tabBar;

    private static final String BUNDLE_KEY_QUERY = "query";
    private static final String BUNDLE_KEY_TAB_INDEX = "tabIndex";

    protected DataHandler dataHandler;

    private String query = "";

    private int tabIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            query = savedInstanceState.getString(BUNDLE_KEY_QUERY, "");
            tabIndex = savedInstanceState.getInt(BUNDLE_KEY_TAB_INDEX, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        dataHandler = new DataHandler(inflater.getContext(), Constants.STORAGE_DATABASE);

        View view = inflater.inflate(LAYOUT_LIST_FRAGMENT, container, false);

        //Set up search view
        SearchView searchView = view.findViewById(ID_SEARCH_VIEW);

        configureSearchView(searchView);

        searchView.setOnQueryTextListener(this);

        View clearIcon = searchView.findViewById(R.id.search_close_btn);

        clearIcon.setOnClickListener(this);

        //Set up recycler view
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setHasFixedSize(true);

        //Set up adapters and tab bar
        TabBar tabBar = view.findViewById(ID_TAB_BAR);

        float textSizeInSp = getResources().getDimension(R.dimen.text_size_large) / getResources().getDisplayMetrics().density;

        for(int i=0;i<getNumberOfTabs();i++) {
            TextView tab = new TextView(getContext());

            configureTabForIndex(tab, i);

            tab.setTextSize(textSizeInSp);

            tab.setGravity(Gravity.CENTER);

            tabBar.addView(tab);
        }

        tabBar.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setAdapter(getAdapter(getDataForTab(tabIndex)));

        SearchView searchView = view.findViewById(ID_SEARCH_VIEW);

        searchView.setQuery(query, true);
    }

    @Override
    public void onDestroyView() {
        View view = getView();

        assert view != null;
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setAdapter(null);

        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BUNDLE_KEY_QUERY, query);
        outState.putInt(BUNDLE_KEY_TAB_INDEX, tabIndex);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onBackPressed(){
        View root = getView();

        assert root != null;
        SearchView searchView = root.findViewById(ID_SEARCH_VIEW);

        if(searchView.getQuery().length() > 0) {
            onClick(null);

            return true;
        }else
            return false;
    }

    @Override
    public void onClick(View view) {
        loadData(tabIndex);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;

        View root = getView();

        assert root != null;
        SearchView searchView = root.findViewById(ID_SEARCH_VIEW);

        RecyclerView recyclerView = root.findViewById(ID_RECYCLER_VIEW);

        ArrayList<String> results = new ArrayList<>();

        for(int i=0;i<getNumberOfTabs();i++){
            RecyclerObjectAdapter<String> adapter = getAdapter(getDataForTab(i));

            for(String path : adapter.getItems()) {
                if (path.contains(Constants.OPERATOR_PATH) && dataHandler.getKey(path).contains(query))
                    results.add(path);
            }
        }

        recyclerView.setAdapter(getAdapter(results));

        searchView.clearFocus();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View view, int index) {
        loadData(index);
    }

    private void loadData(int index){
        View root = getView();

        assert root != null;
        SearchView searchView = getView().findViewById(ID_SEARCH_VIEW);

        searchView.setQuery("", false);

        RecyclerView recyclerView = root.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setAdapter(getAdapter(getDataForTab(index)));

        query = "";

        tabIndex = index;
    }

    /**Override this method to define adapter type.**/
    protected RecyclerObjectAdapter<String> getAdapter(ArrayList<String> items){
        return new DataAdapter(dataHandler, items);
    }

    protected abstract ArrayList<String> getDataForTab(int index);

    protected abstract void configureSearchView(SearchView searchView);

    protected abstract void configureTabForIndex(TextView tab, int index);

    public abstract int getNumberOfTabs();
}
