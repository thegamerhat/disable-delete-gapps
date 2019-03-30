package org.droidtr.deletegapps;

import android.content.Context;
import android.os.AsyncTask;
import java.io.DataOutputStream;

public class Run extends AsyncTask<String, Integer, String> {

    private Context ctx;

    public Run(Context context) {
        ctx = context;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes(sUrl[0]);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Exception e) {
            return e.toString();
        }
        return null;
    }
}
