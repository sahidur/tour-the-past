package com.shabit.tourthepast.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.shabit.tourthepast.R;
import com.shabit.tourthepast.utility.Preferences;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Preferences.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .replace(R.id.settings_content, new SettingsFragment())
                    .commit();
        }

    }

    public static class SettingsFragment extends PreferenceFragment {

        LinearLayout aboutDialogContent;
        TextView aboutDialogAppName, aboutDialogAppVersion, aboutDialogAppCopyright;
        private Preference aboutPreference, itemTerms, itemAcknow;
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;

        public static final String APP_NAME = "Tour The Past";
        public static final String APP_VERSION = "1.0";
        public static final String APP_YEAR = "2016";
        public static final String APP_COPYRIGHT = "ShaBit";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);

            aboutPreference = findPreference("settings_version");
            itemTerms = findPreference("settings_terms");
            itemAcknow = findPreference("settings_acknow");

            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Preferences.sync(getPreferenceManager(), key);
                    if (key.equals(getString(R.string.pref_theme))) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }
                }
            };

            itemAcknow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
                    builder
                            .setTitle(R.string.settings_acknow)
                            .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(R.string.negative, null)
                            .show();
                    return true;
                }
            });

            itemTerms.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
                    builder
                            .setTitle(R.string.settings_terms)
                            .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(R.string.negative, null)
                            .show();
                    return true;
                }
            });

            aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                public boolean onPreferenceClick(Preference arg0) {

                    AlertDialogWrapper.Builder alertDialog = new AlertDialogWrapper.Builder(getActivity());
                    alertDialog.setTitle(getText(R.string.settings_header_about));

                    aboutDialogContent = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.about_dialog, null);

                    alertDialog.setView(aboutDialogContent);

                    aboutDialogAppName = (TextView) aboutDialogContent.findViewById(R.id.aboutDialogAppName);
                    aboutDialogAppVersion = (TextView) aboutDialogContent.findViewById(R.id.aboutDialogAppVersion);
                    aboutDialogAppCopyright = (TextView) aboutDialogContent.findViewById(R.id.aboutDialogAppCopyright);

                    aboutDialogAppName.setText(APP_NAME);
                    aboutDialogAppVersion.setText("Version " + APP_VERSION);
                    aboutDialogAppCopyright.setText("Copyright © " + APP_YEAR + " " + APP_COPYRIGHT);

                    alertDialog.setCancelable(true);
                    alertDialog.setNeutralButton(getText(R.string.positive), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

                    alertDialog.show();

                    return false;
                }
            });
        }
    }
}