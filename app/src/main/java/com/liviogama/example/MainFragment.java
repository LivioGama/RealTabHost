package com.liviogama.example;

import com.liviogama.realtabhost.example.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private int mBackgroundColor;

    public static MainFragment newInstance(int color) {
        final MainFragment mainFragment = new MainFragment();
        mainFragment.setBackgroundColor(color);
        return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.setBackgroundColor(mBackgroundColor);
        return view;
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }
}