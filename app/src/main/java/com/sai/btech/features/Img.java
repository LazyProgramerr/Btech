package com.sai.btech.features;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Img {
    public static Bitmap compressBitmap(Bitmap originalBitmap, int percent) {
        // Calculate the compression ratio
//        int compression = 100;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        originalBitmap.compress(
                Bitmap.CompressFormat.JPEG, 60, stream); // Adjust compression quality as needed
        byte[] data = stream.toByteArray();

//        // Use try-with-resources to automatically close the stream
//        try (stream) {
//            originalBitmap.compress(Bitmap.CompressFormat.JPEG, compression, stream);
//
//            // Loop until the compressed image size is less than or equal to the desired size
//            while (stream.toByteArray().length / 1024 > maxSizeKB && compression > 0) {
//                // Reset the stream
//                stream.reset();
//                // Reduce compression ratio
//                compression -= 10;
//                // Compress the bitmap again
//                originalBitmap.compress(Bitmap.CompressFormat.JPEG, compression, stream);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Convert the compressed byte array back to a Bitmap
        return BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()));
    }
}
