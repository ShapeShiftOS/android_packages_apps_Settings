/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.android.settings.display;

import android.content.Context;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;

public class RefreshRatePreferenceController extends AbstractPreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_REFRESH_RATE = "refresh_rate_setting";

    private ListPreference mRefreshRate;
    private int mMaxRefreshRate;

    public RefreshRatePreferenceController(Context context) {
        super(context);
    }

    @Override
    public boolean isAvailable() {
        return mContext.getResources().getBoolean(com.android.internal.R.bool.config_hasVariableRefreshRate);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_REFRESH_RATE;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        if (!isAvailable()) {
            setVisible(screen, KEY_REFRESH_RATE, false);
            return;
        }
        mRefreshRate = (ListPreference) screen.findPreference(KEY_REFRESH_RATE);
        int refreshRate = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.REFRESH_RATE_SETTING, 0);
        mMaxRefreshRate = mContext.getResources().getInteger(
                com.android.internal.R.integer.config_maxRefreshRate);

        CharSequence[] m90HzEntries = { mContext.getResources().getString(R.string.auto_refresh_rate),
                mContext.getResources().getString(R.string.refresh_rate_60),
                mContext.getResources().getString(R.string.refresh_rate_90) };
        CharSequence[] m120HzEntries = { mContext.getResources().getString(R.string.auto_refresh_rate),
                mContext.getResources().getString(R.string.refresh_rate_60),
                mContext.getResources().getString(R.string.refresh_rate_120) };
        CharSequence[] mRefreshRateValues = {"0", "1" , "2"};

        mRefreshRate.setEntries(mMaxRefreshRate == 90 ? m90HzEntries : m120HzEntries);
        mRefreshRate.setEntryValues(mRefreshRateValues);
        mRefreshRate.setValue(String.valueOf(refreshRate));
        mRefreshRate.setOnPreferenceChangeListener(this);
        updateRefreshRateSummary(refreshRate);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int refreshRate = Integer.valueOf((String) newValue);
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.System.REFRESH_RATE_SETTING, refreshRate);
        updateRefreshRateSummary(refreshRate);
        return true;
    }

    public void updateRefreshRateSummary(int refreshRate) {
        if (refreshRate == 1) {
            mRefreshRate.setSummary(R.string.refresh_rate_summary_60);
        } else if (refreshRate == 2) {
            if (mMaxRefreshRate == 90)
                mRefreshRate.setSummary(R.string.refresh_rate_summary_90);
            else
                mRefreshRate.setSummary(R.string.refresh_rate_summary_120);
        } else {
            if (mMaxRefreshRate == 90)
                mRefreshRate.setSummary(R.string.refresh_rate_summary_auto_90);
            else
                mRefreshRate.setSummary(R.string.refresh_rate_summary_auto_120);
        }
    }
}
