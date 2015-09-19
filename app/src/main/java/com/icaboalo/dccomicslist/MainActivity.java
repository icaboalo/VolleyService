package com.icaboalo.dccomicslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements MyOnItemClickListener{

    private List<JusticeLeague> myJusticeLeagueList;
    HeroRecyclerViewAdapter heroRecyclerViewAdapter;
    RecyclerView heroRecylerView;

    EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        setupRecyclerView();

        MyAsyncTask peticion = new MyAsyncTask();
        peticion.execute("Daft");


    }

    @Override
    protected void onResume() {
        super.onResume();
        searchInput = (EditText) findViewById(R.id.search_input);
        heroRecylerView = (RecyclerView) findViewById(R.id.character_list);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (searchInput.getText().toString().isEmpty()){
                    executeRequestWithVolley(" ");
                    heroRecylerView.setVisibility(View.INVISIBLE);

                }else{
                    executeRequestWithVolley(searchInput.getText().toString());
                    heroRecylerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupRecyclerView() {
        heroRecyclerViewAdapter = new HeroRecyclerViewAdapter(this, createHero());


        RecyclerView heroRecyclerView = (RecyclerView) findViewById(R.id.character_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this, 2);
        heroRecyclerView.setLayoutManager(linearLayoutManager);
        heroRecyclerView.setHasFixedSize(true);

        heroRecyclerView.setAdapter(heroRecyclerViewAdapter);

        heroRecyclerViewAdapter.setMyOnItemClickListener(this);
    }

    private List<JusticeLeague> createHero(){
        myJusticeLeagueList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            myJusticeLeagueList.add(new JusticeLeague("0000", "Test Hero"));
        }
        return myJusticeLeagueList;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(MainActivity.this, "Hero Name: " + myJusticeLeagueList.get(position).getHeroName(), Toast.LENGTH_SHORT).show();

    }
    public void executeRequestWithVolley(String query){
        String url = "https://api.spotify.com/v1/search?type=artist&q="+query;

        StringRequest requestString = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    List test = answerParsing(response);
                    heroRecyclerViewAdapter.setData(test);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        JsonRequest requestJSON = new JsonRequest<JSONObject>(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response", response.toString());
                try {
                    List test = answerParsing(response.toString());
                    heroRecyclerViewAdapter.setData(test);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                return null;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(requestString);
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
            String id = artist.getString("id");
            JusticeLeague justiceLeague = new JusticeLeague(id, name);

                if (imagesJSONArray.length() >0){

                JSONObject imageJSON = imagesJSONArray.getJSONObject(0);
                String image = imageJSON.getString("url");
                Log.d("Image", "asasa"+image);
                justiceLeague.setHeroImage(image);
                //justiceLeague.getHeroList().add(image);
                //Log.d(LOG_TAG, image);
            }
            artists.add(justiceLeague);
        }
        return artists;
    }
}
