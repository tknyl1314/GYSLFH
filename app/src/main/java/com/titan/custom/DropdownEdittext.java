package com.titan.custom;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.OnDismissListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.otitan.gyslfh.R;

import java.util.List;

public class DropdownEdittext extends LinearLayout {
	
	private AutoCompleteTextView tv;
	//private Button btn;
	ImageView btn;
	Context mcontext;
	long dismissTime;//设置标记防止事件重复执行
	View DropdownEditText;
   public DropdownEdittext(Context context){
       super(context);
   }
	public DropdownEdittext(Context context, AttributeSet attrs) {
		super(context, attrs);
		 if(!isInEditMode())
		 {
			 DropdownEditText=LayoutInflater.from(context).inflate(R.layout.dropdown_edittextview1, this);
				this.mcontext = context;
				tv = (AutoCompleteTextView) findViewById(R.id.autotextview);
				tv.setDropDownBackgroundResource(R.color.white);
				//tv.setDropDownHeight()
				btn = (ImageView) findViewById(R.id.btn_dropdown);
			    applyAttributes(attrs);
				//设置匹配字符
				tv.setThreshold(100);
				//设置下拉宽度
				tv.setDropDownAnchor(this.getId());
				//Drawable dr=tv.getDropDownBackground();
				btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					  
						   if(!tv.isPopupShowing()&& (System.currentTimeMillis() - dismissTime > 100)){
								
								//btn.setBackgroundResource(R.drawable.btn_down);
							   btn.setImageResource(R.drawable.btn_down);
							   tv.showDropDown();
						   }
					}
				});
				
				tv.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						//btn.setBackgroundResource(R.drawable.btn_up);
						 btn.setImageResource(R.drawable.btn_up);
						dismissTime=System.currentTimeMillis(); 
				
					}
				});
		 }
	}

	/**
	 * 设置属性
	 * @param attrs
	 */
	protected void applyAttributes(AttributeSet attrs) {
		// Slight contortion to prevent allocating in onLayout

		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DropdownEditTextView);
		String  text = typedArray.getString(R.styleable.DropdownEditTextView_text);
		String  hint =typedArray.getString(R.styleable.DropdownEditTextView_hint);
		tv.setHint(hint);
		tv.setText(text);
		typedArray.recycle();
	}

	public void setAdapter(String[] datalist) {
		//android.R.layout.simple_dropdown_item_1line
		if(datalist!=null&&datalist.length>0)
			
		{
	    btn.setEnabled(true);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext,   R.layout.dropdown_item,
				datalist);
		
		tv.setAdapter(adapter);
		}
		else {
			btn.setEnabled(false);
		}
	}
	public void setAdapter(List<String> datalist) {
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext,  android.R.layout.simple_list_item_1,
				datalist);*/
		if(datalist!=null&&datalist.size()>0)
		{
			btn.setEnabled(true);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext, R.layout.dropdown_item,datalist);
			tv.setAdapter(adapter);	
		}
		else {
			btn.setEnabled(false);
		}
	}
	public void setCompletionHint(int number)
	{
		tv.setCompletionHint("最近登录用户"+number+"个");
	}
	//获取数据
	public String getText() {
		String tv_text = tv.getText().toString();
		if (tv_text == null) {
			tv_text = "";
		}
		return tv_text;
	}
	//设置数据
	public void setText(CharSequence text) {
		tv.setText(text);
	}


}
