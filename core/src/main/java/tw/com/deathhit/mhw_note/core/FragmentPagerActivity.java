package tw.com.deathhit.mhw_note.core;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

/**Activity class that supports fragment in a view pager.**/
public abstract class FragmentPagerActivity extends BaseActivity {
    public static FragmentPagerActivity get() {
        return (FragmentPagerActivity)BaseActivity.get();
    }

    /**Allows fragment to implement onBackPressed().**/
    @Override
    public void onBackPressed() {
        if(getCurrentFragment() == null || !getCurrentFragment().onBackPressed())
            super.onBackPressed();
    }

    /**Get the current fragment shown on the view pager.**/
    @Nullable
    public BaseFragment getCurrentFragment(){
        ViewPager viewPager = findViewById(getViewPagerId());

        return  getCurrentFragment(viewPager.getCurrentItem());
    }

    /**Get the current fragment shown on the view pager by index.**/
    @Nullable
    public BaseFragment getCurrentFragment(int index){
        return  (BaseFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + getViewPagerId() + ":" + index);
    }

    /**Get the view pager id. This method is used in getCurrentFragment() to get fragment.**/
    public abstract int getViewPagerId();
}
