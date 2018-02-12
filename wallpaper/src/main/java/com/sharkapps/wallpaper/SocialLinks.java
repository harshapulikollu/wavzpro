package com.sharkapps.wallpaper;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SocialLinks {

    public static void facebook(Context context, String pageUrl, String pageId){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId))); //219652651712359
        } catch (Exception e) {
            String fb = pageUrl; //https://www.facebook.com/browsesimply/
            if (fb != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb));
                context.startActivity(browserIntent);
            }

        }
    }

    public static void twitter(Context context, String url, String twitterId){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + twitterId))); //700922154792173568
        } catch (Exception e) {
            String twitter = url; //https://twitter.com/BrowseSimply
            if (twitter != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                context.startActivity(browserIntent);
            }

        }

    }

    public static void instagram(Context context, String url, String username){
        try {
            String scheme = "http://instagram.com/_u/" + username; //browsesimply
            String nomPackageInfo = "com.instagram.android";
            context.getPackageManager().getPackageInfo(nomPackageInfo, 0);
            Intent intentAiguilleur = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            context.startActivity(intentAiguilleur);
        } catch (Exception e) {
            String insta = url; //https://www.instagram.com/browsesimply/
            if (insta != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(insta));
                context.startActivity(browserIntent);
                }
        }
    }
}
