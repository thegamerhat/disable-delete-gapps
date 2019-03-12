package org.droidtr.deletegapps;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;

    public Download(Context context) {
        this.context = context;
    }

    public void run(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
        dataOutputStream.writeBytes(cmd);
        dataOutputStream.flush();
        dataOutputStream.close();


    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(new File("/data/data/org.droidtr.deletegapps/tmp"));

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
            output.close();
            run("mount -o remount,rw /system");
            run("umount /system/etc/hosts");
            run("cp -prfv /data/data/org.droidtr.deletegapps/tmp " + sUrl[1]);
            run("rm -f /data/data/org.droidtr.deletegapps/tmp");
            run("chmod 644 /system/etc/hosts");
        } catch (Exception e) {
            return e.toString();
        }
        return null;
    }
}
