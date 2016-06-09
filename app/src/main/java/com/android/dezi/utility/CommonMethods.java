package com.android.dezi.utility;
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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.dezi.GlobalActivity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Mobilyte on 8/7/2015.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CommonMethods {
    private static CommonMethods mCommonmethods;
    private static AlertDialog alert;
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private CommonMethods() {
    }

    public static CommonMethods getInstance() {
        if (mCommonmethods == null)
            mCommonmethods = new CommonMethods();
        return mCommonmethods;
    }

    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public void el(String tag, String msg) {
        Log.e(tag, msg);
    }

    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public void DisplayToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public void displaySnackBar(Context ctx, String msg, View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean validateEmail(final String EmailID) {
        EmailValidator();
        matcher = pattern.matcher(EmailID);
        return matcher.matches();
    }

    /*
    Phone Validation
     */
    public boolean isPhoneValid(String phone) {
        if (Patterns.PHONE.matcher(phone).matches() && phone.length() >= 9) {
            return true;
        }
        return false;
    }

    /*
    Password Verification
    */
    public boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /*
    Change Image Orientation portrate after camera intent
     */
    public Bitmap changeToPortrate(String photoPath, Bitmap bp) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        Bitmap bitmap = null;
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation == ExifInterface.ORIENTATION_NORMAL) {

// Do nothing. The original image is fine.
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {

            bitmap = rotateImage(bp, 90);

        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {

            bitmap = rotateImage(bp, 180);

        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {

            bitmap = rotateImage(bp, 270);

        }
        return bitmap;
    }

    public String getDeviceId(Context context) {
        try {
            // GET DEVICE ID
            String DEVICEID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return DEVICEID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /*
     * Hide Keyboard
	 */
    void hideKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        //  imm.hideSoftInputFromWindow(ctx.getApplicationContext().getApplicationWindowToken(), 0);
    }

    /*
    Read Image  Orientation
     */
    public final int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /*
    Split text from string
     */
    public String getMeNthParamInString(String p_text, String p_seperator, int nThParam) { // / "TOTRPIDS=101=104" returns
        // "101" If nThParam ==
        // 2.
        String retStrThirdParam = new String("");
        int index = -1;
        int prevIdx = 0;
        int loopNM = 1;
        boolean loopBool = true;
        while (loopBool) {
            try {
                index = p_text.indexOf(p_seperator, prevIdx);
                if (loopNM >= nThParam) {
                    if (index >= 0) {
                        retStrThirdParam = p_text.substring(prevIdx, index);
                    } else // /-1
                    {
                        retStrThirdParam = p_text.substring(prevIdx);
                    }
                    loopBool = false;
                    break;
                } else {
                    if (index < 0) // /-1
                    {
                        loopBool = false;
                        retStrThirdParam = "";
                        break;
                    }
                }
                loopNM++;
                prevIdx = index + 1;
            } catch (Exception ex) {
                loopBool = false;
                retStrThirdParam = "";
                break;
            }
        } // /while
        if (retStrThirdParam.trim().length() <= 0) {
            retStrThirdParam = "";
        }
        return retStrThirdParam;
    }

    public final String getPath(Uri uri, Context context) {
        if (uri == null) {
            CommonMethods.getInstance().e("", "uri is null");
            return null;
        }
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
   /* public void customDialogBox(Context mContext){
        // Create custom dialog object
        final Dialog dialog = new Dialog(mContext);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText("Custom dialog Android example.");

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });


    }*/

    /*
     * Alert Box
     */
    public static final void showOkDialog(String dlgText, Context context) {

        if (context == null && GlobalActivity.getGlobalContext() != null) {
            context = GlobalActivity.getGlobalContext();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage(dlgText).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (alert != null)
                    alert.dismiss();
            }

        });
        alert = builder.create();
        alert.show();
    }

    /*
    Get Color
     */
    public int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /*
    Check Location Service is Enabled or not
     */
    public Boolean checkLocationService(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
            dialog.setMessage("GPS Location is not Enabled, Do you want to Enable it ?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        } else {
            return true;
        }
        return false;
    }

}
