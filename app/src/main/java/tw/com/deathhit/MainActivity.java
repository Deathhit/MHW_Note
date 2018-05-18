package tw.com.deathhit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import tw.com.deathhit.core.BaseFragment;
import tw.com.deathhit.components.list.CalculatorFragment;
import tw.com.deathhit.components.list.EquipmentFragment;
import tw.com.deathhit.components.list.MaterialFragment;
import tw.com.deathhit.components.list.MonsterFragment;
import tw.com.deathhit.components.list.SkillFragment;

public final class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{
    private static final int NUMBER_OF_TABS = 5;

    private static final int INDEX_MONSTER = 0;
    private static final int INDEX_MATERIAL = 1;
    private static final int INDEX_EQUIPMENT = 2;
    private static final int INDEX_SKILL = 3;
    private static final int INDEX_CALCULATOR = 4;

    private ViewPager viewPager;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        //Hide keyboard until an editor is chosen
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_main);

        //Set up tab layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(this);

        //Set up view pager
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(NUMBER_OF_TABS);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        viewPager.setVisibility(View.VISIBLE); //Fix for FixedAspectFrameLayout
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Set up advertisement
        MobileAds.initialize(MainActivity.this, getResources().getString(R.string.banner_ad_unit_id));
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onBindViewOnce() {

    }

    @Override
    protected BaseFragment getCurrentFragment(){
        return  (BaseFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
    }

    @Override
    protected Object request(int requestType, @Nullable Object... args) {
        Intent intent = new Intent(this, DetailActivity.class);

        assert args != null;
        String path = (String)args[0];

        intent.putExtra(Constants.ARGUMENT_PATH, path);
        intent.putExtra(Constants.ARGUMENT_REQUEST, requestType);

        tw.com.deathhit.core.BaseActivity.Presenter.startActivity(intent);

        return null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private static final class Adapter extends FragmentPagerAdapter{
        Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case INDEX_MONSTER:
                    return new MonsterFragment();
                case INDEX_MATERIAL:
                    return new MaterialFragment();
                case INDEX_EQUIPMENT:
                    return new EquipmentFragment();
                case INDEX_SKILL:
                    return new SkillFragment();
                case INDEX_CALCULATOR:
                    return new CalculatorFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_TABS;
        }
    }
}
