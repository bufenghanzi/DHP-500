package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.mingseal.activity.TaskActivity;
import com.mingseal.data.dao.GlueInputDao;
import com.mingseal.data.dao.GlueOutputDao;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.utils.FloatUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author 商炎炳
 *
 */
public class TaskMainBaseAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;

	private List<Point> pointLists;
	// private ViewHolder holder = null;
	private final static String TAG = "TaskMainBaseAdapter";

	/**
	 * @Fields isSelected: 用来控制CheckBox的选中状况
	 */
	private static LinkedHashMap<Integer, Boolean> isSelected;

	/**
	 * @Fields singeOrMultify: true显示多选框,false显示单选框
	 */
	private boolean singeOrMultify = false;// 单选框标志,true隐藏单选框
	
	/**
	 * @Fields m_nAxisNum: 判断收到的是4轴还是3轴
	 */
	private int m_nAxisNum = 4;//判断收到的是4轴还是3轴

	private onMyRadioButtonChangedListener mRadioChange;
	private onMyCheckboxChangedListener mCheckBoxChange;
	private int selectID;// 选中的ID
	/**
	 * @Fields selectCheckIDs: 复选框选中的IDs
	 */
	private List<Integer> selectCheckIDs = new ArrayList<>();//复选框选中的IDs

	private int type_value;// PointType类型之index

	private TaskActivity activity;
	
	private static int KEY_X = 0;
	private static int KEY_Y = 1;
	private static int KEY_Z = 2;
	private static int KEY_U = 3;
	private Point point ;
	/**
	 * @Fields inputDao: 输入口的数据库操作
	 */
	private GlueInputDao inputDao;
	/**
	 * @Fields outputDao: 输出口的数据库操作
	 */
	private GlueOutputDao outputDao;
	/**
	 * @Fields openStr: 开和关的状态
	 */
	private String openCloseStr;

	public TaskMainBaseAdapter(Context context, TaskActivity activity) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.pointLists = new ArrayList<Point>();
		isSelected = new LinkedHashMap<Integer, Boolean>();
		this.activity = activity;
		this.inputDao = new GlueInputDao(context);
		this.outputDao = new GlueOutputDao(context);
	}

	public TaskMainBaseAdapter() {
	}

	/**
	 * 设置当前选中的selectID
	 * 
	 * @param position
	 */
	public void setSelectID(int position) {
		this.selectID = position;
	}
	
	/**
	 * 设置复选框选中的IDs
	 * @Title setSelectCheckIDS
	 * @Description 设置复选框选中的IDs
	 * @param selectCheckbox 复选框选中的id
	 */
	public void setSelectCheckIDS(List<Integer> selectCheckbox){
		this.selectCheckIDs = selectCheckbox;
	}

	/**
	 * 自定义List数据
	 * 
	 * @param list
	 */
	public void setData(List<Point> list) {
		this.pointLists = list;
		// 初始化hashMap,否则之前删除的true值会继续保持在map集合中
		isSelected = new LinkedHashMap<Integer, Boolean>();
		// 初始化isSelected()数据
		if (list != null) {
			for (int i = 0; i < pointLists.size(); i++) {
				getIsSelected().put(i, false);
			}
		}
//		Log.i(TAG, "setData():List<Point>:" + list.toString());

	}

	public LinkedHashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(LinkedHashMap<Integer, Boolean> isSelected) {
		TaskMainBaseAdapter.isSelected = isSelected;
	}

	/**
	 * 设置是多选还是单选按钮
	 * 
	 * @param singleFlag true为显示多选,false显示单选
	 */
	public void setSingleOrMultify(boolean singleFlag) {
		this.singeOrMultify = singleFlag;
	}
	
	/**
	 * @Title: setM_nAxisNum
	 * @Description: 设置机器轴数，是三轴还是四轴
	 * @param m_nAxisNum 机器轴数
	 */
	public void setM_nAxisNum(int m_nAxisNum) {
		this.m_nAxisNum = m_nAxisNum;
	}

	@Override
	public int getCount() {
		if (pointLists == null) {
			return 0;

		} else {
			return pointLists.size();
		}
	}

	@Override
	public Point getItem(int position) {
		return pointLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_task_main_listview_item, null);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_x = (EditText) convertView.findViewById(R.id.edit_x);
			holder.tv_y = (EditText) convertView.findViewById(R.id.edit_y);
			holder.tv_z = (EditText) convertView.findViewById(R.id.edit_z);
			holder.tv_u = (EditText) convertView.findViewById(R.id.edit_u);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb1);
			holder.rb = (RadioButton) convertView.findViewById(R.id.rb1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		  holder.tv_x.setSelectAllOnFocus(true);
		  holder.tv_y.setSelectAllOnFocus(true);
		  holder.tv_z.setSelectAllOnFocus(true);
		  holder.tv_u.setSelectAllOnFocus(true);
		 
		
		if(m_nAxisNum == 3){
			holder.tv_u.setVisibility(View.GONE);
		}else if(m_nAxisNum == 4){
			holder.tv_u.setVisibility(View.VISIBLE);
		}
		

		// true显示多选框，隐藏单选框
		if (singeOrMultify) {
			holder.cb.setVisibility(View.VISIBLE);
			holder.rb.setVisibility(View.GONE);
			convertView.setBackgroundColor(Color.TRANSPARENT);
			if(selectCheckIDs.contains(position)){
				convertView.setBackgroundColor(activity.getResources().getColor(R.color.listview_selected));
			}else{
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
		} else {
			holder.cb.setVisibility(View.GONE);
			holder.rb.setVisibility(View.VISIBLE);
			//选中行，改变当前行的颜色
			Log.d(TAG, "position:"+position);
			if(position == selectID){
				convertView.setBackgroundColor(activity.getResources().getColor(R.color.listview_selected));
			}else{
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}

		}
		if (pointLists != null && pointLists.size() != 0) {
			point = getItem(position);

			holder.tv_num.setText(String.valueOf(position + 1));
			type_value = point.getPointParam().getPointType().getValue();
			holder.tv_type.setText(PointType.getTypeName(type_value));
//			holder.tv_x.setText(String.valueOf(RobotParam.INSTANCE.XPulse2Journey(point.getX())));
//			holder.tv_y.setText(String.valueOf(RobotParam.INSTANCE.YPulse2Journey(point.getY())));
//			holder.tv_z.setText(String.valueOf(RobotParam.INSTANCE.ZPulse2Journey(point.getZ())));
//			holder.tv_u.setText(String.valueOf(RobotParam.INSTANCE.UPulse2Journey(point.getU())));
//			Log.d(TAG, "-->"+point.getPointParam().getPointType().toString());
			if (point.getPointParam().getPointType().equals(PointType.POINT_GLUE_INPUT)
					|| point.getPointParam().getPointType().equals(PointType.POINT_GLUE_OUTPUT)) {
				openCloseStr = getIOInfo(point);
				holder.tv_x.setText(openCloseStr);
				holder.tv_y.setText("");
				holder.tv_z.setText("");
				holder.tv_u.setText("");
				holder.tv_x.setEnabled(false);
				holder.tv_y.setEnabled(false);
				holder.tv_z.setEnabled(false);
				holder.tv_u.setEnabled(false);
			} else {
				holder.tv_x.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(point.getX())));
				holder.tv_y.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(point.getY())));
				holder.tv_z.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(point.getZ())));
				holder.tv_u.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.UPulse2Journey(point.getU())));
				holder.tv_x.setEnabled(true);
				holder.tv_y.setEnabled(true);
				holder.tv_z.setEnabled(true);
				holder.tv_u.setEnabled(true);
				final EditText et_x = holder.tv_x;
				final EditText et_y = holder.tv_y;
				final EditText et_z = holder.tv_z;
				final EditText et_u = holder.tv_u;

				holder.tv_x.setOnTouchListener(new OnKeyTouchListener());
				holder.tv_y.setOnTouchListener(new OnKeyTouchListener());
				holder.tv_z.setOnTouchListener(new OnKeyTouchListener());
				holder.tv_u.setOnTouchListener(new OnKeyTouchListener());

				holder.tv_x.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_x, KEY_X));
				holder.tv_y.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_y, KEY_Y));
				holder.tv_z.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_z, KEY_Z));
				holder.tv_u.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_u, KEY_U));

				holder.tv_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_x, KEY_X));
				holder.tv_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_y, KEY_Y));
				holder.tv_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_z, KEY_Z));
				holder.tv_u.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_u, KEY_U));
			}
			
			
