#pragma version(1)
#pragma rs java_package_name(com.example.xch.displaydetection)



uchar2 RS_KERNEL mergeUvAndFixSigns(char u, char v, uint x, uint y)
{
    uchar un;
    uchar vn;
    // fix signs. essential. casting won't work
    if(u < 0)
    {
        un = 256 + u;
    }
    if(v < 0)
    {
        vn = 256 + v;
    }
    return (uchar2){ un, vn };
}