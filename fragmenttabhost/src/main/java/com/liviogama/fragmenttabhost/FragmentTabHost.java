package com.liviogama.fragmenttabhost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * RealTabHost
 *
 * Created by livio on 04/08/15.
 */
public class FragmentTabHost extends LinearLayout {

    private FragmentManager mFragmentManager;

    private List<TabSpec> mTabSpec = new ArrayList<>();

    private LinearLayout mTabContainer;

    private int mNormalTintColor;

    private int mSelectedTintColor;

    public FragmentTabHost(Context context) {
        super(context);
        initView();
    }

    public FragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FragmentTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FragmentTabHost(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.tabhost, this);
        mTabContainer = (LinearLayout) findViewById(R.id.tabContainer);
        mNormalTintColor = Color.WHITE;
        mNormalTintColor = Color.parseColor("#BED7FF");
        mTabContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 150));
    }

    public void setup(Context context, FragmentManager supportFragmentManager) {
        mFragmentManager = supportFragmentManager;
    }

    public TabSpec newTabSpec(String tabTitle, int layoutId, Fragment fragment) {
        return newTabSpec(tabTitle, 0, layoutId, fragment);
    }

    public TabSpec newTabSpec(String tabTitle, int drawableId, int layoutId, Fragment fragment) {
        TabSpec tabSpec = new TabSpec();
        tabSpec.setTabTitle(tabTitle);
        tabSpec.setDrawableId(drawableId);
        tabSpec.setLayoutId(layoutId);
        tabSpec.setFragment(fragment);
        return tabSpec;
    }

    public TabSpec newTabSpec(String tabTitle, int layoutId, Class fragmentClass) {
        return newTabSpec(tabTitle, 0, layoutId, fragmentClass);
    }

    public TabSpec newTabSpec(String tabTitle, int drawableId, int layoutId, Class fragmentClass) {
        TabSpec tabSpec = new TabSpec();
        tabSpec.setTabTitle(tabTitle);
        tabSpec.setDrawableId(drawableId);
        tabSpec.setLayoutId(layoutId);
        tabSpec.setClaz(fragmentClass);
        return tabSpec;
    }

    public void addTab(TabSpec tabSpec) {
        mTabSpec.add(tabSpec);
    }

    public void build() {
        if (mTabSpec.isEmpty()) {
            throw (new IllegalArgumentException("No tab to build found. Use tabHost.addTab() before calling tabHost.build()"));
        } else {
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            params.weight = 1;

            for (final TabSpec tabSpec : mTabSpec) {
                if (tabSpec.getClaz() == null && tabSpec.getFragment() == null) {
                    throw (new IllegalArgumentException("Class or fragment should be provided"));
                } else {
                    if (!TextUtils.isEmpty(tabSpec.getTabTitle()) || tabSpec.getDrawableId() != 0) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        try {
                            final View view = inflater.inflate(tabSpec.getLayoutId(), null);
                            boolean didSetText = false;
                            boolean didSetImage = false;
                            for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                                View nextChild = ((ViewGroup) view).getChildAt(i);
                                if (nextChild instanceof TextView && !didSetText) {
                                    if (TextUtils.isEmpty(tabSpec.getTabTitle())) {
                                        nextChild.setVisibility(GONE);
                                    } else {
                                        ((TextView) nextChild).setText(tabSpec.getTabTitle());
                                        ((TextView) nextChild).setTextColor(mNormalTintColor);
                                    }
                                    didSetText = true;
                                } else if (nextChild instanceof ImageView && !didSetImage) {
                                    if (tabSpec.getDrawableId() == 0) {
                                        nextChild.setVisibility(GONE);
                                    } else {
                                        try {
                                            ((ImageView) nextChild).setImageDrawable(
                                                    ContextCompat.getDrawable(getContext(), tabSpec.getDrawableId()));
                                        } catch (Resources.NotFoundException e) {
                                            throw (new IllegalArgumentException(
                                                    String.format("Unable to find drawable %s", tabSpec.getDrawableId())));
                                        }
                                        ((ImageView) nextChild).setColorFilter(mNormalTintColor, PorterDuff.Mode.SRC_ATOP);
                                    }
                                    didSetImage = true;
                                }

                                if (didSetImage && didSetText) {
                                    break;
                                }
                            }

                            view.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int i = 0; i < ((ViewGroup) v.getParent()).getChildCount(); ++i) {
                                        View tab = ((ViewGroup) v.getParent()).getChildAt(i);
                                        for (int j = 0; j < ((ViewGroup) tab).getChildCount(); ++j) {
                                            View nextChild = ((ViewGroup) tab).getChildAt(j);
                                            if (nextChild instanceof TextView) {
                                                ((TextView) nextChild).setTextColor(mNormalTintColor);
                                            } else if (nextChild instanceof ImageView) {
                                                ((ImageView) nextChild)
                                                        .setColorFilter(mNormalTintColor, PorterDuff.Mode.SRC_ATOP);
                                            }
                                        }
                                    }
                                    for (int i = 0; i < ((ViewGroup) v).getChildCount(); ++i) {
                                        View nextChild = ((ViewGroup) v).getChildAt(i);
                                        if (nextChild instanceof TextView) {
                                            ((TextView) nextChild).setTextColor(mSelectedTintColor);
                                        } else if (nextChild instanceof ImageView) {
                                            ((ImageView) nextChild).setColorFilter(mSelectedTintColor, PorterDuff.Mode.SRC_ATOP);
                                        }
                                    }
                                    v.setSelected(true);

                                    Fragment fragment;
                                    if (tabSpec.getFragment() != null) {
                                        fragment = tabSpec.getFragment();
                                    } else {
                                        fragment = Fragment.instantiate(getContext(), tabSpec.getClaz().getSimpleName());
                                    }
                                    if (fragment != null && !fragment.isAdded()) {
                                        mFragmentManager.beginTransaction()
                                                .replace(R.id.tabContent, fragment)
                                                .commit();
                                    }
                                }
                            });

                            mTabContainer.addView(view, params);
                        } catch (Resources.NotFoundException e) {
                            throw (new IllegalArgumentException(
                                    String.format("Unable to find layout %s", tabSpec.getLayoutId())));
                        }
                    }
                }
            }

            mTabContainer.getChildAt(0).performClick();
        }
    }

    public void setNormalTintColor(int normalTintColor) {
        mNormalTintColor = normalTintColor;
    }

    public void setSelectedTintColor(int selectedTintColor) {
        mSelectedTintColor = selectedTintColor;
    }

    public void setHeight(int height) {
        mTabContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
    }

    public class TabSpec {

        private int layoutId;

        private Fragment fragment;

        private Class claz;

        private String tabTitle;

        private int drawableId;

        public int getLayoutId() {
            return layoutId;
        }

        public void setLayoutId(int resourceId) {
            this.layoutId = resourceId;
        }

        public Class getClaz() {
            return claz;
        }

        public void setClaz(Class claz) {
            this.claz = claz;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public String getTabTitle() {
            return tabTitle;
        }

        public void setTabTitle(String tabTitle) {
            this.tabTitle = tabTitle;
        }

        public int getDrawableId() {
            return drawableId;
        }

        public void setDrawableId(int drawableId) {
            this.drawableId = drawableId;
        }
    }
}