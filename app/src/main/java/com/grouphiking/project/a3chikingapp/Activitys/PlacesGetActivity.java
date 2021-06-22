package com.grouphiking.project.a3chikingapp.Activitys;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.R;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener;

public class PlacesGetActivity extends AppCompatActivity {

    private PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_get);
        getActionBar().hide();

        PlaceOptions placeOptions = PlaceOptions.builder()
                .backgroundColor(ContextCompat.getColor(this, R.color.Brown_Light))
                .hint(getString(R.string.places_hint))
                .language(Constants.LANGUAGE.getLanguage())
                .limit(Constants.LIMIT_OPTIONS)
                .historyCount(Constants.HISTORY_COUNT)
                .build();


        if (savedInstanceState == null) {
            autocompleteFragment = PlaceAutocompleteFragment.newInstance(getString(R.string.mapbox_access_token), placeOptions);

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.places_container, autocompleteFragment,getString(R.string.places_tag));
            transaction.commit();

        } else {
            autocompleteFragment = (PlaceAutocompleteFragment)
                    getSupportFragmentManager().findFragmentByTag(getString(R.string.places_tag));
        }
        setListeners();
    }

    private void setListeners(){
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(CarmenFeature carmenFeature) {
                Constants.setFeature(carmenFeature);
                finish();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
    }
}