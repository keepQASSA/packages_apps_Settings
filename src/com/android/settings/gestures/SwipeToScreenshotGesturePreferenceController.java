/*
 * Copyright (C) 2019 The PixelExperience Project
 * Copyright (C) 2022 QASSA
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

package com.android.settings.gestures;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.os.UserHandle;

import com.android.settings.R;

public class SwipeToScreenshotGesturePreferenceController extends GesturePreferenceController {

    public SwipeToScreenshotGesturePreferenceController(Context context,
            String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    protected String getVideoPrefKey() {
        return "";
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        return true;
    }

    @Override
    public boolean isChecked() {
        return true;
    }

    private boolean isSwipeToScreenshotGestureEnabled() {
        return Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.SWIPE_TO_SCREENSHOT, 0, UserHandle.USER_CURRENT) != 0;
    }

    @Override
    public CharSequence getSummary() {
        return mContext.getText(
                isSwipeToScreenshotGestureEnabled() ? R.string.gesture_setting_on : R.string.gesture_setting_off);
    }
}
