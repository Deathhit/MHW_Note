package tw.com.deathhit;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdView;

import tw.com.deathhit.core.BaseFragment;
import tw.com.deathhit.view_model.detail.PositionFragment;
import tw.com.deathhit.view_model.detail.SeriesFragment;
import tw.com.deathhit.view_model.detail.GuardStoneFragment;
import tw.com.deathhit.view_model.detail.GuardStoneUpgradeFragment;
import tw.com.deathhit.view_model.detail.JewelFragment;
import tw.com.deathhit.view_model.detail.MaterialFragment;
import tw.com.deathhit.view_model.detail.MonsterFragment;
import tw.com.deathhit.view_model.detail.SkillFragment;
import tw.com.deathhit.view_model.detail.CalculatorFragment;

public final class DetailActivity extends BaseActivity {
    private boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        if(savedInstanceState != null)
            isInitialized = true;

        setFragmentContainer(R.id.frameLayout);

        //Show fragment
        Bundle args = getIntent().getExtras();

        assert args != null;
        String path = args.getString(Constants.ARGUMENT_PATH);

        int request = args.getInt(Constants.ARGUMENT_REQUEST);

        request(request, path);

        //Set up advertisement
        AdView adView = findViewById(R.id.adView);
        loadAdViews(adView);
    }

    @Override
    protected Object request(int requestType, @Nullable Object... args) {
        String className = null;

        switch (requestType){
            case Constants.REQUEST_MONSTER_DETAIL :
                className = MonsterFragment.class.getName();
                break;
            case Constants.REQUEST_MATERIAL_DETAIL :
                className = MaterialFragment.class.getName();
                break;
            case Constants.REQUEST_EQUIPMENT_SERIES_DETAIL :
                className = SeriesFragment.class.getName();
                break;
            case Constants.REQUEST_EQUIPMENT_POSITION_DETAIL :
                className = PositionFragment.class.getName();
                break;
            case Constants.REQUEST_GUARD_STONE_DETAIL :
                className = GuardStoneFragment.class.getName();
                break;
            case Constants.REQUEST_GUARD_STONE_UPGRADE_DETAIL :
                className = GuardStoneUpgradeFragment.class.getName();
                break;
            case Constants.REQUEST_JEWEL_DETAIL :
                className = JewelFragment.class.getName();
                break;
            case Constants.REQUEST_SKILL_DETAIL :
                className = SkillFragment.class.getName();
                break;
            case Constants.REQUEST_CALCULATOR_DETAIL :
                className = CalculatorFragment.class.getName();
                break;
        }

        BaseFragment fragment = null;

        try {
            fragment = (BaseFragment) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle fragmentArgs = new Bundle();

        assert args != null;
        fragmentArgs.putString(Constants.ARGUMENT_PATH, (String)args[0]);

        assert fragment != null;
        fragment.setArguments(fragmentArgs);

        if(isInitialized)
            setFragment(fragment, true);
        else{
            setFragment(fragment, false);

            isInitialized = true;
        }

        return fragment;
    }
}
