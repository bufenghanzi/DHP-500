package com.mingseal.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import com.mingseal.dhp.R;



/**
 * @author wj
 * 存放以及管理所有的popupView
 */
public class PopupListView extends RelativeLayout {
    Context context;
    ListView listView;
    LinearLayout extendView;
    PopupListAdapter popupListAdapter;
    View extendPopupView;
    View extendInnerView;
    Handler handler = new Handler();
    int startY;
    int moveY = 0;
    int heightSpace = 0;//条目高度
    int innerViewAlphaVal = 0;
    int listViewAlphaVal = 10;
    
    private OnClickPositionChanged listener;
	private OnZoomInChanged stateListener;
    /**
     * 把当前项对应的iew保存到mCurrentCheckedOption成员变量中
     */
    private View mCurrentCheckedOption;
    private int position;
	private ArrayList<Integer> list;

    public PopupListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        popupListAdapter = new PopupListAdapter();
    }

    /**
     * 
     * @param customListView 允许自定义的listview
     */
    public void init(ListView customListView) {
        setHeightSpace();
        RelativeLayout.LayoutParams listParams = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams extendsParams = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (customListView == null) {
            listView = new ListView(context);
        } else {
            listView = customListView;
        }

        if (extendView == null) {
            extendView = new LinearLayout(context);
            extendView.setOrientation(LinearLayout.VERTICAL);
        }
        listView.setDivider(null);
        listView.setSelector(android.R.color.transparent);
        listView.setLayoutParams(listParams);
        listView.setAdapter(popupListAdapter);
        //选择该方案
        listView.setOnItemClickListener(selectTask);
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				listener.getCurrentPositon(position);//+1为方案号
				int[] p = new int[2];
		          //获取以屏幕为原点一个控件在其整个屏幕的位置。
		            view.getLocationOnScreen(p);
		            //p[1]代表y轴坐标，在整个屏幕中
		            startY = p[1] - heightSpace;
		            moveY = startY;
		            zoomIn(position, startY);
				return true;//拦截事件，只是查看方案，不是选择该方案。
			}
		});
        // ListView 的 item 显示一个选择图标,通过控制它的显示和隐藏来达到看起来选中的效果在 ListView 中 item 数量不多时对内存、性能等影响不大
        //setAdapter() 其实是异步的 ，调用getChildAt这个方法， ListView 的 item 并没有立马创建，而是在下一轮消息处理时才创建。
        //使用 post() 提交一个 Runnable() 对象，在 Runnable() 内部来做默认选中这种初始化动作。
        listView.post(new Runnable() {

			@Override
			public void run() {
				//滑动会改变孩子的positioon
				int realPosition=position-listView.getFirstVisiblePosition();
				mCurrentCheckedOption = listView.getChildAt(realPosition).findViewById(R.id.iv_selected);
				mCurrentCheckedOption.setVisibility(View.VISIBLE);
			}
		});
        extendView.setLayoutParams(extendsParams);
        extendView.setVisibility(GONE);
        this.addView(listView);
        this.addView(extendView);
    }

    /**
     * @Title  setPosition
     * @Description 外部设置当前的item项被选中
     * @author wj
     * @param position
     */
    public void setPosition(int position) {
    	listView.setSelection(position);
		this.position=position;
	}
    
    public void setItemViews(ArrayList<? extends PopupView> items) {
        popupListAdapter.setItems(items);
    }
    public ArrayList<? extends PopupView> getItemViews(){
    	return popupListAdapter.getItems();
    }

    private AdapterView.OnItemClickListener selectTask = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        	if (list.contains(position+1)) {
				
        		//滚动时列表值挥发上改变
        		int realPosition=position-listView.getFirstVisiblePosition();
        		mCurrentCheckedOption.setVisibility(View.GONE);
        		mCurrentCheckedOption = listView.getChildAt(realPosition).findViewById(R.id.iv_selected);
        		mCurrentCheckedOption.setVisibility(View.VISIBLE);
			}
            
        }
    };

    /**
     * 放大
     * @param i
     * @param startY
     */
    public void zoomIn(int i, int startY) {
    	//使listview消失
        listView.setVisibility(GONE);
        if (extendPopupView != null) {
            extendPopupView = null;
        }
        extendPopupView = ((PopupView) popupListAdapter.getItem(i)).getExtendPopupView();
        extendInnerView = ((PopupView) popupListAdapter.getItem(i)).getExtendView();
        //添加子view
        extendView.addView(extendPopupView);
        extendPopupView.setY(startY);
        extendInnerView.setVisibility(GONE);
        extendView.addView(extendInnerView);
        extendView.setVisibility(VISIBLE);
        stateListener.getZoomState(true);
        handler.postDelayed(zoomInRunnable, 100);
    }

    /**
     * @Title  zoomOut
     * @Description 缩小
     * @author wj
     */
    public void zoomOut() {
    	stateListener.getZoomState(false);
        handler.removeCallbacks(zoomInRunnable);
        handler.postDelayed(zoomOutRunnable, 1);
    }

    /**
     * @Title  isItemZoomIn
     * @Description 判断具体内容是否是放大的状态
     * @author wj
     * @return true放大的状态  false缩小的状态
     */
    public boolean isItemZoomIn() {
        if (extendView.getVisibility() == VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public Runnable zoomInRunnable = new Runnable() {
        @Override
        public void run() {
            if (listViewAlphaVal >= 0) {
                listView.setAlpha(listViewAlphaVal * 0.1f);
                listViewAlphaVal--;
                handler.postDelayed(zoomInRunnable, 10);
            } else {
                if (listView.getVisibility() != GONE) {
                    listView.setVisibility(GONE);
                }
                if (moveY > 0) {
                    moveY -= startY / 10;
                    extendPopupView.setY(moveY);
                    handler.postDelayed(zoomInRunnable, 10);
                } else {
                    extendPopupView.setY(0);
                    if (innerViewAlphaVal < 10) {
                        extendInnerView.setAlpha(innerViewAlphaVal * 0.1f);
                        extendInnerView.setVisibility(VISIBLE);
                        innerViewAlphaVal++;
                        handler.postDelayed(zoomInRunnable, 10);
                    }
                }
            }
        }
    };

    public Runnable zoomOutRunnable = new Runnable() {
        @Override
        public void run() {

            if (innerViewAlphaVal > 0) {
                extendInnerView.setAlpha(innerViewAlphaVal * 0.1f);
                innerViewAlphaVal--;
                handler.postDelayed(zoomOutRunnable, 1);
            } else {
                if (extendInnerView.getVisibility() != GONE) {
                    extendInnerView.setVisibility(GONE);
                }
                if (moveY < startY) {
                    moveY += (startY) / 10;
                    extendPopupView.setY(moveY);
                    handler.postDelayed(zoomOutRunnable, 10);
                } else {
                    if (listViewAlphaVal < 10) {
                        listViewAlphaVal++;
                        if (listView.getVisibility() == GONE) {
                            listView.setVisibility(VISIBLE);
                        }
                        listView.setAlpha(listViewAlphaVal * 0.1f);
                        handler.postDelayed(zoomOutRunnable, 10);
                    } else {
                        if (extendPopupView != null) {
                            extendPopupView.setY(startY);
                            extendView.setVisibility(GONE);
                            extendView.removeAllViews();
                            extendPopupView = null;
                        }
                    }
                }
            }


        }
    };

    /**
     * @Title  setHeightSpace
     * @Description 设置view的高度
     * @author wj
     */
    public void setHeightSpace() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
      //获取ActionBar的高度 
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources()
                    .getDisplayMetrics());
        }

        int result = 0;
        //获取状态栏的高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        
        float scale = context.getResources().getDisplayMetrics().density;
        int viewHeight=(int) (96* scale + 0.5f);
        System.out.println("------>"+viewHeight);
        this.heightSpace = actionBarHeight + result+viewHeight;
    }
    
    /**
     * @author wj
     * 接口回传当前点击位置
     */
    public interface OnClickPositionChanged{
		
		public void getCurrentPositon(int position);
	}
    public void setOnClickPositionChanged(OnClickPositionChanged onClickPositionChanged) {
		listener = onClickPositionChanged;
	}
    /**
     * @author wj
     * 观察extendView是否放大
     */
    public interface OnZoomInChanged{
    	
    	public void getZoomState(Boolean isZoomIn);
    }
    /**
     * @Title  setOnZoomInListener
     * @Description 观察extendView是否放大
     * @author wj
     * @param onZoomInChanged
     */
    public void setOnZoomInListener(OnZoomInChanged onZoomInChanged) {
    	stateListener = onZoomInChanged;
    }

	/**
	 * @Title  setSelectedEnable
	 * @Description 
	 * @author wj
	 * @param list 存放可被点击的方案号对应列表的序号
	 */
	public void setSelectedEnable(ArrayList<Integer> list) {
		this.list=list;
	}
}
