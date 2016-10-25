package jejunu.ac.kr.whatsuda;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HSH on 16. 9. 6..
 */
public class LandMark{
    LatLng latLng;
    String name;
    int count;
    int imgId;
    int realCount;

    public LandMark(LatLng latLng, String name, int count, int imgId) {
        this.latLng = latLng;
        this.name = name;
        this.count = count;
        this.imgId = imgId;
    }

    public void setRealCount(int realCount) {
        this.realCount = realCount;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getImgId() {
        return imgId;
    }

    public int getRealCount() {
        return realCount;
    }
}
