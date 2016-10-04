package jejunu.ac.kr.whatsuda;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HSH on 16. 9. 6..
 */
public class LandMark {
    LatLng latLng;
    String name;
    int count;

    public LandMark(LatLng latLng, String name, int count) {
        this.latLng = latLng;
        this.name = name;
        this.count = count;
    }

    public void incrementCount() {
        count++;
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
}
