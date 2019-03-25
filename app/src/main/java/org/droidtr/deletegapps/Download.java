package org.droidtr.deletegapps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download extends AsyncTask<String, Integer, String> {

    private Context ctx;
    ProgressDialog dialog;

    public Download(Context context) {
        ctx = context;
        if(Build.VERSION.SDK_INT>21){
            ctx.setTheme(android.R.style.Theme_Material_Dialog);
        }else if(Build.VERSION.SDK_INT>14){
            ctx.setTheme(android.R.style.Theme_Holo_Dialog);
        }else{
            ctx.setTheme(android.R.style.Theme_Dialog);
        }
        dialog = new ProgressDialog(ctx);
    }

    public void run(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
        dataOutputStream.writeBytes(cmd);
        dataOutputStream.flush();
        dataOutputStream.close();


    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("Please Wait");
        dialog.setMessage("Downloading...");
        dialog.show();

    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
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
            String name=SystemClock.currentThreadTimeMillis()+"";
            output = new FileOutputStream(new File("/data/data/org.droidtr.deletegapps/"+name));

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
            run("umount "+sUrl[1]);
            run("cp -prfv /data/data/org.droidtr.deletegapps/"+name+" " + sUrl[1]);
            run("rm -f /data/data/org.droidtr.deletegapps/"+name);
            run("chmod 644 "+sUrl[1]);
        } catch (Exception e) {
            return e.toString();
        }
        return null;
    }
}
