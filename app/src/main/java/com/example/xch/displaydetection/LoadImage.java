package com.example.xch.displaydetection;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class LoadImage {
    public static void LoadFromSelectedDirectory(String filePath, byte[] bY, byte[] bU, byte[] bV) {
        File fdebug = new File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), filePath);
        RandomAccessFile rFile = null;
        FileChannel inChannel = null;
        ByteBuffer buf_in;
        byte[] bytes = null;
        int numEntry = 0;
        try {
            rFile = new RandomAccessFile(fdebug, "rw");
            inChannel = rFile.getChannel();
            numEntry = (int)inChannel.size();
            buf_in = ByteBuffer.allocate(numEntry);
            bytes = new byte[numEntry];
            inChannel.read(buf_in);
            buf_in.rewind();
            buf_in.get(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (rFile != null) {
                try {
                    rFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        int w = fourBytes2int(bytes, 0);
        int h = fourBytes2int(bytes, 4);
        int uv_pixel_stride = fourBytes2int(bytes, 8);
        int uv_row_stride = fourBytes2int(bytes, 12);
        int Y_end = 16 + w * h;
        int U_end = Y_end + uv_row_stride * h / 2 - 1;
        System.arraycopy(bytes, 16, bY, 0, Y_end-16);
        for (int i = Y_end, j = U_end, k = 0; i < U_end; i += uv_pixel_stride, j += uv_pixel_stride, k++) {
            bU[k] = bytes[i];
            bV[k] = bytes[j];
        }
    }

    private static int fourBytes2int(byte[] bytes, int offset){
        if (offset >= 0 && offset <= bytes.length - 4) {
            return ((bytes[offset] & 0xFF) << 24) | ((bytes[1+offset] & 0xFF) << 16)
                    | ((bytes[2+offset] & 0xFF) << 8) | (bytes[3+offset] & 0xFF);
        } else {
            Log.e("fourBytes2int", "invalid offset " + String.valueOf(offset));
            return 0;
        }
    }
}
