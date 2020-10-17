package com.hamzashami.coronaproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hamzashami.coronaproject.CountryDetailsActivity;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.model.Country;
import com.squareup.picasso.Picasso;
import java.util.List;
import static  android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MainViewHolder> {

  private static final String TAG = "CountryAdapter";
  private Context context;
  private List<Country> countryList;
  public static final String COUNTRY_FLAG_API_1 = "https://www.countryflags.io/";
  public static final String COUNTRY_FLAG_API_2 = "/flat/24.png";

  public CountryAdapter(Context context, List<Country> countryList) {
    this.context = context;
    this.countryList = countryList;
  }

  @NonNull
  @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new MainViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country, parent, false));
  }

  @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
    Country country = countryList.get(position);
    String countryFlagUrl = COUNTRY_FLAG_API_1 + country.getCountryCode() + COUNTRY_FLAG_API_2;

    if (TextUtils.isEmpty(countryFlagUrl)
            || countryFlagUrl.equalsIgnoreCase(context.getString(R.string.no_image))) {
      holder.iv_countryFlag.setImageResource(R.mipmap.ic_launcher);
    } else {
      Log.d(TAG, "onBindViewHolder: Flag url " + countryFlagUrl);
      Picasso.get().load(countryFlagUrl).into(holder.iv_countryFlag);
    }
    holder.tv_countryName.setText(country.getCountryName());
    holder.tv_cases.setText(country.getConfirmed() + "");

    holder.itemView.setOnClickListener(v -> {
      Intent intent = new Intent(context, CountryDetailsActivity.class);
      intent.putExtra("country_id", country.getId());
      intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    });
  }

  @Override
    public int getItemCount() {
    return countryList.size();
  }


  class MainViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_countryFlag;
    TextView tv_countryName;
    TextView tv_cases;

    MainViewHolder(@NonNull View itemView) {
      super(itemView);
      iv_countryFlag = itemView.findViewById(R.id.iv_countryFlag);
      tv_countryName = itemView.findViewById(R.id.tv_countryName);
      tv_cases = itemView.findViewById(R.id.tv_cases);
    }
  }
}
