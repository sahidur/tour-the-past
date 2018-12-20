package com.shabit.tourthepast.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.shabit.tourthepast.MainApplication;
import com.shabit.tourthepast.MainConfig;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.etsy.android.grid.StaggeredGridView;
import com.shabit.tourthepast.R;
import com.shabit.tourthepast.adapter.AdapterTtpByCategory;
import com.shabit.tourthepast.item.ItemTtp;
import com.shabit.tourthepast.utility.JsonConfig;
import com.shabit.tourthepast.utility.JsonUtils;
import com.shabit.tourthepast.utility.Preferences;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    StaggeredGridView gridView;
    List<ItemTtp> list;
    AdapterTtpByCategory adapter;
    ArrayList<String> array_ttp, array_ttp_cat_name, array_cid, array_cat_id, array_cat_image, array_cat_name, array_title, array_image, array_desc, array_cont, array_ingre, array_calories, array_proteins, array_fat, array_carbs, array_time;
    String[] str_ttp, str_ttp_cat_name;
    String[] str_cid, str_cat_id, str_cat_image, str_cat_name, str_title, str_image, str_desc, str_cont, str_ingre, str_calories, str_proteins, str_fat, str_carbs, str_time;
    JsonUtils util;
    int textlength = 0;
    private ItemTtp object;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Preferences.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttp_list);
        setupActionBar();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        gridView = (StaggeredGridView) findViewById(R.id.gridView2);

        list = new ArrayList<ItemTtp>();
        array_ttp = new ArrayList<String>();
        array_ttp_cat_name = new ArrayList<String>();
        array_cid = new ArrayList<String>();
        array_cat_id = new ArrayList<String>();
        array_cat_image = new ArrayList<String>();
        array_cat_name = new ArrayList<String>();
        array_title = new ArrayList<String>();
        array_image = new ArrayList<String>();
        array_desc = new ArrayList<String>();
        array_cont = new ArrayList<String>();
        array_ingre = new ArrayList<String>();
        array_calories = new ArrayList<String>();
        array_proteins = new ArrayList<String>();
        array_fat = new ArrayList<String>();
        array_carbs = new ArrayList<String>();
        array_time = new ArrayList<String>();

        str_ttp = new String[array_ttp.size()];
        str_ttp_cat_name = new String[array_ttp_cat_name.size()];
        str_cid = new String[array_cid.size()];
        str_cat_id = new String[array_cat_id.size()];
        str_cat_image = new String[array_cat_image.size()];
        str_cat_name = new String[array_cat_name.size()];
        str_title = new String[array_title.size()];
        str_image = new String[array_image.size()];
        str_desc = new String[array_desc.size()];
        str_cont = new String[array_cont.size()];
        str_ingre = new String[array_ingre.size()];
        str_calories = new String[array_calories.size()];
        str_proteins = new String[array_proteins.size()];
        str_fat = new String[array_fat.size()];
        str_carbs = new String[array_carbs.size()];
        str_time = new String[array_time.size()];

        util = new JsonUtils(getApplicationContext());

        renderViewBanner();

        if (JsonUtils.isNetworkAvailable(ListActivity.this)) {
            new MyTask().execute(MainConfig.SERVER_URL + "/api.php?cat_id=" + JsonConfig.CATEGORY_IDD);
        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.nonet1, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                object = list.get(position);
                int pos = Integer.parseInt(object.getCatId());

                Intent intplay = new Intent(getApplicationContext(), DetailActivity.class);
                intplay.putExtra("POSITION", pos);
                intplay.putExtra("CATEGORY_ITEM_CID", str_cid);
                intplay.putExtra("CATEGORY_ITEM_NAME", str_cat_name);
                intplay.putExtra("CATEGORY_ITEM_IMAGE", str_cat_image);
                intplay.putExtra("CATEGORY_ITEM_CAT_ID", str_cat_id);
                intplay.putExtra("CATEGORY_ITEM_WORCIPEIMAGE", str_image);
                intplay.putExtra("CATEGORY_ITEM_WORCIPEHEADING", str_title);
                intplay.putExtra("CATEGORY_ITEM_WORCIPEDESCRI", str_desc);
                intplay.putExtra("CATEGORY_ITEM_WORCIPECONTENT", str_cont);
                intplay.putExtra("CATEGORY_ITEM_WORCIPEINGRE", str_ingre);
                intplay.putExtra("CATEGORY_ITEM_WORCIPECALORIES", str_calories);
                intplay.putExtra("CATEGORY_ITEM_WORCIPEPROTEINS", str_proteins);
                intplay.putExtra("CATEGORY_ITEM_WORCIPEFAT", str_fat);
                intplay.putExtra("CATEGORY_ITEM_WORCIPECARBS", str_carbs);
                intplay.putExtra("CATEGORY_ITEM_WORCIPETIME", str_time);

                startActivity(intplay);
            }
        });

        // init analytics tracker
        ((MainApplication) getApplication()).getTracker();
    }

    public void setAdapterToListview() {
        adapter = new AdapterTtpByCategory(ListActivity.this, R.layout.activity_ttp_list_item, list);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                textlength = newText.length();
                list.clear();

                for (int i = 0; i < str_title.length; i++) {
                    if (textlength <= str_title[i].length()) {
                        if (str_title[i].toLowerCase().contains(newText.toLowerCase())) {

                            ItemTtp objItem = new ItemTtp();

                            objItem.setCategoryName(str_cat_name[i]);
                            objItem.setCatId(str_cat_id[i]);
                            objItem.setCId(str_cid[i]);
                            objItem.setTtpDescription(str_desc[i]);
                            objItem.setTtpHeading(str_title[i]);
                            objItem.setTtpImage(str_image[i]);
                            objItem.setTtpTime(str_time[i]);

                            list.add(objItem);

                        }
                    }
                }

                setAdapterToListview();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void renderViewBanner() {
        final AdView adView = (AdView) findViewById(R.id.adView);
        final ViewGroup bannerViewGroup = (ViewGroup) findViewById(R.id.container_admob);

        if (MainConfig.ADMOB_RECIPE_BANNER && JsonUtils.isNetworkAvailable(ListActivity.this)) {

            AdRequest adRequest = new AdRequest.Builder().build();
            bannerViewGroup.setVisibility(View.VISIBLE);
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
            bannerViewGroup.setVisibility(View.GONE);
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(R.string.recipes);
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        MaterialDialog progressDialog = new MaterialDialog.Builder(ListActivity.this)
                .title(R.string.progress_title)
                .content(R.string.progress_content)
                .progress(true, 0)
                .cancelable(false)
                .build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null != progressDialog && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (null == result || result.length() == 0) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.nonet1, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemTtp objItem = new ItemTtp();

                        objItem.setCId(objJson.getString(JsonConfig.CATEGORY_ITEM_CID));
                        objItem.setCategoryName(objJson.getString(JsonConfig.CATEGORY_ITEM_NAME));
                        objItem.setCategoryImage(objJson.getString(JsonConfig.CATEGORY_ITEM_IMAGE));
                        objItem.setCatId(objJson.getString(JsonConfig.CATEGORY_ITEM_CAT_ID));
                        objItem.setTtpImage(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPEIMAGE));
                        objItem.setTtpHeading(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPEHEADING));
                        objItem.setTtpDescription(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPEDESCRI));
                        objItem.setTtpContent(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPECONTENT));
                        objItem.setTtpIngredients(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPEINGRE));
                        objItem.setTtpCalories(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPECALORIES));
                        objItem.setTtpProteins(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPEPROTEINS));
                        objItem.setTtpFat(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPEFAT));
                        objItem.setTtpCarbs(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPECARBS));
                        objItem.setTtpTime(objJson.getString(JsonConfig.CATEGORY_ITEM_WORCIPETIME));

                        list.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < list.size(); j++) {

                    object = list.get(j);

                    array_cat_id.add(object.getCatId());
                    str_cat_id = array_cat_id.toArray(str_cat_id);

                    array_cat_name.add(object.getCategoryName());
                    str_cat_name = array_cat_name.toArray(str_cat_name);

                    array_cid.add(String.valueOf(object.getCId()));
                    str_cid = array_cid.toArray(str_cid);

                    array_image.add(String.valueOf(object.getTtpImage()));
                    str_image = array_image.toArray(str_image);

                    array_title.add(String.valueOf(object.getTtpHeading()));
                    str_title = array_title.toArray(str_title);

                    array_desc.add(String.valueOf(object.getTtpDescription()));
                    str_desc = array_desc.toArray(str_desc);

                    array_cont.add(String.valueOf(object.getTtpContent()));
                    str_cont = array_cont.toArray(str_cont);

                    array_ingre.add(String.valueOf(object.getTtpIngredients()));
                    str_ingre = array_ingre.toArray(str_ingre);

                    array_calories.add(String.valueOf(object.getTtpCalories()));
                    str_calories = array_calories.toArray(str_calories);

                    array_proteins.add(String.valueOf(object.getTtpProteins()));
                    str_proteins = array_proteins.toArray(str_proteins);

                    array_fat.add(String.valueOf(object.getTtpFat()));
                    str_fat = array_fat.toArray(str_fat);

                    array_carbs.add(String.valueOf(object.getTtpCarbs()));
                    str_carbs = array_carbs.toArray(str_carbs);

                    array_time.add(String.valueOf(object.getTtpTime()));
                    str_time = array_time.toArray(str_time);
                }

                setAdapterToListview();
            }

        }
    }
}