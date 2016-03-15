/**
 * 
 */
package com.mingseal.activity;

import com.mingseal.data.dao.UserDao;
import com.mingseal.data.user.User;
import com.mingseal.dhp.R;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
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
 * @description 新增用户Activity
 */
public class UserAddActivity extends Activity implements OnClickListener{
	
	private final static String TAG = "UserAddActivity";
	
	private TextView tv_title;//标题
	private RelativeLayout rl_back;//返回上级菜单
	private RelativeLayout rl_add;//新增用户
	private EditText et_username;//用户名
	private EditText et_password;//密码
	private EditText et_password_again;//再次输入
	private ImageView iv_password;//隐藏显示密码框
	private ImageView iv_password_again;//隐藏显示再次输入密码框
	/**
	 * 密码隐藏标志
	 */
	private boolean isPasswordHide = true;
	/**
	 * 再次输入隐藏标志
	 */
	private boolean isPasswordHideAgain = true;
	private final static int KEY_PASSOWRD = 0x1;
	private final static int KEY_PASSWORD_AGAIN = 0x2;
	
	private UserDao userDao;
	
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_user);
		userDao = new UserDao(this);
		initView();
	}

	/**
	 * 加载组件
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.add_new_user));
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_add = (RelativeLayout) findViewById(R.id.rl_new_user);
		
		rl_back.setOnClickListener(this);
		rl_add.setOnClickListener(this);
		
		et_username  =(EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password_again = (EditText) findViewById(R.id.et_password_again);
		
		iv_password = (ImageView) findViewById(R.id.iv_show_password);
		iv_password_again = (ImageView) findViewById(R.id.iv_show_password_again);
		
		iv_password.setOnClickListener(new ShowOrHidePassword(et_password, iv_password, KEY_PASSOWRD));
		iv_password_again.setOnClickListener(new ShowOrHidePassword(et_password_again, iv_password_again, KEY_PASSWORD_AGAIN));
		
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

			if (key == KEY_PASSOWRD) {
				if (isPasswordHide) {
					// 点击隐藏密码框
					et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.show);
				} else {
					// 显示
					et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.hide);
				}
				isPasswordHide = !isPasswordHide;
			} else if (key == KEY_PASSWORD_AGAIN) {
				if (isPasswordHideAgain) {
					// 点击隐藏密码框
					et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.show);
				} else {
					// 显示
					et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					iv_show.setImageResource(R.drawable.hide);
				}
				isPasswordHideAgain = !isPasswordHideAgain;
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
	 * 检查组件是否为空
	 * @return true表示数据不为空,false表示有数据为空
	 */
	private boolean checkComponentsNull(){
		if("".equals(et_username.getText().toString())){
			ToastUtil.displayPromptInfo(this, "用户名不能为空");
			et_username.requestFocus();
			return false;
		}else if("".equals(et_password.getText().toString())){
			ToastUtil.displayPromptInfo(this, "密码不能为空");
			et_password.requestFocus();
			return false;
			
		}else if("".equals(et_password_again.getText().toString())){
			ToastUtil.displayPromptInfo(this, "再次输入不能为空");
			et_password_again.requestFocus();
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			//返回上级菜单
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			break;
		case R.id.rl_new_user:
			//新增用户
			if(checkComponentsNull()){
				if(et_password.getText().toString().equals(et_password_again.getText().toString())){
					//判断该用户名数据库中是否存在
					if(userDao.checkUserByUsername(et_username.getText().toString())){
						//存在该用户
						ToastUtil.displayPromptInfo(this, "用户名已存在");
						et_username.requestFocus();
					}else{
						//添加用户(只添加操作员)
						user = new User();
						user.setUsername(et_username.getText().toString());
						user.setPassword(et_password.getText().toString());
						user.setType(getResources().getString(R.string.login_user));
						userDao.insertUser(user);
						ToastUtil.displayPromptInfo(this, "新增加了一个用户名为"+et_username.getText().toString()+"的数据");
						setResult(MainAdminSettingActivity.RESULT_CODE, getIntent());
						finish();
						overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
					}
				}else{
					//两次输入密码不一致
					ToastUtil.displayPromptInfo(this, "两次输入密码不一致");
				}
			}
			break;

		}
	}
}
