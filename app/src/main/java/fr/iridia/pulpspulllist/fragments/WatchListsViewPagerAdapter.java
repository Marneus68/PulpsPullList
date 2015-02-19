package fr.iridia.pulpspulllist.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import fr.iridia.pulpspulllist.MainActivity;
import fr.iridia.pulpspulllist.R;

public class WatchListsViewPagerAdapter extends FragmentPagerAdapter {

    protected String[] headers;

    public WatchListsViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        headers = context.getResources().getStringArray(R.array.watchlists_headers);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListFragment lfEn = new ListFragment();
                lfEn.setKey(MainActivity.EnglishWatchlistKey);
                return lfEn;
            case 1:
                ListFragment lfFr = new ListFragment();
                lfFr.setKey(MainActivity.FrenchWatchlistKey);
                return lfFr;
        }
        return null;
    }

    @Override
    public int getCount() {
        return headers.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return headers[position];
    }
}
