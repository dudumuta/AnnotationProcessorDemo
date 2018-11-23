package demo.annotation.liuyang.com.appapi;

import android.app.Activity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提供给App调用的static 方法，提供了绑定与解绑的执行入口
 * Created by ly on 2018/11/23.
 */

public class LCJViewBinder {
    private static final ActivityViewFinder activityFinder = new ActivityViewFinder();//默认声明一个Activity View查找器
    private static final Map<String, ViewBinder> binderMap = new LinkedHashMap<>();//管理保持管理者Map集合

    /**
     * UI组件findViewById的执行入口，所有通过@BindView实例化的View最终都在这个方法中执行findViewById
     *
     * @param activity
     */
    public static void bind(Activity activity) {
        bind(activity, activity, activityFinder);
    }


    /**
     * 注解绑定
     *
     * @param host   表示注解 View 变量所在的类，也就是注解类
     * @param object 表示查找 View 的地方，Activity & View 自身就可以查找，Fragment 需要在自己的 itemView 中查找
     * @param finder ui绑定提供者接口
     */
    private static void bind(Object host, Object object, ViewFinder finder) {
        String className = host.getClass().getName();
        try {
            ViewBinder binder = binderMap.get(className);
            if (binder == null) {
                Class<?> aClass = Class.forName(className + "$$ViewBinder");
                binder = (ViewBinder) aClass.newInstance();
                binderMap.put(className, binder);
            }
            if (binder != null) {
                binder.bindView(host, object, finder);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除注解绑定 ActivityViewFinder
     *
     * @param host
     */
    public static void unBind(Object host) {
        String className = host.getClass().getName();
        ViewBinder binder = binderMap.get(className);
        if (binder != null) {
            binder.unBindView(host);
        }
        binderMap.remove(className);
    }
}
