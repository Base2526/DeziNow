/*
 *
 *
 *  * Copyright © 2016, Mobilyte Inc. and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  * - Redistributions of source code must retain the above copyright
 *  *    notice, this list of conditions and the following disclaimer.
 *  *
 *  * - Redistributions in binary form must reproduce the above copyright
 *  * notice, this list of conditions and the following disclaimer in the
 *  * documentation and/or other materials provided with the distribution.
 *
 * /
 */

package com.android.dezi.imageHandling;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Mobilyte on 29/3/16.
 */
public class CompressImage extends AsyncTask<String, Void, String> {
    private String path;
    private Context context;
    private SampledImageCallback callback;
    private ProgressDialog pd;
    private String loaderMessage;
    private String modifiedPath = "";

    public CompressImage(Context context, String path, String message) {
        this.path = path;
        this.context = context;
        this.loaderMessage = message;
    }

    @Override
    protected void onPreExecute() {
        callback = (SampledImageCallback) context;
        pd = new ProgressDialog(context);
        pd.setMessage(Html.fromHtml("<big>" + loaderMessage + "</big>"));
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String base64 = "";
        try {
            modifiedPath = ImageUtils.getInstance().compressImage(context, path);
            base64 = base64String(modifiedPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        callback.onSampledImage(s, modifiedPath);
    }

    // function to encode the image to base 64.
    private String base64String(String selectedImagePath) {
        Bitmap bm = BitmapFactory.decodeFile(new File(selectedImagePath).getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();
        String base64 = Base64.encodeToString(b, Base64.DEFAULT);
        return base64;
    }
}
