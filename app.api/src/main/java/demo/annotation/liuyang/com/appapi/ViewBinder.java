package demo.annotation.liuyang.com.appapi;

/**
 * UI绑定解绑接口
 * Created by ly on 2018/11/23.
 */

public interface ViewBinder<T> {
    /**
     * 绑定View
     * @param host @BindView所属class
     * @param object @BindView findViewById的最终执行者 Activity View 等
     * @param finder @BindView findViewById的执行接口
     */
    void bindView(T host, Object object, ViewFinder finder);

    void unBindView(T host);
}
