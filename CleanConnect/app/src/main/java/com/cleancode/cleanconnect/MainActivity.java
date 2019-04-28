package com.cleancode.cleanconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cleancode.cleanconnect.conexao.Send_date;
import com.cleancode.cleanconnect.conexao.Upload_file;
import com.cleancode.cleanconnect.conexao.conexao;
import com.cleancode.cleanconnect.conexao.uploadObj;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Send_date.onPostListener, Upload_file.onUploadListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Send Request, you can use "this" in last parameter to activities and fragment, it works
        new Send_date(conexao.POST, "post=yourPost" + "&post2=yourPost2...", "tag", this).execute("MY_URL?" + "get=abc");
        new Send_date(conexao.GET, null, "tag", this).execute("MY_URL" + "get=abc");

        //Upload any file
        //_You can pass one uploadObj_//
        //Last parameter in uploadObj, "delete", is if you want delete file after upload
        //You too can use "this" in last parameter to activities and fragments, it works
        new Upload_file(new uploadObj("urlFile", "urlToSendFile", false), "tag", this).execute("MY_URL");

        //_Or one ArrayList of uploadObj_//
        ArrayList<uploadObj> files = new ArrayList<>();
        files.add(new uploadObj("urlFile", "urlToSendFile", false));
        new Upload_file(files, "tag", this).execute("MY_URL");
    }

    @Override
    public void onPost(String tag, String result) {
        //case success
        //it return your tag and the result
    }

    @Override
    public void onError(String tag) {
        //case error
        //it return the tag
    }

    @Override
    public void onUpload(String tag, JSONArray result) {
        //it return your tag and JSONArray with files states
        //0 -> JSONObject -> {"file" : "fileUrl", "upload" : true/false]...
    }
}
