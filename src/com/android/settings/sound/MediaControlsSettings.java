/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.settings.sound;

import android.app.settings.SettingsEnums;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.SettingsPreferenceFragment;

import com.ssos.support.preferences.SystemSettingSwitchPreference;

/**
 * Media control settings located in the sound menu
 */
@SearchIndexable
public class MediaControlsSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String QS_MEDIA_PLAYER = "qs_media_player";

    private SystemSettingSwitchPreference mQSMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.media_controls_settings);
        PreferenceScreen prefScreen = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        mQSMediaPlayer = findPreference(QS_MEDIA_PLAYER);
        mQSMediaPlayer.setChecked((Settings.System.getInt(resolver,
                Settings.System.QS_MEDIA_PLAYER, 1) == 1));
        mQSMediaPlayer.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mQSMediaPlayer) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.QS_MEDIA_PLAYER, value ? 1 : 0);
            com.ssos.shapeshifter.utils.Utils.showSystemUiRestartDialog(getContext());
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return SettingsEnums.MEDIA_CONTROLS_SETTINGS;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.media_controls_settings);
}
