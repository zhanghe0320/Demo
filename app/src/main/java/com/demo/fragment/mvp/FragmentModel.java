package com.demo.fragment.mvp;

import com.demo.fragment.dao.FragmentBean;
import com.demo.view.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 此处使用线程
 */
public class FragmentModel implements FragmentContract.IFragmentModel {
    
    @Override
    public List Get(String mess) {
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            String text = "";
            for (int j = 0; j < (int) (1 + Math.random() * (100 - 1 + 1)); j++) {
                text = text + "信息信息";
            }
            list.add(
                    new FragmentBean(((int) (1 + Math.random() * (10 - 1 + 1)) + mess + "标题"), "", "", "按钮" + i, text));
        }
        return list;
    }
    
    @Override
    public void Set(String mess) {
        
    }
    
    @Override
    public List Change(String mess) {
        List list = new ArrayList();
        ThreadUtils.getInstance().runOnStdAsyncThread(new Runnable() {
            @Override
            public void run() {
                
                for (int i = 0; i < 10; i++) {
                    String text = "";
                    for (int j = 0; j < (int) (1 + Math.random() * (100 - 1 + 1)); j++) {
                        text = text + "信息信息";
                    }
                    list.add(new FragmentBean(((int) (1 + Math.random() * (10 - 1 + 1)) + mess + "标题"), "", "",
                            "按钮" + i, text));
                }
            }
        });
        
        return list;
    }
    
    @Override
    public List Other(String mess) {
        
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            String text = "";
            for (int j = 0; j < (int) (1 + Math.random() * (100 - 1 + 1)); j++) {
                text = text + "信息信息";
            }
            list.add(
                    new FragmentBean(((int) (1 + Math.random() * (10 - 1 + 1)) + mess + "标题"), "", "", "按钮" + i, text));
        }
        return list;
    }
}
