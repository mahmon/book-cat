package com.mahmon.bookcat;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTaskRegister extends AsyncTask<String, Void, String> {

    AlertDialog alertDialog;
    Context context;

    public BackgroundTaskRegister(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Information");
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://www.mahmon.com/register.php";


        String user_name = params[1];
        String user_email = params[2];
        String user_password = params[3];
        try {
            URL url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("user_name", "UTF-8")
                    + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                    + URLEncoder.encode("user_email", "UTF-8") + "="
                    + URLEncoder.encode(user_email, "UTF-8") + "&"
                    + URLEncoder.encode("user_password", "UTF-8") + "="
                    + URLEncoder.encode(user_password, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();
            return "Registration success";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Registration success")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        } else {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }
}
