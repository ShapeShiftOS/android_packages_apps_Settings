/*
 * Copyright (C) 2019 The Android Open Source Project
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

import static com.google.common.truth.Truth.assertThat;

import androidx.fragment.app.FragmentActivity;
import com.ssos.support.preferences.SwitchPreference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;

@RunWith(RobolectricTestRunner.class)
public class SelectDSUPreferenceControllerTest {

    @Mock
    private SwitchPreference mPreference;

    private FragmentActivity mActivity;
    private SelectDSUPreferenceController mController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mActivity = Robolectric.buildActivity(FragmentActivity.class).get();
        mController = new SelectDSUPreferenceController(mActivity);
        mPreference = new SwitchPreference(mActivity);
        mPreference.setKey("dsu_loader");
    }

    @Test
    public void handlePreferenceTreeClick_shouldLaunchCorrectIntent() {
        mController.handlePreferenceTreeClick(mPreference);

        assertThat(Shadows.shadowOf(mActivity)
                .getNextStartedActivityForResult().intent.getComponent().getClassName())
                .isEqualTo("com.android.settings.development.DSULoader");
    }
}
