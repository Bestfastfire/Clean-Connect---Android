package com.cleancode.cleanconnect.conexao;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Upload_file extends AsyncTask<String, Void, String> {
    public interface onUploadListener { void onUpload(String tag, JSONArray result); }

    private ArrayList<uploadObj> files = new ArrayList<>();
    private final onUploadListener mListener;
    private int serverResponseCode;
    private final String tag;

    public Upload_file(ArrayList<uploadObj> files, String tag, android.support.v4.app.Fragment a){
        this.mListener = (onUploadListener) a;
        this.files = files;
        this.tag = tag;
    }

    public Upload_file(uploadObj file, String tag, android.support.v4.app.Fragment a){
        this.mListener = (onUploadListener) a;
        this.files.add(file);
        this.tag = tag;
    }

    public Upload_file(ArrayList<uploadObj> files, String tag, Activity a){
        this.mListener = (onUploadListener) a;
        this.files = files;
        this.tag = tag;
    }

    public Upload_file(uploadObj file, String tag, Activity a){
        this.mListener = (onUploadListener) a;
        this.files.add(file);
        this.tag = tag;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONArray arr = new JSONArray();

        for(uploadObj obj : files) {
            JSONObject json = new JSONObject();

            try { json.put("file", obj.getFile());
            } catch (JSONException e) { e.printStackTrace();}

            HttpURLConnection conn;
            DataOutputStream dos;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;

            try {
                FileInputStream fileInputStream = new FileInputStream(new File(obj.getFile()));
                URL url = new URL(obj.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("myFile", obj.getFile());
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + obj.getFile() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.e("connError", ex.toString());

            } catch (Exception e) {
                Log.e("connError", e.toString());
            }

            if (serverResponseCode == 200) {
                try { json.put("upload", true);
                } catch (JSONException e) { e.printStackTrace();}

                Log.e("Upload succes", obj.getFile());
                if(obj.isDelete()) {
                    File pa = new File(obj.getFile());
                    if (pa.exists()) {
                        if (pa.delete());
                    }
                }
            } else {
                try { json.put("upload", false);
                } catch (JSONException e) { e.printStackTrace();}
                Log.e("Upload erro", obj.getFile());
            }

            arr.put(json);
        }

        return arr.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            mListener.onUpload(tag, new JSONArray(result));

        } catch (JSONException e) {
            mListener.onUpload(tag, new JSONArray());

        }
    }
}