package com.example.xch.displaydetection;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Matrix3f;
import android.renderscript.RenderScript;
import android.renderscript.Type;
import android.util.Log;

//import org.opencv.core.CvType;
//import org.opencv.core.Mat;

import java.util.Arrays;
import java.util.List;

import static com.example.xch.displaydetection.MainActivity.mRS;

//import static kt.e_technik.tu_dortmund.de.dlight.DLightGlobal.UV_HEIGHT;
//import static kt.e_technik.tu_dortmund.de.dlight.DLightGlobal.UV_WIDTH;
//import static kt.e_technik.tu_dortmund.de.dlight.DLightGlobal.mRS;

public class SpatialSyncRs
{
    private RenderScript mRsContext;
 //   private ScriptC_SpatialSync mScript;

    private double[] mCorners;
    private double[] mLastCorners;
    private int[] mYBuffer;

    private Allocation mUAlloc;
    private Allocation mVAlloc;
    private Allocation mWarpInAllocation;
    private Type       mWarpOutType;


    private boolean containsZeros(double[] a)
    {
        for(double d : a)
        {
            if(d == 0.0)
            {
                return true;
            }
        }

        return false;
    }

    private Matrix3f cvMat2rsMat(Mat m)
    {
        if(m.rows() != 3 || m.cols() != 3)
        {
            throw new IllegalArgumentException("Input OpenCV matrix must be 3x3");
        }

        double[] values = new double[9];
        m.get(0,0, values);

        Matrix3f out = new Matrix3f();

        for(int row = 0; row < 3; ++row)
        {
            for(int col = 0; col < 3; ++col)
            {
                float v = (float)values[row*3 + col];
                out.set(col, row, v);
            }
        }

        return out;
    }


    public SpatialSyncRs(RenderScript rsContext)
    {
        mRsContext = rsContext;
        mScript = new ScriptC_SpatialSync(mRsContext);

        mYBuffer = new int[UV_WIDTH * UV_HEIGHT];

        // pre merge buffers
        Type tInp = Type.createXY(mRS, Element.I8(mRS), UV_WIDTH, UV_HEIGHT);
        mUAlloc = Allocation.createTyped(mRS, tInp);
        mVAlloc = Allocation.createTyped(mRS, tInp);

        // post merge buffers
        Type tWarpIn = Type.createXY(mRS, Element.U8_2(mRS), UV_WIDTH, UV_HEIGHT);
        mWarpInAllocation = Allocation.createTyped(mRS, tWarpIn);
        mWarpOutType = Type.createXY(mRS, Element.F32_2(mRS), UV_WIDTH, UV_HEIGHT);
    }

    public boolean sync(boolean hasCorners, Mat m_Y, byte[] bU, byte[] bV, List<Allocation> dewarpedUvBuffer)
    {
        if(!hasCorners)
        {
            m_Y.convertTo(m_Y, CvType.CV_64F);
            mCorners = LDetector.detectEDThread(m_Y);

            if(mCorners == null || containsZeros(mCorners))
            {
                return false;

            }else
            {
                mLastCorners = mCorners;
                Log.d("CornersED", Arrays.toString(mCorners));
            }
        }

        m_Y.convertTo(m_Y, CvType.CV_32SC1);
        m_Y.get(0, 0, mYBuffer);
        mCorners = LDetector.detectRANSAC(mYBuffer, mCorners);
        if(mCorners == null)
        {
            Log.e("spatialSync RANSAC", "No corner detected! Using previous corners");
            mCorners = mLastCorners;

        }else
        {
            mLastCorners = mCorners;
            Log.d("CornersRANSAC", Arrays.toString(mCorners));
        }

        mUAlloc.copyFrom(bU);
        mVAlloc.copyFrom(bV);
        mScript.forEach_mergeUvAndFixSigns(mUAlloc, mVAlloc, mWarpInAllocation);

        // calculate and upload homography matrix
        float[] srcPoints = new float[8];
        for(int i = 0; i < 8; ++i)
        {
            srcPoints[i] = (float)mCorners[i];
        }
        float[] dstPoints =
                {          0,           0,
                  UV_WIDTH-1,           0,
                           0, UV_HEIGHT-1,
                  UV_WIDTH-1, UV_HEIGHT-1
                };
        float[] homography = nFindHomography(srcPoints, dstPoints);
        Matrix3f hm = new Matrix3f(homography);
        hm.transpose();
        mScript.set_homographyMatrix(hm);

        // create new allocation for each new warped image, so the next invocation won't operate on the same output
        Allocation warpedImage = Allocation.createTyped(mRsContext, mWarpOutType);

        mScript.invoke_applyHomography(mWarpInAllocation, warpedImage);

        dewarpedUvBuffer.add(warpedImage);

        return true;
    }

    public void release()
    {
    }


    static
    {
        System.loadLibrary("CvReplacement");
    }

    /**
     * Finds a homography transform that warps a quad with the corners \c src to the corners \c dst.
     *
     * \c src and \c dst must be formatted like this: {x1 y1 x2 y2 ...}.
     *
     * @note The resulting matrix is in \em column-major order! Make sure you transpose the matrix when
     * using it in RenderScript, as RenderScript matrices use row-major order.
     *
     * @param src Source points (detected corners)
     * @param dst Destination points (corners of output image)
     * @return An array of 9 floats representing the resulting matrix, stored in \em column-major order.
     */
    private native float[] nFindHomography(float[] src, float[] dst);
}
