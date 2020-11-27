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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CountryAdapter extends
        RecyclerView.Adapter<CountryAdapter.MainViewHolder> {
    /**
     * initialize list.
     */
  private final List<Country> countryList;
    /**
     * initialize context.
     */
  private final Context context;
    /**
     * initialize TAG.
     */
    public static final String TAG = "CountryAdapter";
    /**
     * String Variable URL.
     */

    public static final String
            COUNTRY_FLAG_API_1 = "https://www.countryflags.io/";
    /**
     * String Variable Imag.
     */
    public static final String COUNTRY_FLAG_API_2 = "/flat/24.png";
  /**
   * @param context
   * @param countryList
   */
public CountryAdapter(final Context context, final List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }
    /**
     * initialize onCreateViewHolder.
     */
    @NonNull
    @Override

    public MainViewHolder
    onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        return new MainViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country, parent, false));
    }
    /**
     * initialize onBindViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder
                                             holder, final int position) {
        Country country = countryList.get(position);
      String countryFlagUrl = COUNTRY_FLAG_API_1 + country.getCountryCode()
                + COUNTRY_FLAG_API_2;

        if (TextUtils.isEmpty(countryFlagUrl)
                || countryFlagUrl.equalsIgnoreCase(context
                .getString(R.string.no_image))) {
            holder.countryFlag.setImageResource(R.mipmap.ic_launcher);
        } else {
            Log.d(TAG, "onBindViewHolder: Flag url " + countryFlagUrl);
            Picasso.get().load(countryFlagUrl).into(holder.countryFlag);
        }
        holder.countryName.setText(country.getCountryName());
        holder.cases.setText(country.getConfirmed() + "");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CountryDetailsActivity.class);
            intent.putExtra("country_id", country.getId());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
    /**
     * call getItemCount.
     */
    @Override
    public int getItemCount() {
        return countryList.size();
    }


    class MainViewHolder extends RecyclerView.ViewHolder {
      /**
       * ImageView Variable countryFlag.
       */
        private ImageView countryFlag;
      /**
       * Textview Variable countryName.
       */
        private TextView countryName;
      /**
       * Textview Variable cases.
       */
        private TextView cases;

        MainViewHolder(@NonNull final  View itemView) {
            super(itemView);
            countryFlag = itemView.findViewById(R.id.iv_countryFlag);
            countryName = itemView.findViewById(R.id.tv_countryName);
            cases = itemView.findViewById(R.id.tv_cases);
        }
    }
}
