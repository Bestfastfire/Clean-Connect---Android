package com.cleancode.cleanconnect.conexao;

import android.app.Activity;
import android.os.AsyncTask;

public class Send_date extends AsyncTask<String, Void, String> {
    public interface onPostListener {void onPost(String tag, String result); void onError(String tag); }

    private final onPostListener mListener;
    private final String parametros;
    private final String type;
    private final String tag;

    public Send_date(String type, String paramentros, String tag, Activity a){
        this.mListener = (onPostListener) a;
        this.parametros = paramentros;
        this.type = type;
        this.tag = tag;
    }

    public Send_date(String type, String paramentros, String tag, android.support.v4.app.Fragment a) {
        this.mListener = (onPostListener) a;
        this.parametros = paramentros;
        this.type = type;
        this.tag = tag;
    }

    @Override
    protected String doInBackground(String... urls) {
        return conexao.postDados(urls[0], type, this.parametros);
    }

    @Override
    public void onPostExecute(String result) {
        if(result != null) mListener.onPost(tag, result);
        else mListener.onError(tag);
    }
}
