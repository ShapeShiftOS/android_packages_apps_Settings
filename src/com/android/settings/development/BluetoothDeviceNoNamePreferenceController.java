/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;

import androidx.annotation.VisibleForTesting;
import androidx.preference.Preference;
import com.ssos.support.preferences.SwitchPreference;

import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class BluetoothDeviceNoNamePreferenceController extends
        DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener,
        PreferenceControllerMixin {

    private static final String BLUETOOTH_SHOW_DEVICES_WITHOUT_NAMES_KEY =
            "bluetooth_show_devices_without_names";
    @VisibleForTesting
    static final String BLUETOOTH_SHOW_DEVICES_WITHOUT_NAMES_PROPERTY =
            "persist.bluetooth.showdeviceswithoutnames";

    public BluetoothDeviceNoNamePreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return BLUETOOTH_SHOW_DEVICES_WITHOUT_NAMES_KEY;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final boolean isEnabled = (Boolean) newValue;
        SystemProperties.set(BLUETOOTH_SHOW_DEVICES_WITHOUT_NAMES_PROPERTY,
                isEnabled ? "true" : "false");
        return true;
    }

    @Override
    public void updateState(Preference preference) {
        final boolean isEnabled = SystemProperties.getBoolean(
                BLUETOOTH_SHOW_DEVICES_WITHOUT_NAMES_PROPERTY, false /* default */);
        ((SwitchPreference) mPreference).setChecked(isEnabled);
    }

    @Override
    protected void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        SystemProperties.set(BLUETOOTH_SHOW_DEVICES_WITHOUT_NAMES_PROPERTY, "false");
        ((SwitchPreference) mPreference).setChecked(false);
    }
}