//			final EditText et_x = holder.tv_x;
//			final EditText et_y = holder.tv_y;
//			final EditText et_z = holder.tv_z;
//			final EditText et_u = holder.tv_u;

			/*
			 * //点击时全选 et_x.setSelectAllOnFocus(true);
			 * et_y.setSelectAllOnFocus(true); et_z.setSelectAllOnFocus(true);
			 * et_u.setSelectAllOnFocus(true);
			 */

			// 点击类型更改
			holder.tv_type.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (activity.ismIsFirst()) {
						activity.setmIsFirst(false);
						// int height = v.getHeight();
						activity.popMenu = new MyPopWindowClickListener(activity);
						activity.mPopupWindow = activity.popMenu.getMenu();
					}
					activity.popMenu.setPoint(pointLists, getItem(position), 1, TaskMainBaseAdapter.this);
					// mPopupWindow.setFocusable(true);
					activity.mPopupWindow.setOutsideTouchable(true); // 设置点击屏幕其它地方弹出框消失
					/*=================== begin ===================*/
					activity.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
					/*===================  add  ===================*/
					if (activity.mPopupWindow == null) {
						return;
					}
					if (activity.mPopupWindow.isShowing()) {
						activity.mPopupWindow.dismiss();
						return;
					}
					// 设置popwindow的显示和消失动画
					activity.mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
					// 改变后面的数值就能上下平移
					activity.mPopupWindow.showAtLocation(v, Gravity.LEFT, 0, 0);

					selectID = position;
					//显示单选框
					singeOrMultify = false;
					if (mRadioChange != null) {
						mRadioChange.setSelectID(selectID);
					}
				}
			});

			// 监听checkBox并根据原来的状态来设置新的状态
			holder.cb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isSelected.get(position)) {
						isSelected.put(position, false);
						setIsSelected(isSelected);
					} else {
						isSelected.put(position, true);
						setIsSelected(isSelected);
					}
					// Log.d(TAG, "isSelected.get(position):"+isSelected);
					if (mCheckBoxChange != null) {
						mCheckBoxChange.setCheckBoxSelected(isSelected);
					}
				}
			});
			holder.cb.setChecked(getIsSelected().get(position));

			// 核心方法，判断单选按钮被按下的位置与之前的位置是否相等，然后做相应的操作。
			if (selectID == position) {
				holder.rb.setChecked(true);
			} else {
				holder.rb.setChecked(false);
			}

			holder.rb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectID = position;
					if (mRadioChange != null) {
						mRadioChange.setSelectID(selectID);
					}
				}
			});

