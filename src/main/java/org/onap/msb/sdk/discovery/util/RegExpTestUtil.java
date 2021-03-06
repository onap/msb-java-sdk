/*******************************************************************************
 * Copyright 2017 ZTE, Inc. and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.onap.msb.sdk.discovery.util;

import java.util.regex.Pattern;

public class RegExpTestUtil {

    public static boolean serviceNameRegExpTest(String serviceName) {

        String serviceNameReg = "^([0-9a-zA-Z]|-|_)*$";
        return Pattern.matches(serviceNameReg, serviceName);

    }


    public static boolean hostRegExpTest(String host) {

        String hostReg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                        + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                        + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                        + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)" + ":(\\d{1,5})$";
        return Pattern.matches(hostReg, host);

    }

    public static boolean ipRegExpTest(String ip) {

        String hostReg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                        + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                        + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                        + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        return Pattern.matches(hostReg, ip);

    }

    public static boolean portRegExpTest(String port) {
        String hostReg = "^\\d{1,5}$";
        if (Pattern.matches(hostReg, port)) {
            int iport = Integer.parseInt(port);
            if (iport >= 1 && iport <= 65535)
                return true;
        }

        return false;

    }

    public static boolean versionRegExpTest(String version) {

        String versionReg = "^v\\d+(\\.\\d+)?$";
        return Pattern.matches(versionReg, version);

    }

    public static boolean urlRegExpTest(String url) {
        if (url.equals("/"))
            return true;
        String urlReg = "^\\/.*((?!\\/).)$";
        return Pattern.matches(urlReg, url);

    }



}
