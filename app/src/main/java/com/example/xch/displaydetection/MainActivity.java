package com.example.xch.displaydetection;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static RenderScript mRS = null;
    private RenderScript mRsContext;
    private ScriptC_DisplayDetector mScript;

    private final int UV_WIDTH = 1920;
    private final int UV_HEIGHT = 1080;


    private Allocation mUAlloc;
    private Allocation mVAlloc;
    private Allocation mWarpInAllocation;
    private Type mWarpOutType;


    // start point
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mRS == null) mRS = RenderScript.create(this);

        initializeRs();
        DetectDisplay();
    }


    private void initializeRs()
    {
        mScript = new ScriptC_DisplayDetector(mRS);

        // input
        Type tInp = Type.createXY(mRS, Element.I8(mRS), UV_WIDTH, UV_HEIGHT);
        mUAlloc = Allocation.createTyped(mRS, tInp);
        mVAlloc = Allocation.createTyped(mRS, tInp);

        // output
        Type tWarpIn = Type.createXY(mRS, Element.U8_2(mRS), UV_WIDTH, UV_HEIGHT);
        mWarpInAllocation = Allocation.createTyped(mRS, tWarpIn);
    }

    private void DetectDisplay() {

        // read image
        byte[] bY = new byte[UV_WIDTH*UV_HEIGHT*4];
        byte[] bU = new byte[UV_WIDTH*UV_HEIGHT];
        byte[] bV = new byte[UV_WIDTH*UV_HEIGHT];
        String filePath = "YUV_1.yuv";
        LoadImage.LoadFromSelectedDirectory(filePath, bY, bU, bV);

        // copy to gpu allocation
        mUAlloc.copyFrom(bU);
        mVAlloc.copyFrom(bV);

        // execute gpu script
        mScript.forEach_mergeUvAndFixSigns(mUAlloc, mVAlloc, mWarpInAllocation);
    }
}

