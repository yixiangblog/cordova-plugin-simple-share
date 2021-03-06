/**
 *  Share.java
 *  Cordova Share Plugin for Android
 *
 *  Created by Ronald Klip ((github.com/RonaldPK)) on 02-09-2013.
 *  Copyright 2013 Ronald Klip. All rights reserved.
 *  LGPL v3 licensed
 */

package com.yixiangblog.cordova.share;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;
import android.net.Uri;

import com.yixiangblog.cordova.share.FileProvider;
import java.io.File;

public class Share extends CordovaPlugin {

    private void doSendIntent(String subject, String text, String imagePath, String mimeType) {
        Uri parsedUri = Uri.parse(imagePath);
        // use content:// url only on 6.0 and above
        // sending content:// url seems to not work on 5.x (I wonder why too)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            parsedUri = FileProvider.getUriForFile(this.cordova.getActivity().getApplicationContext(), cordova.getActivity().getPackageName() + ".simpleshare.provider", new File(parsedUri.getPath()));
        }
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setType(mimeType);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        sendIntent.putExtra(android.content.Intent.EXTRA_STREAM, parsedUri);
        this.cordova.startActivityForResult(this, sendIntent, 0);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        try {
            JSONObject jo = args.getJSONObject(0);
            doSendIntent(jo.getString("subject"), jo.getString("text"), jo.getString("imagePath"), jo.getString("mimeType"));
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            return true;
        } catch (JSONException e) {
            Log.e("PhoneGapLog", "Share Plugin: Error: " + PluginResult.Status.JSON_EXCEPTION);
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
            return false;
        }
    }
}
