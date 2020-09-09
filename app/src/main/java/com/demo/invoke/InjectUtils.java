package com.demo.invoke;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 */
public class InjectUtils {
    //  使用
    // InjectUtils.inJect(this)  加注解
    public static void inJect(Activity activity) {
        injectContentView(activity);
        findById(activity);
        onClick(activity);
    }
    //遍历类里面所有声明的方法，找到加了注解的方法
//    private static void onclick(final Activity activity) {
//        Class<? extends Activity> clazz = activity.getClass();
//        Method[] declaredMethods = clazz.getDeclaredMethods();
//        for (int i = 0; i < declaredMethods.length; i++) {
//
//            final Method method = declaredMethods[i];
//            if (method == null) {
//                return;
//            }
//            if (method != null) {
//                OnClickView annotation = method.getAnnotation(OnClickView.class);
//                if(annotation!=null) {
//                    int[] values = annotation.value();
//                    for (int j = 0; j < values.length; j++) {
//                        final View view = activity.findViewById(values[j]);
//                        if (view == null) {
//                            continue;
//                        }
//                        view.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    method.invoke(activity);// 调用无参的方法
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    try {
//                                        method.invoke(activity, view);// 调用有参的方法 view 代表当前点击的View
//                                    } catch (Exception e1) {
//                                        e1.printStackTrace();
//                                    }
//                                }
//                            }
//                        });
//                    }
//                }
//
//            }
//        }
//
//    }

    /**
     * 处理OnClick注解：直接找到该类名称为onClick的方法，作为专属处理点击事件的方法
     */
    private static void onClick(final Activity activity) {
        // findViewById  setOnClick
        // 1.获取该Activity的所有方法
        Class<?> clazz = activity.getClass();

//        Method[] methods = clazz.getDeclaredMethods();

        // 2.遍历方法获取所有的值
//        for (final Method method:methods){
//            // 2.1 获取OnClick注解
//            OnClickView onClick = method.getAnnotation(OnClickView.class);
        try {
            final Method method = clazz.getMethod("onClick", View.class);
            OnClickView onClick = method.getAnnotation(OnClickView.class);
            // 2.2 该方法上是否有OnClick注解
            if (onClick != null) {
                // 2.3 获取OnClick里面所有的值
                int[] viewIds = onClick.value();// @OnClick({R.id.text_view,R.id.button})

                // 2.4 先findViewById , setOnclick
                for (int viewId : viewIds) {
                    // 先findViewById
                    final View view = activity.findViewById(viewId);
                    if (view == null) {
                        continue;
                    }
                    // 后设置setOnclick
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 首先需要判断 方法是否需要检测网络
                            // 3.反射调用原来配置了OnClick的方法
                            method.setAccessible(true);// 私有的方法

                            try {
                                method.invoke(activity);// 调用无参的方法
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    method.invoke(activity, view);// 调用有参的方法 view 代表当前点击的View
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
//            }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private static void findById(Activity activity) {
        //获取Activity的class
        Class<? extends Activity> clazz = activity.getClass();
        //获取该类中的所有声明的属性
        Field[] declaredFields = clazz.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            //获取属性上的注解对象
//            @ViewById(R.id.textView) R.id.textView-value
//            TextView textView;//属性
            ViewById annotation = field.getAnnotation(ViewById.class);
            if (annotation != null) {
                int viewId = annotation.value();
                View view = activity.findViewById(viewId);
                try {
                    //私有属性也可以动态注入（不写该句代码，private声明的属性会报异常）
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注入主布局文件
     *
     * @param activity
     */
    private static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        // 查询类上是否存在ContentView注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null)// 存在
        {
            int contentViewLayoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView",
                        int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
