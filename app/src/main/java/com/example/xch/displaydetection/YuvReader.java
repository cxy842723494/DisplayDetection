package org.lskt.xu.vlcreceiver.util;

import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Xu on 23.10.2017.
 */

public class YuvReader {

    private int w;
    private int h;

    private byte[] y;
    private byte[] u;
    private byte[] v;
    private byte[] nv21;

    /**
     * YUV_420_888
     */

    public byte[] getY() { return y; }
    public byte[] getU() { return u; }
    public byte[] getV() { return v; }
    public int getWidth() { return w; }
    public int getHeight() { return h; }

    public void readYUV(File f) {
//        try {
            if (f.exists()) {
//                DataInputStream in = new DataInputStream(new FileInputStream(f));
//                int L = (int) f.length();
//                byte[] bytes = new byte[L];
//                in.readFully(bytes);

                RandomAccessFile rFile = null;
                FileChannel inChannel = null;
                ByteBuffer buf_in;
                byte[] bytes = null;
                int numEntry = 0;
                try {
                    rFile = new RandomAccessFile(f, "rw");
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

                w = fourBytes2int(bytes, 0);
                h = fourBytes2int(bytes, 4);
                int uv_pixel_stride = fourBytes2int(bytes, 8);
                int uv_row_stride = fourBytes2int(bytes, 12);

                y = new byte[w * h];
                u = new byte[w * h / 4];
                v = new byte[w * h / 4];

//                int d = (w * h * (1 + uv_pixel_stride / 2)) - (bytes.length - 16);
//                switch(d){
//                    case 0:
//                        break;
//                    case 1:
//                        break;
//                    case 2: {
                        int Y_end = 16 + w * h;
                        int U_end = Y_end + uv_row_stride * h / 2 - 1;
                        System.arraycopy(bytes, 16, y, 0, Y_end-16);
                        for (int i = Y_end, j = U_end, k = 0; i < U_end; i += uv_pixel_stride, j += uv_pixel_stride, k++) {
                            u[k] = bytes[i];
                            v[k] = bytes[j];
                        }
                        /*
                        for (int i = Y_end, k = 0; i < U_end; i += uv_pixel_stride, k++) {
                            u[k] = bytes[i];
                        }
                        for (int i = U_end, k = 0; i < L; i += uv_pixel_stride, k++) {
                            v[k] = bytes[i];
                        }
                        */
//                        break;
//                    }
//                    case 3:
//                        break;
//                    default:
//                        break;
//                }

//                in.close();
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void yuv2nv21() {
        nv21 = new byte[(int)(w * h * 1.5)];

        int k = 0;
        for (int i = 0; i < y.length; i ++, k++) {
            nv21[k] = y[i];
        }
        for (int i = 0; i < u.length; i++) {
            nv21[k] = u[i];
            nv21[k + 1] = v[i];
            k = k + 2;
        }
    }

    public YuvImage readYuv2Nv21(File f) {
        readYUV(f);
        yuv2nv21();
        return new YuvImage(nv21, ImageFormat.NV21, w, h, null);
    }

    private int fourBytes2int(byte[] bytes, int offset){
        if (offset >= 0 && offset <= bytes.length - 4) {
            return ((bytes[offset] & 0xFF) << 24) | ((bytes[1+offset] & 0xFF) << 16)
                    | ((bytes[2+offset] & 0xFF) << 8) | (bytes[3+offset] & 0xFF);
        } else {
            Log.e("fourBytes2int", "invalid offset " + String.valueOf(offset));
            return 0;
        }
    }
}