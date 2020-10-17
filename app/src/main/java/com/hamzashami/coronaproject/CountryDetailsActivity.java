package com.hamzashami.coronaproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hamzashami.coronaproject.adapters.MainAdapter;
import com.hamzashami.coronaproject.model.Country;
import com.hamzashami.coronaproject.model.MainItem;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.hamzashami.coronaproject.fragments.mainFragments.MainFragment.COUNTRY_API;


public class CountryDetailsActivity extends AppCompatActivity {
    private static final String TAG = "CountryDetailsActivity";
    public static final String COUNTRY_FLAG_API_1 = "https://www.countryflags.io/";
    public static final String COUNTRY_FLAG_API_2 = "/flat/64.png";
    private Country country;
    private ImageView iv_countryFlag;
    private TextView tv_countryCode;
    private RecyclerView rv_countryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("country_id")) {
            Toast.makeText(this, "There is no Country have this id", Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(R.layout.activity_country_details);
        int countryId = intent.getIntExtra("country_id", -1);
        if (countryId == -1) {
            Toast.makeText(this, "There is no Country have this id", Toast.LENGTH_SHORT).show();
            finish();
        }

        iv_countryFlag = findViewById(R.id.iv_countryFlag);
        tv_countryCode = findViewById(R.id.tv_countryCode);
        rv_countryData = findViewById(R.id.rv_countryData);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, COUNTRY_API + "/" + countryId, null, response -> {
            try {
                JSONObject jsonObject = response.getJSONObject("location");
                Log.d(TAG, "getCountriesData: " + jsonObject.toString());

                int id = jsonObject.getInt("id");
                String countryName = jsonObject.getString("country");
                setTitle(countryName);
                String countryCode = jsonObject.getString("country_code");
                long countryPopulation;
                if (jsonObject.has("country_population") && !jsonObject.isNull("country_population")) {
                    countryPopulation = jsonObject.getLong("country_population");
                } else {
                    countryPopulation = 0;
                }

                JSONObject jsonObject1 = jsonObject.getJSONObject("latest");
                int confirmed = jsonObject1.getInt("confirmed");
                int deaths = jsonObject1.getInt("deaths");
                int recovered = jsonObject1.getInt("recovered");

                JSONObject jsonObject2 = jsonObject.getJSONObject("coordinates");
                double latitude = jsonObject2.getDouble("latitude");
                double longitude = jsonObject2.getDouble("longitude");

                String lastUpdated = jsonObject.getString("last_updated");

                country = new Country(id, countryName, countryCode, countryPopulation, confirmed, deaths, recovered, lastUpdated);
                country.setLat(latitude);
                country.setLng(longitude);

                String countryFlagUrl = COUNTRY_FLAG_API_1 + countryCode + COUNTRY_FLAG_API_2;
                if (TextUtils.isEmpty(countryCode)) {
                    iv_countryFlag.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Log.d(TAG, "onBindViewHolder: Flag url " + countryFlagUrl);
                    Picasso.get().load(countryFlagUrl).into(iv_countryFlag);
                }
                tv_countryCode.setText(countryName.toUpperCase() + "(" + countryCode + ")");
                getMainData(country);
            } catch (Exception e) {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }

    private void getMainData(Country country) {
        ArrayList<MainItem> mainItems = new ArrayList<>();
        MainAdapter mainAdapter = new MainAdapter(getApplicationContext(), mainItems);

        rv_countryData.setHasFixedSize(true);
        rv_countryData.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rv_countryData.setAdapter(mainAdapter);

        mainItems.add(new MainItem("Confirmed", country.getConfirmed(), android.R.color.black));
        mainItems.add(new MainItem("Recovered", country.getRecovered(), android.R.color.holo_green_light));
        mainItems.add(new MainItem("Deaths", country.getDeaths(), android.R.color.holo_red_dark));
        mainAdapter.notifyDataSetChanged();
    }

}
