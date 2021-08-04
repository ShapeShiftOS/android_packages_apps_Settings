/*
 * Copyright (C) 2019 The Android Open Source Project
 * Copyright (C) 2020 ShapeShiftOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.firmwareversion;

import android.app.settings.SettingsEnums;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.deviceinfo.firmwareversion.EasterEgg;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.Preference.OnPreferenceChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SearchIndexable
public class FirmwareVersionSettings extends DashboardFragment implements OnPreferenceClickListener {

    private PreferenceScreen mLogo;
    private Toast mToast;
    private int mLogoCountdown = 10;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        final Resources res = getResources();
        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mLogo = (PreferenceScreen) findPreference("ssos_logo");
        mLogo.setOnPreferenceClickListener(this);
        mToast = null;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.firmware_version;
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mLogoCountdown > 0) {
            mLogoCountdown--;
            if (mLogoCountdown == 0) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getContext(),
                        getResources().getString(R.string.emojii),
                        Toast.LENGTH_SHORT);
                mToast.show();
                EasterEgg EasterEggFragment = new EasterEgg();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(this.getId(), EasterEggFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getContext(),
                        getResources().getQuantityString(
                        R.plurals.show_logo_countdown, mLogoCountdown,
                        mLogoCountdown), Toast.LENGTH_SHORT);
                mToast.show();
            }
        }
        return true;
    }

    @Override
    protected String getLogTag() {
        return "FirmwareVersionSettings";
    }

    @Override
    public int getMetricsCategory() {
        return SettingsEnums.DIALOG_FIRMWARE_VERSION;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.firmware_version);
}
