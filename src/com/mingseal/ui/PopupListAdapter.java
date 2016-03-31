package com.mingseal.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;



/**
 * @author wj
 *
 */
public class PopupListAdapter extends BaseAdapter {
    ArrayList<? extends PopupView> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = items.get(i).getPopupView();
        return view;
    }

    public void setItems(ArrayList<? extends PopupView> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    public ArrayList<? extends PopupView> getItems(){
		return items;
    }
}
