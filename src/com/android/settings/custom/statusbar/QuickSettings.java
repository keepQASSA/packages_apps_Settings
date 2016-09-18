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

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.ArraySet;
import android.view.View;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.settings.custom.preference.CustomSeekBarPreference;
import com.android.settings.custom.preference.SystemSettingListPreference;
import com.android.settings.custom.preference.SystemSettingSwitchPreference;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.util.custom.ActionUtils;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.Set;

public class QuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String CATEGORY_BRIGHTNESS = "status_bar_brightness_category";
    private static final String CATEGORY_QS_ANIMATION = "quick_settings_animations";

    private static final String STATUS_BAR_QUICK_QS_PULLDOWN = "qs_quick_pulldown";
    private static final String STATUS_BAR_QUICK_QS_SHOW_AUTO_BRIGHTNESS = "qs_show_auto_brightness";
    private static final String STATUS_BAR_QUICK_QS_ANIMATION_STYLE = "anim_tile_style";
    private static final String STATUS_BAR_QUICK_QS_ANIMATION_TILE_DURATION = "anim_tile_duration";
    private static final String STATUS_BAR_QUICK_QS_ANIMATION_TILE_INTERPOLATOR = "anim_tile_interpolator";
    private static final String HEADER_ICONS_STYLE = "headers_icons_style";
    private static final String QS_BLUR_RADIUS = "qs_blur_radius";

    private static final int PULLDOWN_DIR_NONE = 0;
    private static final int PULLDOWN_DIR_RIGHT = 1;
    private static final int PULLDOWN_DIR_LEFT = 2;

    private SystemSettingListPreference mQuickPulldown;
    private SystemSettingListPreference mStatusBarQsAnimationStyle;
    private SystemSettingListPreference mStatusBarQsAnimationTileDuration;
    private SystemSettingListPreference mStatusBarQsAnimationTileInterpolator;

    private SystemSettingSwitchPreference mHeaderIconsStyle;

    private SwitchPreference mStatusBarQsShowAutoBrightness;

    private CustomSeekBarPreference mQsBlurRadius;

    private PreferenceCategory mStatusBarBrightnessCategory;
    private PreferenceCategory mStatusBarQsAnimationCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.quick_settings);

        final ContentResolver resolver = getActivity().getContentResolver();

        mQuickPulldown =
                (SystemSettingListPreference) findPreference(STATUS_BAR_QUICK_QS_PULLDOWN);
        mQuickPulldown.setOnPreferenceChangeListener(this);
        updateQuickPulldownSummary(mQuickPulldown.getIntValue(0));

        mStatusBarBrightnessCategory =
                (PreferenceCategory) getPreferenceScreen().findPreference(CATEGORY_BRIGHTNESS);
        mStatusBarQsShowAutoBrightness = mStatusBarBrightnessCategory.findPreference(STATUS_BAR_QUICK_QS_SHOW_AUTO_BRIGHTNESS);
        if (!getResources().getBoolean(
                com.android.internal.R.bool.config_automatic_brightness_available)){
            mStatusBarBrightnessCategory.removePreference(mStatusBarQsShowAutoBrightness);
        }

        mStatusBarQsAnimationCategory =
                (PreferenceCategory) getPreferenceScreen().findPreference(CATEGORY_QS_ANIMATION);

        mStatusBarQsAnimationStyle =
                (SystemSettingListPreference) mStatusBarQsAnimationCategory.findPreference(STATUS_BAR_QUICK_QS_ANIMATION_STYLE);
        mStatusBarQsAnimationStyle.setOnPreferenceChangeListener(this);

        mStatusBarQsAnimationTileDuration =
                (SystemSettingListPreference) mStatusBarQsAnimationCategory.findPreference(STATUS_BAR_QUICK_QS_ANIMATION_TILE_DURATION);
        mStatusBarQsAnimationTileDuration.setOnPreferenceChangeListener(this);

        mStatusBarQsAnimationTileInterpolator =
                (SystemSettingListPreference) mStatusBarQsAnimationCategory.findPreference(STATUS_BAR_QUICK_QS_ANIMATION_TILE_INTERPOLATOR);
        mStatusBarQsAnimationTileInterpolator.setOnPreferenceChangeListener(this);

        updateQsAnimationDependents(Integer.parseInt(mStatusBarQsAnimationStyle.getValue()));

        mHeaderIconsStyle = findPreference(HEADER_ICONS_STYLE);
        mHeaderIconsStyle.setChecked((Settings.System.getInt(resolver,
                Settings.System.HEADER_ICONS_STYLE, 0) == 1));
        mHeaderIconsStyle.setOnPreferenceChangeListener(this);

        mQsBlurRadius = (CustomSeekBarPreference) findPreference(QS_BLUR_RADIUS);
        final int blurRadius = Settings.System.getInt(resolver,
                Settings.System.QS_BLUR_RADIUS, 0);
            mQsBlurRadius.setValue((blurRadius));
            mQsBlurRadius.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Adjust status bar preferences for RTL
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mQuickPulldown.setEntries(R.array.status_bar_quick_qs_pulldown_entries_rtl);
            mQuickPulldown.setEntryValues(R.array.status_bar_quick_qs_pulldown_values);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    final ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mHeaderIconsStyle) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.HEADER_ICONS_STYLE, value ? 1 : 0);
            ActionUtils.showSystemUiRestartDialog(getContext());
            return true;
        }
        int value = Integer.parseInt((String) newValue);
        String key = preference.getKey();
        switch (key) {
            case STATUS_BAR_QUICK_QS_PULLDOWN:
                updateQuickPulldownSummary(value);
                break;
        case STATUS_BAR_QUICK_QS_ANIMATION_STYLE:
                updateQsAnimationDependents(value);
                break;
        case QS_BLUR_RADIUS:
                Integer blurRadius = (Integer) newValue;
                Settings.System.putInt(resolver,
                        Settings.System.QS_BLUR_RADIUS, blurRadius);
                return true;
        }
        return true;
    }

    private void updateQsAnimationDependents(int value){
        mStatusBarQsAnimationTileDuration.setEnabled(value != 0);
        mStatusBarQsAnimationTileInterpolator.setEnabled(value != 0);
    }

    private void updateQuickPulldownSummary(int value) {
        String summary="";
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL){
            if (value == PULLDOWN_DIR_LEFT) {
                value = PULLDOWN_DIR_RIGHT;
            }else if (value == PULLDOWN_DIR_RIGHT) {
                value = PULLDOWN_DIR_LEFT;
            }
        }
        switch (value) {
            case PULLDOWN_DIR_NONE:
                summary = getResources().getString(
                    R.string.status_bar_quick_qs_pulldown_off);
                break;
            case PULLDOWN_DIR_LEFT:
                summary = getResources().getString(
                    R.string.status_bar_quick_qs_pulldown_summary_left_edge);
                break;
            case PULLDOWN_DIR_RIGHT:
                summary = getResources().getString(
                    R.string.status_bar_quick_qs_pulldown_summary_right_edge);
                break;
        }
        mQuickPulldown.setSummary(summary);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.CUSTOM_SETTINGS;
    }
}
