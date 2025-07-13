/*
 * Copyright (C) 2017 The halogenOS Project
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

    public class QASSAVersionPreferenceController extends BasePreferenceController {

    private static final Uri INTENT_URI_DATA = Uri.parse("https://t.me/ngantuprjkt/");
    private static final String TAG = "qassaDialogCtrl";
    private static final String QASSA_VERSION_NUMBER = "ro.qassa.version.number";
    private static final String QASSA_BUILD_CODENAME = "ro.qassa.build.codename";
    private static final String QASSA_BUILD_TYPE = "ro.qassa.build.type";
    private final PackageManager mPackageManager = this.mContext.getPackageManager();

    public QASSAVersionPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    public CharSequence getSummary() {
        String qassaVersion = SystemProperties.get(QASSA_VERSION_NUMBER,
                mContext.getString(R.string.device_info_default));
        String qassaCodename =  SystemProperties.get(QASSA_BUILD_CODENAME,
                this.mContext.getString(R.string.device_info_default));
        String qassaReleasetype =  SystemProperties.get(QASSA_BUILD_TYPE,
                this.mContext.getString(R.string.device_info_default));
        if (!qassaVersion.isEmpty() && !qassaCodename.isEmpty())
            return qassaVersion + " | " + qassaCodename + " | " + qassaReleasetype;
        else
            return mContext.getString(R.string.unknown);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(INTENT_URI_DATA);
        if (this.mPackageManager.queryIntentActivities(intent, 0).isEmpty()) {
            Log.w(TAG, "queryIntentActivities() returns empty");
            return true;
        }
        this.mContext.startActivity(intent);
        return true;
    }
}
