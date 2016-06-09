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

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mobilyte on 15/01/16.
 */
public class FileUtils {

    // function to get the path of user profile picture selected from gallery.
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, final Uri uri) {
        System.out.println(uri.toString());

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    return "storage/" + split[0] + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else {
                Cursor imageCursor = context.getContentResolver().query(uri, null, null, null, null);
                if (imageCursor != null) {
                    imageCursor.moveToFirst();
                    System.out.println(DatabaseUtils.dumpCursorToString(imageCursor));
                    int column_index = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    if (column_index != -1) {
                        return imageCursor.getString(column_index);
                    } else {
                        String id = imageCursor.getString(imageCursor.getColumnIndex("document_id"));
                        String document_id = id.split(":")[1];
                        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null);
                        cursor.moveToFirst();
                        System.out.println(DatabaseUtils.dumpCursorToString(cursor));
                        return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                } else
                    return uri.getPath();
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (uri.toString().startsWith("content://com.google.android.apps.photos.content") || uri.toString().startsWith("content://com.google.android.apps.docs.storage")) {
                try {
                    InputStream is = context.getContentResolver().openInputStream(uri);
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/Seniorentreff/image.png");
                    FileOutputStream fout = new FileOutputStream(file);
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = is.read(bytes)) != -1) {
                        fout.write(bytes, 0, read);
                    }
                    fout.close();
                    is.close();
                    return file.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            Cursor imageCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (imageCursor != null) {
                imageCursor.moveToFirst();
                System.out.println(DatabaseUtils.dumpCursorToString(imageCursor));
                int column_index = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (column_index != -1) {
                    return imageCursor.getString(column_index);
                } else {
                    String id = imageCursor.getString(imageCursor.getColumnIndex("document_id"));
                    String document_id = id.split(":")[1];
                    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null);
                    cursor.moveToFirst();
                    System.out.println(DatabaseUtils.dumpCursorToString(cursor));
                    return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
            } else
                return uri.getPath();
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                System.out.println(DatabaseUtils.dumpCursorToString(cursor));

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
