package com.shabit.tourthepast.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.shabit.tourthepast.MainConfig;
import com.shabit.tourthepast.R;
import com.shabit.tourthepast.activity.ListActivity;
import com.shabit.tourthepast.adapter.AdapterCategory;
import com.shabit.tourthepast.item.ItemCategory;
import com.shabit.tourthepast.utility.JsonConfig;
import com.shabit.tourthepast.utility.JsonUtils;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    GridView gridview;
    List<ItemCategory> list;
    AdapterCategory adapter;
    ArrayList<String> array_cat_id, array_cat_name, array_cat_image;
    String[] str_cat_id, str_cat_name, str_cat_image;
    int textlength = 0;
    private ItemCategory object;
    private CoordinatorLayout coordinatorLayout;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_ttp_main, container, false);
        setHasOptionsMenu(true);

        coordinatorLayout = (CoordinatorLayout) mRootView.findViewById(R.id
                .coordinatorLayout);

        gridview = (GridView) mRootView.findViewById(R.id.gridView1);

        list = new ArrayList<ItemCategory>();

        array_cat_id = new ArrayList<String>();
        array_cat_name = new ArrayList<String>();
        array_cat_image = new ArrayList<String>();

        str_cat_id = new String[array_cat_id.size()];
        str_cat_name = new String[array_cat_image.size()];
        str_cat_image = new String[array_cat_name.size()];

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                object = list.get(position);
                int Catid = object.getCategoryId();
                JsonConfig.CATEGORY_IDD = object.getCategoryId();
                Log.e("cat_id", "" + Catid);
                JsonConfig.CATEGORY_TITLE = object.getCategoryName();

                Intent intcat = new Intent(getActivity(), ListActivity.class);
                startActivity(intcat);
            }
        });

        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTask().execute(MainConfig.SERVER_URL + "/api.php");
        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.nonet1, Snackbar.LENGTH_LONG);
            snackbar.show();

            ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
            containerOffline.setVisibility(View.VISIBLE);

        }

        return mRootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void showFloatingActionButton(boolean visible) {
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (visible) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    public void render() {
        final FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        adapter = new AdapterCategory(getActivity(), R.layout.fragment_ttp_main_item, list);
        gridview.setAdapter(adapter);
        showFloatingActionButton(false);

        // floating action button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"me.sahid@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Tour The Past-Support");
                i.putExtra(Intent.EXTRA_TEXT, "Write Your Message Here....");
                try {
                    startActivity(Intent.createChooser(i, "Send email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        MaterialDialog progressDialog = new MaterialDialog.Builder(getActivity())
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
                        ItemCategory objItem = new ItemCategory();
                        objItem.setCategoryName(objJson.getString(JsonConfig.CATEGORY_NAME));
                        objItem.setCategoryId(objJson.getInt(JsonConfig.CATEGORY_CID));
                        objItem.setCategoryImageurl(objJson.getString(JsonConfig.CATEGORY_IMAGE));
                        list.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (int j = 0; j < list.size(); j++) {
                    object = list.get(j);

                    array_cat_id.add(String.valueOf(object.getCategoryId()));
                    str_cat_id = array_cat_id.toArray(str_cat_id);

                    array_cat_image.add(object.getCategoryName());
                    str_cat_name = array_cat_image.toArray(str_cat_name);

                    array_cat_name.add(object.getCategoryImageurl());
                    str_cat_image = array_cat_name.toArray(str_cat_image);
                }

                render();
            }

        }
    }
}
