package jejunu.ac.kr.whatsuda;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by HSH on 2016. 10. 25..
 */

public class DatabaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
