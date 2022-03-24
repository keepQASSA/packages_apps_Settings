
package com.android.settings.deviceinfo;

import android.os.SystemProperties;

public class VersionUtils {
    public static String getAOSQPVersion(){
        return SystemProperties.get("ro.aosqp.version.display","");
    }
}
