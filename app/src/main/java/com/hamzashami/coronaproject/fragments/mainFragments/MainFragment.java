package com.hamzashami.coronaproject.fragments.mainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.adapters.CountryAdapter;
import com.hamzashami.coronaproject.adapters.MainAdapter;
import com.hamzashami.coronaproject.model.Country;
import com.hamzashami.coronaproject.model.MainItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private FirebaseAuth auth;
    public static final String MAIN_API = "https://corona.lmao.ninja/v2/all";
    public static final String COUNTRY_API = "https://coronavirus-tracker-api.herokuapp.com/v2/locations";
    private TextView tv_date;
    private SwipeRefreshLayout srl_main;
    private RecyclerView rv_mainData;
    private RecyclerView rv_countries;
    private ConstraintLayout cl_mainLoadingOverlay;
    private ConstraintLayout cl_countryLoadingOverlay;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        srl_main = view.findViewById(R.id.srl_main);

        tv_date = view.findViewById(R.id.tv_date);
        rv_mainData = view.findViewById(R.id.rv_mainData);
        cl_mainLoadingOverlay = view.findViewById(R.id.cl_mainLoadingOverlay);
        cl_countryLoadingOverlay = view.findViewById(R.id.cl_countryLoadingOverlay);
        rv_countries = view.findViewById(R.id.rv_countries);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        srl_main.setOnRefreshListener(() -> {
            srl_main.setRefreshing(false);
            getMainData();
            getCountriesData();
        });
        getMainData();
        getCountriesData();
    }

    private void getCountriesData() {
        cl_countryLoadingOverlay.setVisibility(View.VISIBLE);
        ArrayList<Country> countries = new ArrayList<>();
        CountryAdapter countryAdapter = new CountryAdapter(getContext(), countries);

        rv_countries.setHasFixedSize(true);
        rv_countries.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rv_countries.setAdapter(countryAdapter);

        ViewCompat.setNestedScrollingEnabled(rv_countries, false);
        rv_countries.setNestedScrollingEnabled(false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, COUNTRY_API, null, response -> {
            try {
                cl_countryLoadingOverlay.setVisibility(View.GONE);
                JSONArray jsonArray = (JSONArray) response.get("locations");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d(TAG, "getCountriesData: " + jsonObject.toString());

                    int id = jsonObject.getInt("id");
                    String countryName = jsonObject.getString("country");
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

                    String lastUpdated = jsonObject.getString("last_updated");

                    Country country = new Country(id, countryName, countryCode, countryPopulation, confirmed, deaths, recovered, lastUpdated);
                    countries.add(country);
                    countryAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, error -> {
            cl_countryLoadingOverlay.setVisibility(View.GONE);
            error.printStackTrace();
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    private void getMainData() {
        cl_mainLoadingOverlay.setVisibility(View.VISIBLE);
        ArrayList<MainItem> mainItems = new ArrayList<>();
        MainAdapter mainAdapter = new MainAdapter(getContext(), mainItems);

        rv_mainData.setHasFixedSize(true);
        rv_mainData.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_mainData.setAdapter(mainAdapter);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, MAIN_API, null, response -> {
            try {
                cl_mainLoadingOverlay.setVisibility(View.GONE);
                long updated = response.getLong("updated");
                tv_date.setText("Last Update: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(updated)));
                int cases = response.getInt("cases");
                mainItems.add(new MainItem("Cases", cases, android.R.color.black));

                int todayCases = response.getInt("todayCases");
                mainItems.add(new MainItem("Today Cases", todayCases, android.R.color.holo_orange_light));

                int deaths = response.getInt("deaths");
                mainItems.add(new MainItem("Deaths", deaths, android.R.color.holo_red_dark));

                int todayDeaths = response.getInt("todayDeaths");
                mainItems.add(new MainItem("Today Deaths", todayDeaths, android.R.color.holo_red_light));

                int recovered = response.getInt("recovered");
                mainItems.add(new MainItem("Recovered", recovered, android.R.color.holo_green_light));

                int active = response.getInt("active");
                mainItems.add(new MainItem("Active", active, android.R.color.darker_gray));

                int critical = response.getInt("critical");
                mainItems.add(new MainItem("Critical", critical, android.R.color.holo_blue_light));

                int affectedCountries = response.getInt("affectedCountries");
                mainItems.add(new MainItem("Affected Countries", affectedCountries, android.R.color.holo_purple));

                int population = response.getInt("tests");
                mainItems.add(new MainItem("Tests", population, android.R.color.holo_blue_dark));

                mainAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                cl_mainLoadingOverlay.setVisibility(View.GONE);
                e.printStackTrace();
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
}
