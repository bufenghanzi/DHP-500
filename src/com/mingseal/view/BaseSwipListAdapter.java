package com.mingseal.view;

import android.widget.BaseAdapter;

/**
 * @author wj
 * 侧滑基类
 */
public abstract class BaseSwipListAdapter extends BaseAdapter {

    /**
     * @Title  getSwipEnableByPosition
     * @Description 
     * @author wj
     * @param position
     * @return true 可以滑动 false 不可以滑动
     */
    public boolean getSwipEnableByPosition(int position){
        return true;
    }

}
