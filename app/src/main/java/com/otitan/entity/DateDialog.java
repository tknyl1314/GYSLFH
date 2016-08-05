package com.otitan.entity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.otitan.gyslfh.R;
import com.otitan.util.TosGallery;
import com.otitan.util.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateDialog extends Dialog {

	@SuppressWarnings("unused")
	private Context context;
	TextView dateView;

	ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
	ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
	ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
	ArrayList<TextInfo> mHours = new ArrayList<TextInfo>();
	ArrayList<TextInfo> mMinutes = new ArrayList<TextInfo>();

	WheelView mDateWheel = null;
	WheelView mMonthWheel = null;
	WheelView mYearWheel = null;
	WheelView mHourWheel = null;
	WheelView mMinuteWheel = null;

	int mCurDate = 0;
	int mCurMonth = 0;
	int mCurYear = 0;
	int mCurHour = 0;
	int mCurMinute = 0;
	int mSecond = 0;

	private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
		@Override
		public void onEndFling(TosGallery v) {
			int pos = v.getSelectedItemPosition();

			if (v == mDateWheel) {
				TextInfo info = mDates.get(pos);
				setDate(info.mIndex);
			} else if (v == mMonthWheel) {
				TextInfo info = mMonths.get(pos);
				setMonth(info.mIndex);
			} else if (v == mYearWheel) {
				TextInfo info = mYears.get(pos);
				setYear(info.mIndex);
			} else if (v == mHourWheel) {
				TextInfo info = mHours.get(pos);
				setHour(info.mIndex);
			} else if (v == mMinuteWheel) {
				TextInfo info = mMinutes.get(pos);
				setMinute(info.mIndex);
			}

			dateView.setText(formatDate());
		}
	};

	private String formatDate() {
		Date date = new Date();
		mSecond = date.getSeconds();
		return String.format("%d-%02d-%02d %02d:%02d:%02d", mCurYear,
				mCurMonth + 1, mCurDate, mCurHour, mCurMinute, mSecond);
	}

	private void setDate(int date) {
		if (date != mCurDate) {
			mCurDate = date;
		}
	}

	private void setYear(int year) {
		if (year != mCurYear) {
			mCurYear = year;
		}
	}

	private void setMonth(int month) {
		if (month != mCurMonth) {
			mCurMonth = month;

			Calendar calendar = Calendar.getInstance();
			int date = calendar.get(Calendar.DATE);
			prepareDayData(mCurYear, month, date, mCurHour, mCurHour);
		}
	}

	private void setHour(int hour) {
		if (hour != mCurHour) {
			
			mCurHour = hour;
		}
	}

	private void setMinute(int minute) {
		if (minute != mCurMinute) {
			mCurMinute = minute;
		}
	}

	public DateDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DateDialog(Context context, int theme) {
		super(context, theme);
	}

	public DateDialog(Context context, final TextView dateView) {
		super(context, R.style.dialog);
		this.context = context;
		this.dateView = dateView;

		setContentView(R.layout.dialog_date);
		DateDialog.this.setCanceledOnTouchOutside(true);
		mDateWheel = (WheelView) findViewById(R.id.wheel_date);
		mMonthWheel = (WheelView) findViewById(R.id.wheel_month);
		mYearWheel = (WheelView) findViewById(R.id.wheel_year);
		mHourWheel = (WheelView) findViewById(R.id.wheel_hour);
		mMinuteWheel = (WheelView) findViewById(R.id.wheel_minute);

		mDateWheel.setOnEndFlingListener(mListener);
		mMonthWheel.setOnEndFlingListener(mListener);
		mYearWheel.setOnEndFlingListener(mListener);
		mHourWheel.setOnEndFlingListener(mListener);
		mMinuteWheel.setOnEndFlingListener(mListener);

		mDateWheel.setSoundEffectsEnabled(true);
		mMonthWheel.setSoundEffectsEnabled(true);
		mYearWheel.setSoundEffectsEnabled(true);
		mHourWheel.setSoundEffectsEnabled(true);
		mMinuteWheel.setSoundEffectsEnabled(true);

		mDateWheel.setAdapter(new WheelTextAdapter(context));
		mMonthWheel.setAdapter(new WheelTextAdapter(context));
		mYearWheel.setAdapter(new WheelTextAdapter(context));
		mHourWheel.setAdapter(new WheelTextAdapter(context));
		mMinuteWheel.setAdapter(new WheelTextAdapter(context));

		prepareData();

		dateView.setText(formatDate());

		Button btn_sure = (Button) findViewById(R.id.date_sure);

		btn_sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DATE);
				int startYear = 2016;

			/*	mMonthWheel.setSelection(month, true);
				
				mYearWheel.setSelection(year - startYear, true);
				mDateWheel.setSelection(day - 1, true);*/
				DateDialog.this.dismiss();

			}
		});

		Button btn_cancle = (Button) findViewById(R.id.date_cancle);
		btn_cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dateView.setText("点击选择时间");
				DateDialog.this.dismiss();
			}
		});

		// ��ǰʱ��
		// findViewById(R.id.btn_now).setOnClickListener(
		// new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Calendar calendar = Calendar.getInstance();
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// int startYear = 2012;
		//
		// mMonthWheel.setSelection(month, true);
		// mYearWheel.setSelection(year - startYear, true);
		// mDateWheel.setSelection(day - 1, true);
		// }
		// });

	}

	private static final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };

	private static final String[] MONTH_NAME = { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11", "12", };

	private boolean isLeapYear(int year) {
		return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
	}

	private void prepareData() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int startYear = 2012;
		int endYear = 2038;
		int startHour = 00;
		int endHour = 24;
		int startMinute = 00;
		int endMinute = 59;
		mCurDate = day;
		mCurMonth = month;
		mCurYear = year;
		mCurHour = hour;
		mCurMinute = minute;

		for (int i = 0; i < MONTH_NAME.length; ++i) {
			mMonths.add(new TextInfo(i, MONTH_NAME[i], (i == month)));
		}

		for (int i = startYear; i <= endYear; ++i) {
			mYears.add(new TextInfo(i, String.valueOf(i), (i == year)));
		}

		for (int i = startHour; i <= endHour; i++) {
			if (i < 10) {
				String s = "0" + i;
				mHours.add(new TextInfo(i, String.valueOf(s), (i == hour)));
			} else {
				mHours.add(new TextInfo(i, String.valueOf(i), (i == hour)));
			}
		}

		for (int i = startMinute; i <= endMinute; i++) {
			if (i < 10) {
				String s = "0" + i;
				mMinutes.add(new TextInfo(i, String.valueOf(s), (i == minute)));
			} else {
				mMinutes.add(new TextInfo(i, String.valueOf(i), (i == minute)));
			}
		}

		((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
		((WheelTextAdapter) mYearWheel.getAdapter()).setData(mYears);
		((WheelTextAdapter) mHourWheel.getAdapter()).setData(mHours);
		((WheelTextAdapter) mMinuteWheel.getAdapter()).setData(mMinutes);

		prepareDayData(year, month, day, hour, minute);

		mMonthWheel.setSelection(month);
		mYearWheel.setSelection(year - startYear);
		mDateWheel.setSelection(day - 1);
		mHourWheel.setSelection(hour - startHour);
		mMinuteWheel.setSelection(minute - startMinute);
	}

	private void prepareDayData(int year, int month, int curDate, int hour,
			int minute) {
		mDates.clear();

		int days = DAYS_PER_MONTH[month];

		// The February.
		if (1 == month) {
			days = isLeapYear(year) ? 29 : 28;
		}

		for (int i = 1; i <= days; ++i) {
			mDates.add(new TextInfo(i, String.valueOf(i), (i == curDate)));
		}

		((WheelTextAdapter) mDateWheel.getAdapter()).setData(mDates);
	}

	protected class TextInfo {
		public TextInfo(int index, String text, boolean isSelected) {
			mIndex = index;
			mText = text;
			mIsSelected = isSelected;

			if (isSelected) {
				mColor = Color.BLUE;
			}
		}

		public int mIndex;
		public String mText;
		public boolean mIsSelected = false;
		public int mColor = Color.BLACK;
	}

	protected class WheelTextAdapter extends BaseAdapter {
		ArrayList<TextInfo> mData = null;
		int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		int mHeight = 50;
		Context mContext = null;

		public WheelTextAdapter(Context context) {
			mContext = context;
			mHeight = (int) Utils.pixelToDp(context, mHeight);
		}

		public void setData(ArrayList<TextInfo> data) {
			mData = data;
			this.notifyDataSetChanged();
		}

		public void setItemSize(int width, int height) {
			mWidth = width;
			mHeight = (int) Utils.pixelToDp(mContext, height);
		}

		@Override
		public int getCount() {
			return (null != mData) ? mData.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = null;

			if (null == convertView) {
				convertView = new TextView(mContext);
				convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth,
						mHeight));
				textView = (TextView) convertView;
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				textView.setTextColor(Color.BLACK);
			}

			if (null == textView) {
				textView = (TextView) convertView;
			}

			TextInfo info = mData.get(position);
			textView.setText(info.mText);
			textView.setTextColor(info.mColor);

			return convertView;
		}
	}
}
