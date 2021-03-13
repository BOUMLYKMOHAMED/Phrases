package com.rutershok.phrases;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import androidx.annotation.Nullable;

import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.utils.Dialog;
import com.rutershok.phrases.utils.Key;
import com.rutershok.phrases.utils.Setting;
import com.rutershok.phrases.utils.Share;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Context mContext;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        setSettings();
    }

    private void setSettings() {
        for (String key : Key.PREFERENCES) {
            findPreference(key).setOnPreferenceClickListener(this);
        }

        findPreference(Key.PREF_CHANGE_LANGUAGE).setOnPreferenceChangeListener((preference, newValue) -> {
            Storage.setLanguage(preference.getContext(), (String) newValue);
            Setting.setLanguage(preference.getContext(), Storage.getLanguage(preference.getContext()));
            Setting.restartApp(mContext);
            return true;
        });
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String preferenceKey = preference.getKey();
        switch (preferenceKey) {
            case Key.PREF_NOTIFICATIONS:
                Setting.setNotificationsEnabled(mContext, preference);
                break;
            case Key.PREF_ANIMATIONS:
                Setting.setAnimations(preference.getContext(), preference);
                break;
            case Key.PREF_RATE_APP:
                Share.openAppPage(mContext, mContext.getPackageName());
                break;
            case Key.PREF_CONTACT_US:
                Share.contactUs(mContext);
                break;
            case Key.PREF_SHARE:
                Share.shareApp(mContext);
                break;
            case Key.PREF_GOOGLE_PLAY:
                Share.openPublisherPage(mContext);
                break;
            case Key.PREF_INSTAGRAM:
                Share.openInstagram(mContext);
                break;
            case Key.PREF_FACEBOOK:
                Share.openFacebook(mContext);
                break;
            case Key.PREF_TWITTER:
                Share.openTwitter(mContext);
                break;
            case Key.PREF_PRIVACY_POLICY:
                Share.privacyPolicy(mContext);
                break;
            case Key.PREF_GDPR:
                Dialog.gdprConsent(getActivity());
                break;
        }
        return true;
    }
}
