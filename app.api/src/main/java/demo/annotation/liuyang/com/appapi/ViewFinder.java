package demo.annotation.liuyang.com.appapi;

import android.view.View;

/**
 * findViewById的执行接口
 * Created by ly on 2018/11/23.
 */

public interface ViewFinder {
    View findView(Object object, int id);
}
