/**
 * 
 */
package com.mingseal.activity;

import com.mingseal.application.UserApplication;
import com.mingseal.data.dao.UserDao;
import com.mingseal.data.user.User;
import com.mingseal.dhp.R;
import com.mingseal.utils.FileDatabase;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 商炎炳
 * @description 管理员系统设置界面
 */
public class MainAdminSettingActivity extends Activity implements OnClickListener {

	private final static String TAG = "MainAdminSettingActivity";
	/**
	 * MainAdminSettingActivity的requestCode
	 */
	public final static int REQUEST_CODE = 0x00000201;
	/**
	 * MainAdminSettingActivity的resultCode
	 */
	public final static int RESULT_CODE = 0x00000202;
	private TextView tv_title;//标题栏
	private RelativeLayout rl_back;//返回上级菜单
	private RelativeLayout rl_reset;// 恢复出厂设置
	private RelativeLayout rl_feedback;// 意见反馈
	private RelativeLayout rl_contact;// 联系我们
	private RelativeLayout rl_edit;// 修改密码
	private RelativeLayout rl_add;// 新增用户
	private EditText et_old_password;// 原密码
	private EditText et_new_password;// 新密码
	private EditText et_new_password_again;// 再次输入新密码
	private ImageView iv_show_old_password;// 隐藏显示原密码
	private ImageView iv_show_new_password;// 隐藏显示新密码
	private ImageView iv_show_new_password_again;// 隐藏显示再次输入密码
	/**
	 * 原密码隐藏标志
	 */
	private boolean isOldHide = true;
	/**
	 * 新密码隐藏标志
	 */
	private boolean isNewHide = true;
	/**
	 * 再次输入隐藏标志
	 */
	private boolean isNewHideAgain = true;
	private final static int KEY_OLD = 0x1;
	private final static int KEY_NEW = 0x2;
	private final static int KEY_NEW_AGAIN = 0x3;
	
	/**
	 * 保存用户的全局变量
	 */
	private UserApplication userApplication;
	private UserDao userDao;
	private User user = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting_admin);

		initView();
		userDao = new UserDao(this);
		//如果数据不为空，则获取保存的全局变量User
		userApplication = (UserApplication) getApplication();

	}

	/**
	 * 加载自定义组件
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.system_setting));
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_reset = (RelativeLayout) findViewById(R.id.rl_reset);
		rl_contact = (RelativeLayout) findViewById(R.id.rl_contactus);
		rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
		rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);
		rl_add = (RelativeLayout) findViewById(R.id.rl_add_new);
		rl_back.setOnClickListener(this);
		rl_reset.setOnClickListener(this);
		rl_contact.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_edit.setOnClickListener(this);
		rl_add.setOnClickListener(this);

		et_old_password = (EditText) findViewById(R.id.et_old_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_new_password_again = (EditText) findViewById(R.id.et_new_password_again);
		iv_show_old_password = (ImageView) findViewById(R.id.iv_show_old_password);
		iv_show_new_password = (ImageView) findViewById(R.id.iv_show_new_password);
		iv_show_new_password_again = (ImageView) findViewById(R.id.iv_show_new_password_again);
		iv_show_old_password.setOnClickListener(new ShowOrHidePassword(et_old_password, iv_show_old_password, KEY_OLD));
		iv_show_new_password.setOnClickListener(new ShowOrHidePassword(et_new_password, iv_show_new_password, KEY_NEW));
		iv_show_new_password_again.setOnClickListener(
				new ShowOrHidePassword(et_new_password_again, iv_show_new_password_again, KEY_NEW_AGAIN));
	}

	/**
	 * 密码框的隐藏与显示
	 *
	 */
	private class ShowOrHidePassword implements OnClickListener {
		private EditText et_password;// 判断输入的是哪个编辑框
		private ImageView iv_show;// 判断点击的是哪个编辑框后面的图像
		private int key;// 判断是哪个编辑框的标志位

		/**
		 * @param et_password
		 *            判断输入的是哪个编辑框
		 * @param iv_show
		 *            判断点击的是哪个编辑框后面的图像
		 * @param key
		 *            判断是哪个编辑框的标志位
		 */
		public ShowOrHidePassword(EditText et_password, ImageView iv_show, int key) {
			super();
			this.et_password = et_password;
			this.iv_show = iv_show;
			this.key = key;
		}

		@Override
		public void onClick(View arg0) {

			if (key == KEY_OLD) {
				if (isOldHide) {
					// 点击隐藏密码框
					et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.show);
				} else {
					// 显示
					et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.hide);
				}
				isOldHide = !isOldHide;
			} else if (key == KEY_NEW) {
				if (isNewHide) {
					// 点击隐藏密码框
					et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.show);
				} else {
					// 显示
					et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.hide);
				}
				isNewHide = !isNewHide;
			} else if (key == KEY_NEW_AGAIN) {
				if (isNewHideAgain) {
					// 点击隐藏密码框
					et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.show);
				} else {
					// 显示
					et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.hide);
				}
				isNewHideAgain = !isNewHideAgain;
			}
			et_password.postInvalidate();
			// 切换后将EditText光标置于末尾
			CharSequence charSequence = et_password.getText();
			if (charSequence instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence;
				Selection.setSelection(spanText, charSequence.length());
			}
		}

	}

	/**
	 * 判断输入框是否为空
	 * @return true表示都不为空,false表示有空数据
	 */
	private boolean checkPasswordNull() {
		if("".equals(et_old_password.getText().toString())){
			ToastUtil.displayPromptInfo(this, "原密码不能为空");
			et_old_password.requestFocus();
			return false;
		}else if("".equals(et_new_password.getText().toString())){
			ToastUtil.displayPromptInfo(this, "新密码不能为空");
			et_new_password.requestFocus();
			return false;
		}else if("".equals(et_new_password_again.getText().toString())){
			ToastUtil.displayPromptInfo(this, "再次输入不能为空");
			et_new_password_again.requestFocus();
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			// 返回上级菜单
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			break;
		case R.id.rl_reset:
			// 恢复出厂设置
			FileDatabase.exportToFile(this);
			break;
		case R.id.rl_feedback:
			// 意见反馈
			break;
		case R.id.rl_contactus:
			// 联系我们
			break;
		case R.id.rl_add_new:
			//新增用户
			Intent intent = new Intent(this, UserAddActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
			break;
		case R.id.rl_edit:
			// 修改密码
			if(checkPasswordNull()){
				user = userApplication.getUser();
				if(et_old_password.getText().toString().equals(user.getPassword())){
					if(et_new_password.getText().toString().equals(et_new_password_again.getText().toString())){
						//保存新密码
						user.setPassword(et_new_password.getText().toString());
						userDao.updateUser(user);
						ToastUtil.displayPromptInfo(this, "密码修改成功");
						//全局变量的User需要重新设置
						userApplication.setUser(user);
						//之后要退回到之前的Activity中(还没写)
						//2015/12/10
						setResult(TaskListActivity.ADMIN_SETTING_RESULT_CODE, getIntent());
						finish();
						overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
					}else{
						ToastUtil.displayPromptInfo(this, "两次输入密码不一致");
						et_new_password_again.requestFocus();
					}
				}else{
					ToastUtil.displayPromptInfo(this, "原密码输入错误!");
					et_old_password.requestFocus();
				}
			}
			break;

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
