package org.anrdigital.rebootdeckbuilder.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ImageDownloadUtil {

    public static Bitmap downloadImageToCache(Context context, URL imageSrc, String localFileName) throws Exception {
        try {
            URLConnection conn = imageSrc.openConnection();
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection https = (HttpsURLConnection) conn;
                https.setSSLSocketFactory(getTrustAllSocketFactory().getSocketFactory());
            }
            Bitmap theImage = BitmapFactory.decodeStream(conn.getInputStream());
            //FileOutputStream out = context.openFileOutput(card.getImageFileName(), Context.MODE_PRIVATE);
            FileOutputStream out = new FileOutputStream(new File(context.getCacheDir(), localFileName));
            theImage.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            return theImage;
        } catch (Exception e) {
            Log.e("LOG", "Could not download image: " + imageSrc.toString());
            throw e;
        }
    }

    private static SSLContext getTrustAllSocketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            return sc;
        } catch (Exception ignored) {
        }
        return null;
    }

}
