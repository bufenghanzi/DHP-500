/**
 * 
 */
package com.mingseal.activity;

import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author 商炎炳
 * @description
 */
public class TestActivity extends Activity implements OnClickListener{

	private EditText et_number;
	private EditText et_number2;
	private Button but_u_minus;
	private Button but_u_plus;
	private Button but_z_plus;
	private Button but_z_minus;
	private Button but_y_plus;
	private Button but_y_minus;
	private Button but_x_plus;
	private Button but_x_minus;

	private int MAX_VALUE = 6000;
	private int MIN_VALUE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_test);
		et_number = (EditText) findViewById(R.id.et_num);
		et_number2 = (EditText) findViewById(R.id.et_num2);
		but_u_minus = (Button) findViewById(R.id.nav_u_minus);
		but_u_plus = (Button) findViewById(R.id.nav_u_plus);
		but_z_minus = (Button) findViewById(R.id.nav_z_minus);
		but_z_plus = (Button) findViewById(R.id.nav_z_plus);
		but_y_minus = (Button) findViewById(R.id.nav_y_minus);
		but_y_plus = (Button) findViewById(R.id.nav_y_plus);
		but_x_minus = (Button) findViewById(R.id.nav_x_minus);
		but_x_plus = (Button) findViewById(R.id.nav_x_plus);
//		but_u_minus.setEnabled(false);
		but_u_minus.setOnClickListener(this);
		but_u_plus.setOnClickListener(this);
		but_z_minus.setOnClickListener(this);
		but_z_plus.setOnClickListener(this);
		but_y_minus.setOnClickListener(this);
		but_y_plus.setOnClickListener(this);
		but_x_minus.setOnClickListener(this);
		but_x_plus.setOnClickListener(this);
		et_number.addTextChangedListener(new MaxMinEditWatcher(6000, 0, et_number));
		et_number2.addTextChangedListener(new MaxMinEditWatcher(3000, 0, et_number2));
		
		et_number.setOnFocusChangeListener(new MaxMinFocusChangeListener(6000, 0, et_number));
		et_number2.setOnFocusChangeListener(new MaxMinFocusChangeListener(3000, 0, et_number2));
		
		et_number.setSelectAllOnFocus(true);
		et_number2.setSelectAllOnFocus(true);
		
//		but_number.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				ToastUtil.displayPromptInfo(TestActivity.this, ""+Integer.parseInt(et_number.getText().toString()));
//			}
//		});
	}

	/* 
	 * <p>Title: onClick</p>
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
	}
	
	
}
