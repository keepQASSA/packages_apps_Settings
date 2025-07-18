/*
 * Copyright (C) 2019 Xtended
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

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.os.SystemProperties;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class AboutDeviceNamePreferenceController extends BasePreferenceController {

    private static final String TAG = "AboutDeviceNameCtrl";

    private static final String KEY_DEVICE_NAME_PROP = "ro.product.device";
    private static final String KEY_DEVICE_MODEL_PROP = "ro.product.model";

    public AboutDeviceNamePreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public CharSequence getSummary() {
	String deviceName = SystemProperties.get(KEY_DEVICE_NAME_PROP);
	String deviceModel =  SystemProperties.get(KEY_DEVICE_MODEL_PROP);
	if (!deviceName.isEmpty() && !deviceModel.isEmpty())
	    return deviceName + " (" + deviceModel + ")";
	else
            return mContext.getString(R.string.unknown);
    }
}
