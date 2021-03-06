package org.anrdigital.rebootdeckbuilder.helper;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by sebast on 02/07/16.
 */

public class StringDownloader extends AsyncTask<Void, Integer, String> {

    private final Context mContext;
    private URL mUrl;
    private final String mFilename;
    private final FileDownloaderListener mListener;

    public interface FileDownloaderListener {
        void onBeforeTask();

        void onTaskComplete(String s);

        void onError(Exception e);
    }

    public StringDownloader(Context context, String url, String filename, FileDownloaderListener listener) {
        this.mContext = context;
        this.mFilename = filename;
        this.mListener = listener;

        try {
            this.mUrl = new URL(url);
        } catch (MalformedURLException e) {
            mListener.onError(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        mListener.onBeforeTask();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            // Open the cconnection
            HttpsURLConnection conn = (HttpsURLConnection) this.mUrl.openConnection();
            conn.setSSLSocketFactory(getTrustAllSocketFactory().getSocketFactory());
            conn.connect();

            // Download if success
            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                String theFile = streamToString(conn.getInputStream());
                return theFile;
            } else {
                return null;
            }
        } catch (Exception e) {
            mListener.onError(e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        // Do nothing if string is null
        if (s == null) {
            mListener.onError(new FileNotFoundException());
            return;
        }

        // Save the file to the requested filename
        try {
            // Delete the old file
            File f = new File(mContext.getFilesDir(), mFilename);
            if (f.exists()) {
                f.delete();
            }

            // Save the file
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            fileOutputStream.write(s.getBytes());
            fileOutputStream.close();

            // We are done!
            mListener.onTaskComplete(s);
        } catch (IOException e) {
            mListener.onError(e);
            e.printStackTrace();
        }


    }

    private String streamToString(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private SSLContext getTrustAllSocketFactory() {
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
