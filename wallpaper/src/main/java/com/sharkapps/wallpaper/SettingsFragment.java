package com.sharkapps.wallpaper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Settings fragment
 */
public class SettingsFragment extends Fragment {
    public static final String APP_LINK = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

    static final String KEY_COLOR_1 = "COLOR_1";
    static final String KEY_COLOR_2 = "COLOR_2";
    static final String KEY_COLOR_3 = "COLOR_3";
    static final String KEY_COLOR_4 = "COLOR_4";
    static final String KEY_COLOR_5 = "COLOR_5";
    @Bind({R.id.view_1, R.id.view_2, R.id.view_3, R.id.view_4, R.id.view_5})
    View[] views;
    private SharedPreferences preferences;

    public static SettingsFragment instance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AdmobAds.fullAd(getActivity(), getActivity().getString(R.string.admob_full_ad));
        AdmobAds.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AdmobAds.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String[] colorsStrings = getResources().getStringArray(R.array.color_preset1);
        final int[] colors = new int[colorsStrings.length];
        for (int i = 0; i < colorsStrings.length; i++) {
            colors[i] = Color.parseColor(colorsStrings[i]);
        }
        colors[0] = preferences.getInt(KEY_COLOR_1, colors[0]);
        colors[1] = preferences.getInt(KEY_COLOR_2, colors[1]);
        colors[2] = preferences.getInt(KEY_COLOR_3, colors[2]);
        colors[3] = preferences.getInt(KEY_COLOR_4, colors[3]);
        colors[4] = preferences.getInt(KEY_COLOR_5, colors[4]);
        for (int i = 0; i < 5; i++) {
            final int index = i;
            views[i].setBackgroundColor(colors[i]);
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //If Pro Version
                    if (BuildConfig.FULL_VERSION) {
                        ColorPickerDialogFragment fragment = ColorPickerDialogFragment.instance(index, colors[index]);
                        fragment.setTargetFragment(SettingsFragment.this, 1);
                        fragment.show(getFragmentManager(), "ColorPicker");
                    } else {
                        //download pro

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Download Pro")
                                .setMessage("Download Wavie Pro to use this feature.")
                                .setPositiveButton("Download now", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // download
                                        downloadPro(getActivity());
                                    }
                                })
                                .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AdmobAds.show();
                                    }
                                })
                                .show();
                    }
                }
            });
        }
    }

    public boolean MyStartActivity(Intent aIntent, Context c) {
        try {
            c.startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public  void downloadPro(Context c) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=" + "com.sharkapps.wavzpro"));
        if (!MyStartActivity(intent, c)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(Uri.parse("com.sharkapps.wavzpro"));
            if (!MyStartActivity(intent, c)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(c, "Please install the Google playstore", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            int index = data.getIntExtra(ColorPickerDialogFragment.KEY_INDEX, -1);
            int color = data.getIntExtra(ColorPickerDialogFragment.KEY_COLOR, 0);
            views[index].setBackgroundColor(color);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        if (!BuildConfig.FULL_VERSION) {
            inflater.inflate(R.menu.main, menu);
            MenuItem preset = menu.findItem(R.id.action_preset);
            Menu m = preset.getSubMenu();
            String[] presets = getResources().getStringArray(R.array.presets);
            for (int i = 0; i < presets.length; i++) {
                final int index = i + 1;
                m.add(Menu.NONE, i, Menu.NONE, presets[i]).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        setPreset(index);
                        return true;
                    }
                });
            }
        } else {
            inflater.inflate(R.menu.main2, menu);
            MenuItem preset = menu.findItem(R.id.action_preset2);
            Menu m = preset.getSubMenu();
            String[] presets = getResources().getStringArray(R.array.presets);
            for (int i = 0; i < presets.length; i++) {
                final int index = i + 1;
                m.add(Menu.NONE, i, Menu.NONE, presets[i]).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        setPreset(index);
                        return true;
                    }
                });
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if (item.getItemId() == R.id.action_save || item.getItemId() == R.id.action_save2) {
            preferences.edit()
                    .putInt(KEY_COLOR_1, ((ColorDrawable) views[0].getBackground()).getColor())
                    .putInt(KEY_COLOR_2, ((ColorDrawable) views[1].getBackground()).getColor())
                    .putInt(KEY_COLOR_3, ((ColorDrawable) views[2].getBackground()).getColor())
                    .putInt(KEY_COLOR_4, ((ColorDrawable) views[3].getBackground()).getColor())
                    .putInt(KEY_COLOR_5, ((ColorDrawable) views[4].getBackground()).getColor())
                    .apply();
            getActivity().finish();
            AdmobAds.show();
            return true;


        }else if (item.getItemId() == R.id.action_rate || item.getItemId() == R.id.action_rate2) {

            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
            if (!MyStartActivity(intent, getActivity())) {
                intent.setData(Uri.parse(BuildConfig.APPLICATION_ID));
                if (!MyStartActivity(intent, getActivity())) {
                    Toast.makeText(getActivity(),
                            "Please download Google play store.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
       else if (item.getItemId() == R.id.action_share || item.getItemId() == R.id.action_share2) {

            String shareMessage = "Hey I found amazing music live wallpaper app for android" +
                    "\n" + APP_LINK;

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TITLE, "Check out this cool music live wallpaper");
            share.putExtra(Intent.EXTRA_TEXT, shareMessage);

            try {
                getActivity().startActivity(Intent.createChooser(share, "Share with"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
        else if (item.getItemId() == R.id.action_fb || item.getItemId() == R.id.action_fb2) {

            SocialLinks.facebook(getActivity(), getActivity().getString(R.string.facebook_url), getActivity().getString(R.string.facebookid));
            return true;
        }
      /*  else if (item.getItemId() == R.id.action_twitter || item.getItemId() == R.id.action_twitter2) {

            SocialLinks.twitter(getActivity(), getActivity().getString(R.string.twitter_url), getActivity().getString(R.string.twitterid));
            return true;
        }
        else if (item.getItemId() == R.id.action_insta || item.getItemId() == R.id.action_insta2) {

            SocialLinks.instagram(getActivity(), getActivity().getString(R.string.instagram_url),getActivity().getString(R.string.instagram_username));
            return true;
        }*/
        else if (item.getItemId() == R.id.action_moreapps || item.getItemId() == R.id.action_moreapps2) {

            Intent intent2 = new Intent(Intent.ACTION_VIEW);
            intent2.setData(Uri.parse("market://search?q=pub:" + getActivity().getString(R.string.googe_play_dev_name)));
            if (!MyStartActivity(intent2,  getActivity())) {
                intent2.setData(Uri.parse("http://play.google.com/store/search?q=pub:" + getActivity().getString(R.string.googe_play_dev_name)));
                if (!MyStartActivity(intent2,  getActivity())) {
                    Toast.makeText( getActivity(),
                            "Please download Google play store.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        }
        else if (item.getItemId() == R.id.action_pro) {
            downloadPro(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);




    }

    private void setPreset(int index) {
        int id = getResources().getIdentifier("color_preset" + index, "array", getContext().getPackageName());
        String[] colors = getResources().getStringArray(id);
        for (int i = 0; i < 5; i++) {
            views[i].setBackgroundColor(Color.parseColor(colors[i]));
        }
        AdmobAds.show();
    }
}
