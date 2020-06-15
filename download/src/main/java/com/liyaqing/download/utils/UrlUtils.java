package com.liyaqing.download.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.webkit.MimeTypeMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

        private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*(\"?)([^\"]*)\\1\\s*$", 2);

        public UrlUtils() {
        }

        public static String guessFileName(String url, String contentDisposition, String mimeType, String defaultFileName, String defaultExt) {
            String filename = null;
            String extension = null;
            int dotIndex;
            if (filename == null && contentDisposition != null) {
                filename = parseContentDisposition(contentDisposition);
                if (filename != null) {
                    dotIndex = filename.lastIndexOf(47) + 1;
                    if (dotIndex > 0) {
                        filename = filename.substring(dotIndex);
                    }
                }
            }

            int lastDotIndex;
            if (filename == null) {
                String decodedUrl = Uri.decode(url);
                if (decodedUrl != null) {
                    lastDotIndex = decodedUrl.indexOf(63);
                    if (lastDotIndex > 0) {
                        decodedUrl = decodedUrl.substring(0, lastDotIndex);
                    }

                    if (!decodedUrl.endsWith("/")) {
                        int index = decodedUrl.lastIndexOf(47) + 1;
                        if (index > 0) {
                            filename = decodedUrl.substring(index);
                        }
                    }
                }
            }

            if (filename == null) {
                filename = defaultFileName;
            }

            dotIndex = filename.indexOf(46);
            if (dotIndex < 0) {
                if (mimeType != null) {
                    extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                    if (extension != null) {
                        extension = "." + extension;
                    }
                }

                if (extension == null) {
                    if (mimeType != null && mimeType.toLowerCase(Locale.ROOT).startsWith("text/")) {
                        if (mimeType.equalsIgnoreCase("text/html")) {
                            extension = ".html";
                        } else {
                            extension = ".txt";
                        }
                    } else {
                        extension = "." + defaultExt;
                    }
                }
            } else {
                if (mimeType != null) {
                    lastDotIndex = filename.lastIndexOf(46);
                    String typeFromExt = MimeTypeMap.getSingleton().getMimeTypeFromExtension(filename.substring(lastDotIndex + 1));
                    if (typeFromExt != null && !typeFromExt.equalsIgnoreCase(mimeType)) {
                        extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                        if (extension != null) {
                            extension = "." + extension;
                        }
                    }
                }

                if (extension == null) {
                    extension = filename.substring(dotIndex);
                }

                filename = filename.substring(0, dotIndex);
            }

            return filename + extension;
        }

        static String parseContentDisposition(String contentDisposition) {
            try {
                Matcher m = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);
                if (m.find()) {
                    return m.group(2);
                }
            } catch (IllegalStateException var2) {
            }

            return null;
        }

        public static String MapToGETString(Map<String, String> data) {
            if (data == null) {
                return null;
            } else {
                int runstate = 0;
                StringBuilder stringbuild = new StringBuilder();
                Iterator it = data.entrySet().iterator();

                while (it.hasNext()) {
                    switch (runstate) {
                        case 0:
                            stringbuild.append('?');
                            runstate = 1;
                            break;
                        case 1:
                            stringbuild.append('&');
                    }

                    Map.Entry pair = (Map.Entry) it.next();
                    String key = (String) pair.getKey();
                    String value = String.valueOf(pair.getValue());
                    if (key != null && value != null) {
                        stringbuild.append(key);
                        stringbuild.append('=');

                        try {
                            String encodevalue = URLEncoder.encode(value, "UTF-8");
                            stringbuild.append(encodevalue);
                        } catch (UnsupportedEncodingException var8) {
                            stringbuild.append("none");
                            var8.printStackTrace();
                        }
                    }
                }

                return stringbuild.toString();
            }
        }

        public static boolean isValidUrl(String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            } else {
                Pattern p = Patterns.WEB_URL;
                Matcher m = p.matcher(url.toLowerCase());
                return m.matches();
            }
        }
}