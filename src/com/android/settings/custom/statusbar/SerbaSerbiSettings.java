/*
 * Copyright (C) 2014-2015 The CyanogenMod Project
 *               2017-2019 The LineageOS Project
 *               2022 AOSQP
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
package com.android.settings.custom.statusbar;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.settings.custom.preference.SystemSettingListPreference;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.Set;

public class SerbaSerbiSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String ICON_BLACKLIST = "icon_blacklist";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.serba_serbi_settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        final String curIconBlacklist = Settings.Secure.getString(getContext().getContentResolver(),
                ICON_BLACKLIST);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.CUSTOM_SETTINGS;
    }
}
