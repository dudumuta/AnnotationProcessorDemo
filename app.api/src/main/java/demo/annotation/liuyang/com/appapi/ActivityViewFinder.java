package demo.annotation.liuyang.com.appapi;

import android.app.Activity;
import android.view.View;

/**
 * findViewById的最终执行者
 * Created by ly on 2018/11/23.
 */

public class ActivityViewFinder implements ViewFinder {
    @Override
    public View findView(Object object, int id) {
        return ((Activity) object).findViewById(id);
    }
}
