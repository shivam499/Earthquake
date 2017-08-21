package com.pathantalabs.earthquake;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthQuakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuakes>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyTextView;

    //original link
    /*private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";*/

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    ListView listView;
    private EarthQuakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_quake);

        listView = (ListView) findViewById(R.id.listView);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyTextView);

        mAdapter = new EarthQuakeAdapter(this,new ArrayList<EarthQuakes>());

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                EarthQuakes currentEarthQuakes = mAdapter.getItem(i);

                Uri earthQuakeUri = Uri.parse(currentEarthQuakes.getUrl());
                Intent webSiteIntent = new Intent(Intent.ACTION_VIEW,earthQuakeUri);
                startActivity(webSiteIntent);
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();

        if(isConnected){
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<EarthQuakes>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String minLimit = sharedPrefs.getString(
                getString(R.string.settings_min_limit_key),
                getString(R.string.settings_min_limit_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", minLimit);
        uriBuilder.appendQueryParameter("minmagnitude", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");
        return new EarthquakeLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<EarthQuakes>> loader, List<EarthQuakes> earthQuakes) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyTextView.setText(R.string.no_earthquakes);

        mAdapter.clear();

        if (earthQuakes != null && !earthQuakes.isEmpty()) {
            mAdapter.addAll(earthQuakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuakes>> loader) {
        mAdapter.clear();
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<EarthQuakes> > {

        @Override
        protected List<EarthQuakes> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<EarthQuakes> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<EarthQuakes> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }

        }
    }
}
