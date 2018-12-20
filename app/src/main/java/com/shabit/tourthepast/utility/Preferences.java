package com.shabit.tourthepast.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.view.ContextThemeWrapper;

import com.shabit.tourthepast.R;

import java.util.Map;

public class Preferences {

    private static final BoolToStringPref[] PREF_MIGRATION = new BoolToStringPref[]{
            new BoolToStringPref(R.string.pref_green_theme, false,
                    R.string.pref_theme, R.string.pref_theme_value_green),
    };

    public static void sync(PreferenceManager preferenceManager) {
        Map<String, ?> map = preferenceManager.getSharedPreferences().getAll();
        for (String key : map.keySet()) {
            sync(preferenceManager, key);
        }
    }

    public static void sync(PreferenceManager preferenceManager, String key) {
        Preference pref = preferenceManager.findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        }
    }

    public static void migrate(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        for (BoolToStringPref pref : PREF_MIGRATION) {
            if (pref.isChanged(context, sp)) {
                editor.putString(context.getString(pref.newKey), context.getString(pref.newValue));
            }

            if (pref.hasOldValue(context, sp)) {
                editor.remove(context.getString(pref.oldKey));
            }
        }

        editor.apply();
    }

    public static void applyTheme(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.indigoThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_Ttp_Carrot);
        } else if (Preferences.carrotThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_Ttp_Blue);
        } else if (Preferences.blueThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_Ttp_Brown);
        } else if (Preferences.brownThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_Ttp_Gray);
        } else if (Preferences.grayThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_Ttp_Green);
        } else if (Preferences.greenThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_Ttp_Red);
        } else if (Preferences.redThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_Ttp_Yellow);
        }
    }

    private static boolean indigoThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_indigo))
                .equals(context.getString(R.string.pref_theme_value_carrot));
    }

    private static boolean carrotThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_carrot))
                .equals(context.getString(R.string.pref_theme_value_blue));
    }

    private static boolean blueThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_blue))
                .equals(context.getString(R.string.pref_theme_value_brown));
    }

    private static boolean brownThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_brown))
                .equals(context.getString(R.string.pref_theme_value_gray));
    }

    private static boolean grayThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_gray))
                .equals(context.getString(R.string.pref_theme_value_green));
    }

    private static boolean greenThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_green))
                .equals(context.getString(R.string.pref_theme_value_red));
    }

    private static boolean redThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_red))
                .equals(context.getString(R.string.pref_theme_value_yellow));
    }

    private static class BoolToStringPref {
        private final int oldKey;
        private final boolean oldDefault;
        private final int newKey;
        private final int newValue;

        private BoolToStringPref(@StringRes int oldKey, boolean oldDefault,
                                 @StringRes int newKey, @StringRes int newValue) {
            this.oldKey = oldKey;
            this.oldDefault = oldDefault;
            this.newKey = newKey;
            this.newValue = newValue;
        }

        private boolean isChanged(Context context, SharedPreferences sp) {
            return hasOldValue(context, sp) &&
                    sp.getBoolean(context.getString(oldKey), oldDefault) != oldDefault;
        }

        private boolean hasOldValue(Context context, SharedPreferences sp) {
            return sp.contains(context.getString(oldKey));
        }
    }
}