package com.transsion.api.utils;
/* Top Secret */

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;

/**
 * 视图工具类
 *
 * @author 孙志刚.
 * @date 2017/1/4.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ViewUtils {
    /**
     * 判断点击是否在View内
     *
     * @param child
     * @param ev
     * @return
     */
    public static boolean isInRangeOfView(View child, MotionEvent ev) {
        if (ev.getX() < child.getX() || ev.getX() > (child.getX() + child.getWidth()) ||
                ev.getY() < child.getY() || ev.getY() > (child.getY() + child.getHeight())) {
            return false;
        }

        return true;
    }

    /**
     * 在当前view位置，向下搜索满足type类型的第一个元素
     *
     * @param view
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T findViewByClass(View view, Class<T> type) {
        if (view == null || type == null) {
            return null;
        }

        if (!(view instanceof ViewGroup)) {
            if (type.isInstance(view)) {
                return (T) view;
            } else {
                return null;
            }
        }

        if (type.isInstance(view)) {
            return (T) view;
        }

        ViewGroup layout = (ViewGroup) view;
        T child, result;
        for (int n = 0; n < layout.getChildCount(); n++) {
            child = (T) layout.getChildAt(n);

            if (type.isInstance(child)) {
                return child;
            }

            if (child instanceof ViewGroup) {
                result = findViewByClass((ViewGroup) child, type);

                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * 在当前view位置，向下搜索满足type类型的所有元素
     *
     * @param view
     * @param type
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> findViewsByClass(View view, Class<T> type) {
        if (view == null || type == null) {
            return null;
        }

        ArrayList<T> viewList = new ArrayList<>();
        if (!(view instanceof ViewGroup)) {
            if (type.isInstance(view)) {
                viewList.add((T) view);
            }

            return viewList;
        }

        ViewGroup layout = (ViewGroup) view;
        View child;
        for (int n = 0; n < layout.getChildCount(); n++) {
            child = layout.getChildAt(n);

            if (type.isInstance(child)) {
                viewList.add((T) child);
                continue;
            }

            if (child instanceof ViewGroup) {
                for (T v : findViewsByClass(child, type)) {
                    viewList.add(v);
                }
            }
        }

        return viewList;
    }

    /**
     * 在当前view位置，向上搜索满足type类别的第一个元素
     *
     * @param view
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T findParentViewByClass(View view, Class<T> type) {
        if (view == null || type == null) {
            return null;
        }

        ViewParent parent = view.getParent();

        if (parent != null) {
            while ((parent = parent.getParent()) != null) {
                if (type.isInstance(parent)) {
                    return (T) parent;
                }
            }
        }

        return null;
    }

    /**
     * 搜索最近的指定类型view(跟自己同级及以上，下级不搜索)
     *
     * @param view
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T findClosetViewByClass(View view, Class<T> type) {
        if (view == null || type == null) {
            return null;
        }

        //搜索直系父节点，采用双向搜索
        if (view.getParent() != null && view.getParent() instanceof View) {
            ViewGroup parent = (ViewGroup) view.getParent();

            int index = 0, rindex = 0;
            for (int n = 0; n < parent.getChildCount(); n++) {
                View child = parent.getChildAt(n);

                if (child.equals(view)) {
                    index = n + 1;
                    rindex = n - 1;
                    break;
                }
            }

            while (index < parent.getChildCount() || rindex >= 0) {
                //先左后右搜索
                if (rindex >= 0) {
                    //往左搜索
                    View child = parent.getChildAt(rindex);

                    if (type.isInstance(child)) {
                        return (T) child;
                    }

                    rindex--;
                }

                if (index < parent.getChildCount()) {
                    //往右搜索
                    View child = parent.getChildAt(index);

                    if (type.isInstance(child)) {
                        return (T) child;
                    }

                    index++;
                }
            }

            view = (View) view.getParent();
        }

        //直系父节点没有搜索到后继续向上搜索，采用顺序搜索
        while (view.getParent() != null && view.getParent() instanceof View) {
            view = (View) view.getParent();
            ViewGroup parent = (ViewGroup) view;

            for (int n = 0; n < parent.getChildCount(); n++) {
                View child = parent.getChildAt(n);

                if (type.isInstance(child)) {
                    return (T) child;
                }
            }
        }

        return null;
    }

    /**
     * 获取当前view的顶级布局
     *
     * @param view
     * @return
     */
    public static View getRootView(View view) {
        if (view == null) {
            return null;
        }

        while (view.getParent() != null && view.getParent() instanceof View) {
            view = (View) view.getParent();
        }

        return view;
    }

    /**
     * 获取当前activity的顶级布局
     *
     * @param activity
     * @return
     */
    public static ViewGroup getRootView(Activity activity) {
        if (activity == null || activity.getWindow() == null) {
            return null;
        }

        return (ViewGroup) activity.getWindow().getDecorView().getRootView();
    }

    /**
     * 设置view及下属enable
     *
     * @param view
     * @param enabled
     */
    public static void setEnableExt(View view, boolean enabled) {
        if (view == null) {
            return;
        }

        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            for (int n = 0; n < ((ViewGroup) view).getChildCount(); n++) {
                View child = ((ViewGroup) view).getChildAt(n);
                if (child != null) {
                    child.setEnabled(enabled);
                    if (child instanceof ViewGroup) {
                        setEnableExt(child, enabled);
                    }
                }
            }
        }
    }
}
