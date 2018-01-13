package view.Activities.interfaces;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

/**
 * Created by krzysztof on 13.01.2018.
 */

public interface MainActivityInterface {

    boolean isTwoPane();

    FragmentManager getSupportFragmentManager();

    void startProperActivity(Intent intent);

    Context getActivityContext();

    void finishActivity();
}