//			holder.tv_x.setOnTouchListener(new OnKeyTouchListener());
//			holder.tv_y.setOnTouchListener(new OnKeyTouchListener());
//			holder.tv_z.setOnTouchListener(new OnKeyTouchListener());
//			holder.tv_u.setOnTouchListener(new OnKeyTouchListener());
//
//			holder.tv_x.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_x, KEY_X));
//			holder.tv_y.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_y, KEY_Y));
//			holder.tv_z.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_z, KEY_Z));
//			holder.tv_u.setOnEditorActionListener(new OnKeyEditorActionListener(point, et_u, KEY_U));
//
//			holder.tv_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_x, KEY_X));
//			holder.tv_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_y, KEY_Y));
//			holder.tv_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_z, KEY_Z));
//			holder.tv_u.setOnFocusChangeListener(new OnKeyFocusChangeListener(point, et_u, KEY_U));

		}

		return convertView;
	}

	/**
	 * 自定义OnFocusChangeListene,失去焦点时，保存当前的内容
	 * 
	 */
	private class OnKeyFocusChangeListener implements OnFocusChangeListener {

		private Point point;
		private EditText et;
		private int key;// 判断是x,y,z,u
		private double value;

		/**
		 * 失去焦点时，保存当前的内容
		 * 
		 * @param point
		 *            Point
		 * @param edittext
		 *            Edittext
		 * @param key
		 *            判断是x,y,z,u中的哪一个
		 */
		public OnKeyFocusChangeListener(Point point, EditText et, int key) {
			this.point = point;
			this.et = et;
			this.key = key;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				((EditText) v).setCursorVisible(false);
				try {
					value = Double.parseDouble(et.getText().toString());
				} catch (NumberFormatException e) {
					value = 0;
				}
				if (key == KEY_X) {
					if(value>RobotParam.INSTANCE.GetXJourney()){
						value = RobotParam.INSTANCE.GetXJourney();
					}else if(value<0){
						value = 0;
					}
					point.setX(RobotParam.INSTANCE.XJourney2Pulse(value));
				} else if (key == KEY_Y) {
					if(value>RobotParam.INSTANCE.GetYJourney()){
						value = RobotParam.INSTANCE.GetYJourney();
					}else if(value<0){
						value = 0;
					}
					point.setY(RobotParam.INSTANCE.YJourney2Pulse(value));
				} else if (key == KEY_Z) {
					if(value>RobotParam.INSTANCE.GetZJourney()){
						value = RobotParam.INSTANCE.GetZJourney();
					}else if(value<0){
						value = 0;
					}
					point.setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
				} else if (key == KEY_U) {
					if(value>RobotParam.INSTANCE.GetUJourney()){
						value = RobotParam.INSTANCE.GetUJourney();
					}else if(value<0){
						value = 0;
					}
					point.setU(RobotParam.INSTANCE.UJourney2Pulse(value));
				}
				et.setText(value+"");
//				notifyDataSetChanged();
			}
		}

	}

	/**
	 * 自定义的OnEditorActionListener,软键盘输入回车，将数据保存到List集合中
	 *
	 */
	private class OnKeyEditorActionListener implements OnEditorActionListener {

		private Point point;
		private EditText et;
		private int key;// 判断是x,y,z,u
		private double value;

		/**
		 * 软键盘输入回车，将数据保存到List集合中
		 * 
		 * @param point
		 *            Point
		 * @param et
		 *            Edittext
		 * @param key
		 *            判断是x,y,z,u中的哪一个
		 */
		public OnKeyEditorActionListener(Point point, EditText et, int key) {
			this.point = point;
			this.et = et;
			this.key = key;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				try {
					value = Double.parseDouble(et.getText().toString());
				} catch (NumberFormatException e) {
					value = 0;
				}
				if (key == KEY_X) {
					if(value>RobotParam.INSTANCE.GetXJourney()){
						value = RobotParam.INSTANCE.GetXJourney();
					}else if(value<0){
						value = 0;
					}
					point.setX(RobotParam.INSTANCE.XJourney2Pulse(value));
				} else if (key == KEY_Y) {
					if(value>RobotParam.INSTANCE.GetYJourney()){
						value = RobotParam.INSTANCE.GetYJourney();
					}else if(value<0){
						value = 0;
					}
					point.setY(RobotParam.INSTANCE.YJourney2Pulse(value));
				} else if (key == KEY_Z) {
					if(value>RobotParam.INSTANCE.GetZJourney()){
						value = RobotParam.INSTANCE.GetZJourney();
					}else if(value<0){
						value = 0;
					}
					point.setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
				} else if (key == KEY_U) {
					if(value>RobotParam.INSTANCE.GetUJourney()){
						value = RobotParam.INSTANCE.GetUJourney();
					}else if(value<0){
						value = 0;
					}
					point.setU(RobotParam.INSTANCE.UJourney2Pulse(value));
				}
				et.setText(value+"");
				notifyDataSetChanged();
			}

			return false;
		}

	}
	
	/**
	 * 输入输出口的状态
	 * @Title getIOInfo
	 * @Description 输入输出口的状态
	 * @param point
	 * @return 输入输出口的状态
	 */
	private String getIOInfo(Point point){
		String result = "";
		PointGlueInputIOParam inputParam;
		PointGlueOutputIOParam outputParam;
		int id = point.getPointParam().get_id();
		PointType type = point.getPointParam().getPointType();
		boolean[] io = new boolean[12];
		if(type == PointType.POINT_GLUE_INPUT){
			inputParam = inputDao.getInputPointByID(id);
			io = inputParam.getInputPort();
			result = getOpenOrClose(io);
		}else if(type == PointType.POINT_GLUE_OUTPUT){
			outputParam = outputDao.getOutPutPointByID(id);
			io = outputParam.getInputPort();
			result = getOpenOrClose(io);
		}
		return result;
		
	}
	
	/**
	 * @Title getOpenOrClose
	 * @Description 返回输入口的状态
	 * @param ios 输入口状态
	 * @return 开/关
	 */
	private String getOpenOrClose(boolean[] ios) {
		String result = "";
		for (int i=0;i<4;i++) {
			if (ios[i]) {
				result += "开";
			} else {
				result += "关";
			}
		}
		return result;

	}

	/**
	 * 自定义onTouchListner,用于点击列表中的选项时能获取焦点
	 *
	 */
	private class OnKeyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			((EditText) v).setCursorVisible(true);
			//全选
//			((EditText) v).setSelectAllOnFocus(true);

			return false;
		}

	}

	/**
	 * 自定义checkbox回调函数
	 * 
	 * @param l
	 */
	public void setOnCheckboxChanged(onMyCheckboxChangedListener l) {
		mCheckBoxChange = l;
	}

	/**
	 * 自定义checkbox接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface onMyCheckboxChangedListener {
		void setCheckBoxSelected(LinkedHashMap<Integer, Boolean> isSelected);

	}

	/**
	 * 回调函数
	 * 
	 * @param l
	 */
	public void setOnRadioButtonChanged(onMyRadioButtonChangedListener l) {
		mRadioChange = l;
	}

	/**
	 * 自定义接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface onMyRadioButtonChangedListener {
		void setSelectID(int selectID);
	}

	private static class ViewHolder {
		private TextView tv_num;// 第几个数据
		private TextView tv_type;// 什么类型的数据
		private EditText tv_x;// X轴的数据
		private EditText tv_y;// Y轴的数据
		private EditText tv_z;// Z轴的数据
		private EditText tv_u;// U轴的数据

		private CheckBox cb;// 多选框的选择情况
		private RadioButton rb;// 单选框的选择情况

	}

}
