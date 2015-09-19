package com.icaboalo.dccomicslist;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by icaboalo on 9/14/2015.
 */
public class MyAsyncTask extends AsyncTask<String, Void, List<JusticeLeague>> {

    public static final String LOG_TAG = "LogTag";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List doInBackground(String... query) {
        if (query[0] == null) {
            Log.e(LOG_TAG, "Error");
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String querySearch = query[0];
        String answer;

        try {
            URL url = new URL("https://api.spotify.com/v1/search?type=artist&q=" + querySearch);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder answerConstructor = new StringBuilder();
            if (inputStream == null) {
                Log.e(LOG_TAG, "No hay respuesta");
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                answerConstructor.append(line)
                        .append("\n");
            }
            if (answerConstructor.length() == 0) {
                Log.e(LOG_TAG, "Respuesta Vacia");
                return null;
            }

            answer = answerConstructor.toString();
            return answerParsing(answer);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL erronea");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Hubo un error abriendo la conexion");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Hubo un error en el parseo");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "No se pudo cerrar el Buffer");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<JusticeLeague> justiceLeagues) {
        for (int i = 0; i < justiceLeagues.size(); i++) {
            Log.d(LOG_TAG, justiceLeagues.get(i).getHeroName());
            for (int j = 0; j < justiceLeagues.get(i).getHeroList().size(); j++) {
                Log.d(LOG_TAG, justiceLeagues.get(i).getHeroList().get(j));
            }
        }
    }

public List<JusticeLeague> answerParsing(String answer) throws JSONException {
        ArrayList<JusticeLeague> artists = new ArrayList<>();
        JSONObject answerJSON = new JSONObject(answer);
        JSONObject artistsJSON = answerJSON.getJSONObject("artists");
        JSONArray itemsJSONArray = artistsJSON.getJSONArray("items");

        int itemsLenght = itemsJSONArray.length();
        for (int i = 0; i < itemsLenght; i++) {
            JSONObject artist = itemsJSONArray.getJSONObject(i);
            String name = artist.getString("name");
            JSONArray imagesJSONArray = artist.getJSONArray("images");

            JusticeLeague justiceLeague = new JusticeLeague(name);

            for (int j = 0; j < imagesJSONArray.length(); j++) {
                JSONObject imageJSON = imagesJSONArray.getJSONObject(j);
                String image = imageJSON.getString("url");
                justiceLeague.getHeroList().add(image);
                //Log.d(LOG_TAG, image);
            }
            artists.add(justiceLeague);
        }
        return artists;
    }
}
