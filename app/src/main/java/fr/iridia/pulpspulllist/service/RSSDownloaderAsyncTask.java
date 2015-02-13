package fr.iridia.pulpspulllist.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RSSDownloaderAsyncTask extends AsyncTask<Void, Integer, Integer> {

    public static final String TAG = "RSSDownloaderAsyncTask";

    protected URL       url;
    protected String    localFile;
    protected Context   context;

    protected int       fileLength;

    public RSSDownloaderAsyncTask(Context context, URL url, String localFile) {
        this.url = url;
        this.localFile = localFile;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return -1;
            }

            fileLength = connection.getContentLength();

            input = connection.getInputStream();
            output = new FileOutputStream(localFile);
            Log.i(TAG, localFile);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                publishProgress((int)total);
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            if (connection != null)
                connection.disconnect();
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.i(TAG, values[0].toString());
    }
}
