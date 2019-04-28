package com.cleancode.cleanconnect.conexao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class conexao {
    static public final String POST = "POST";
    static public final String GET = "GET";

    public static String postDados(String urlUsuario, String type, String parametrosUsuario) {
        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlUsuario);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(type);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.setRequestProperty("Content-Language", "pt-BR");

            if(type.contains("POST")){
                connection.setRequestProperty("Content-Lenght", "" + Integer.toString(parametrosUsuario.getBytes().length));
                connection.setDoInput(true);
                connection.setDoOutput(true);
            }

            connection.setUseCaches(false);

            if(type.contains("POST")) {
                OutputStreamWriter outPutStream = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
                outPutStream.write(parametrosUsuario);
                outPutStream.flush();
                outPutStream.close();
            }

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            String linha;
            StringBuilder resposta = new StringBuilder();

            while((linha = bufferedReader.readLine()) != null) {
                resposta.append(linha);
                resposta.append('\r');
            }

            bufferedReader.close();

            return resposta.toString();

        } catch (Exception erro) {
            return null;

        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}