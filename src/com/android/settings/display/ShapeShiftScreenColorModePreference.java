/*
 * Copyright (C) 2021 ShapeShiftOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.display;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.settings.R;

import com.ssos.support.preferences.indicator.PageIndicatorView;

import java.util.ArrayList;

public class ShapeShiftScreenColorModePreference extends Preference {
    private static final int LAYOUT_RES_ID = R.layout.shapeshift_screen_color_mode_preference;
    private static final int LAYOUT_RES_ID_2K = R.layout.shapeshift_screen_color_mode_preference_2k;
    private int currIndex = 0;
    private Context mContext;
    private PageIndicatorView mPageIndicator;
    private int mCurrentPage;

    public ShapeShiftScreenColorModePreference(Context context, AttributeSet attributeSet, int numb) {
        super(context, attributeSet, numb);
        initView(context);
    }

    public ShapeShiftScreenColorModePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public ShapeShiftScreenColorModePreference(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;

        if (((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth() == 1440) {
            setLayoutResource(LAYOUT_RES_ID_2K);
        } else {
            setLayoutResource(LAYOUT_RES_ID);
        }
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ViewPager viewPager = (ViewPager) preferenceViewHolder.findViewById(R.id.whatsnew_viewpager);

        this.mPageIndicator = (PageIndicatorView) preferenceViewHolder.findViewById(R.id.gesture_page_indicator);
        LayoutInflater from = LayoutInflater.from(this.mContext);

        View inflate = from.inflate(R.layout.shapeshift_screen_image, (ViewGroup) null);
        ((ImageView) inflate.findViewById(R.id.imageview)).setImageResource(R.drawable.shapeshift_screen_show_1);
        View inflate2 = from.inflate(R.layout.shapeshift_screen_image, (ViewGroup) null);
        ((ImageView) inflate2.findViewById(R.id.imageview)).setImageResource(R.drawable.shapeshift_screen_show_2);
        View inflate3 = from.inflate(R.layout.shapeshift_screen_image, (ViewGroup) null);
        ((ImageView) inflate3.findViewById(R.id.imageview)).setImageResource(R.drawable.shapeshift_screen_show_3);

        final ArrayList arrayList = new ArrayList();
        arrayList.add(inflate);
        arrayList.add(inflate2);
        arrayList.add(inflate3);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            @Override
            public int getCount() {
                return arrayList.size();
            }

            @Override
            public void destroyItem(View view, int integer, Object obj) {
                ((ViewPager) view).removeView((View) arrayList.get(integer));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView((View) arrayList.get(position));
                return arrayList.get(position);
            }
        });

        viewPager.setCurrentItem(this.currIndex);
        this.mPageIndicator.setCount(arrayList.size());
        this.mPageIndicator.setSelection(viewPager.getCurrentItem());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                ShapeShiftScreenColorModePreference.this.currIndex = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int posIndicator;
                if (position == mCurrentPage) {
                    if (positionOffset > 0.5) {
                        posIndicator = position + 1;
                    } else {
                        posIndicator = position;
                    }
                } else {
                    if (positionOffset < 0.5) {
                        posIndicator = position;
                    } else {
                        posIndicator = position + 1;
                    }
                }
                ShapeShiftScreenColorModePreference.this.mPageIndicator.setSelection(posIndicator);
            }
        });
    }
}
