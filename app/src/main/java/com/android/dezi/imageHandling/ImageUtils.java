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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.android.dezi.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mobilyte on 29/3/16.
 */
public class ImageUtils {
    private float maxHeight = 960.0f;
    private float maxWidth = 960.0f;

    public static final int IMAGE_CHOOSER_REQUEST_CODE = 570;

    private String sampledImagePath = "";

    static ImageUtils imageUtils;

    public static ImageUtils getInstance() {
        if (imageUtils == null)
            imageUtils = new ImageUtils();
        return imageUtils;
    }

    private ImageUtils() {

    }

    public String compressImage(Context context, String imagePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            checkFolder(context);
            sampledImagePath = new File(Environment.getExternalStorageDirectory()
                    + "/" + context.getResources().getString(R.string.app_name) + "/scaledImage.png").getAbsolutePath();
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            FileOutputStream fOut = new FileOutputStream(new File(sampledImagePath));
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampledImagePath;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void openImageChooser(Context context, Activity activity) {
        List<Intent> cameraIntents = new ArrayList<Intent>();
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            String packageName = res.activityInfo.packageName;
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(context));
            cameraIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        activity.startActivityForResult(chooserIntent, IMAGE_CHOOSER_REQUEST_CODE);
    }

    // function to set the path of picture captured from camera.
    private Uri setImageUri(Context context) {
        checkFolder(context);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/" + context.getResources().getString(R.string.app_name) + "/originalImage.png");
        Uri imgUri = Uri.fromFile(file);
        return imgUri;
    }

    // function to get the path of picture captured from camera.
    public String getImagePath(Context context) {
        File file = new File(Environment.getExternalStorageDirectory()
                + "/" + context.getResources().getString(R.string.app_name) + "/originalImage.png");
        return file.getAbsolutePath();
    }

    // start cropping
    public void beginCrop(Context context, Activity activity, String path, boolean square) {
        try {
            checkFolder(context);
            File f = new File(path);
            File croppedFile = new File(Environment.getExternalStorageDirectory()
                    + "/" + context.getResources().getString(R.string.app_name) + "/croppedImage.png");
            Uri destination = Uri.fromFile(croppedFile);
            if (square)
                Crop.of(Uri.fromFile(f), destination).asSquare().start(activity);
            else
                Crop.of(Uri.fromFile(f), destination).start(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beginCrop(Context context, Fragment fragment, String path, boolean square) {
        try {
            checkFolder(context);
            File f = new File(path);
            File croppedFile = new File(Environment.getExternalStorageDirectory()
                    + "/" + context.getResources().getString(R.string.app_name) + "/croppedImage.png");
            Uri destination = Uri.fromFile(croppedFile);
            if (square)
                Crop.of(Uri.fromFile(f), destination).asSquare().start(context,fragment);
            else
                Crop.of(Uri.fromFile(f), destination).start(context,fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCroppedImagePath(Context context) {
        File croppedFile = new File(Environment.getExternalStorageDirectory()
                + "/" + context.getResources().getString(R.string.app_name) + "/croppedImage.png");
        return croppedFile.getAbsolutePath();
    }


    public void resampleImage(Context context, String path) {
        new CompressImage(context, path, "Saving Image...").execute();
    }


    private void checkFolder(Context context) {
        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name));
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}