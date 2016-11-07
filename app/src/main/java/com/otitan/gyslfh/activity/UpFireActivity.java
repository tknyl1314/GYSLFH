package com.otitan.gyslfh.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.king.photo.util.Res;
import com.otitan.entity.DateDialog;
import com.otitan.entity.Image;
import com.otitan.gyslfh.R;
import com.otitan.util.DateTool;
import com.otitan.util.PadUtil;
import com.otitan.util.PictureUtil;
import com.otitan.util.ToastUtil;
import com.otitan.util.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpFireActivity extends Activity {

	private EditText city_text, town_text, viliage_text, huozai_address,
			longitude, latitude, remark;// endTime,
	ImageButton imgBtn_upSure;
	ImageButton returnBtn;
	TextView endTime, startTime;
	String FIREStartTime;
	 String REMARK,REALNAME,TELNO,CITY,TOWN,VILLAGE,PLACE,X,Y,FireType;
	 String  upImageResult="0" ;
	double lon ,lat;
 
	private Spinner county_textSpinner, fireStateSpinner,firetype;
	private ArrayAdapter<String> countryAdapter, fireStateSpinnerAdapter,firetypeSpinnerAdapter;
	private boolean addFireResult = false;
	// private boolean upImageResult = false;
	private ArrayList<Image> upLoads = new ArrayList<Image>();
	private ArrayList<String> selectedDataList=new ArrayList<String>();
	private HorizontalScrollView scrollview;
	private LinearLayout selectedImageLayout;
	private String userID, countryValue, DQLEVEL, UNITID,address,devisionID;
	private int fireStateValue;
	private double longitudeValue, latitudeValue;
	private WebServiceUtil websUtil;
	private SharedPreferences sharedPreferences;
	//photo
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
/*	private static final int TAKE_PICTURE = 0x000001;
	private static final int PHOTO_PICTURE = 0x000002;*/
	private static final int TAKE_PHOTO = 0x000001;
	private static final int ALBUM = 0x000002;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//PublicWay.activityList.add(this);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (PadUtil.isPad(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		parentView = getLayoutInflater().inflate(R.layout.activity_up_fire,null);
		setContentView(parentView);
		Res.init(this);
		websUtil = new WebServiceUtil(this);
		sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
		Intent intent = getIntent();
		userID = intent.getStringExtra("userID");
		longitudeValue = intent.getDoubleExtra("longitude", longitudeValue);
		latitudeValue = intent.getDoubleExtra("latitude", latitudeValue);
		DQLEVEL = intent.getStringExtra("DQLEVEL");//用户等级  区县用户2
		UNITID = intent.getStringExtra("UNITID");
		//address = intent.getStringExtra("address");
		address="";
		REALNAME=sharedPreferences.getString("REALNAME", "");
		TELNO=sharedPreferences.getString("TELNO", "");
		websUtil.initWebserviceTry();

		city_text = (EditText) findViewById(R.id.city_text);// 市
		
		town_text = (EditText) findViewById(R.id.town_text);// 镇
		town_text.setCursorVisible(true);
		viliage_text = (EditText) findViewById(R.id.viliage_text);
		viliage_text.setCursorVisible(true);
		huozai_address = (EditText) findViewById(R.id.huozai_address);
		huozai_address.setText(address);
		startTime = (TextView) findViewById(R.id.startTime);
		startTime.setOnClickListener(new MyListener());
		DateTool dateTool = new DateTool();
		startTime.setText(dateTool.getDateStr());
		endTime = (TextView) findViewById(R.id.endTime);
		endTime.setOnClickListener(new MyListener());
		longitude = (EditText) findViewById(R.id.longitude);
		//isUpsj = (CheckBox) findViewById(R.id.isUpSJ);
		

		DecimalFormat df = new DecimalFormat(".000000");
		longitude.setText(df.format(longitudeValue) + "");
		latitude = (EditText) findViewById(R.id.latitude);
		latitude.setText(df.format(latitudeValue) + "");
		remark = (EditText) findViewById(R.id.remark);
		
		firetype = (Spinner) findViewById(R.id.firetype);// 火灾类型
		final String[] firetypes = getResources().getStringArray(R.array.firetype);
		firetypeSpinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, firetypes);
		firetypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		firetype.setAdapter(firetypeSpinnerAdapter);
		firetype.setSelection(0);
		firetype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
				TextView tv=(TextView)view;
				tv.setTextColor(getResources().getColor(R.color.balck));
				/*if (position == 0) {
					fireStateValue = 1;
				} else {
					fireStateValue = position - 1;
				}*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	

		fireStateSpinner = (Spinner) findViewById(R.id.fireState);
		final String[] fireStates = getResources().getStringArray(R.array.currentqingkuang);
		fireStateSpinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fireStates);
		fireStateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fireStateSpinner.setAdapter(fireStateSpinnerAdapter);
		//fireStateSpinner.setSelection(0);
		fireStateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						TextView tv=(TextView)view;
						tv.setTextColor(getResources().getColor(R.color.balck));
						if (position == 0) {
							fireStateValue = 1;
						} else {
							fireStateValue = position - 1;
						}
//						TextView tv = (TextView) view;
//						tv.setTextColor(color.balck);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		fireStateSpinner.setSelection(2);
		county_textSpinner = (Spinner) findViewById(R.id.county_text);
		final String[] countries = getResources().getStringArray(R.array.counties);
		//countryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
		countryAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item1, countries);
		countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		county_textSpinner.setAdapter(countryAdapter);
		if ("2".equals(DQLEVEL)) {
			//county_textSpinner.setClickable(false);
			county_textSpinner.setEnabled(false);
			county_textSpinner.setBackground(new ColorDrawable(getResources().getColor(R.color.pedestrian)));
			//county_textSpinner.setBackground(getResources().getDrawable().getDrawable(R.drawable.selectbg));
			//county_textSpinner.setBackgroundDrawable(getResources().getColor(R.color.balck));
			county_textSpinner.setSelection(Integer.parseInt(UNITID));
			countryValue = countries[Integer.parseInt(UNITID)].toString();
			devisionID = UNITID;
		} else {
			county_textSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							TextView tv=(TextView)view;
							tv.setTextColor(getResources().getColor(R.color.balck));
							//tv.setBackgroundColor(R.color.gray);
							if (position == 0) {
								countryValue = "";
							} else {
								countryValue = countries[position].toString();
								devisionID = position+"";
							}
//							TextView tv = (TextView) view;
//							tv.setTextColor(color.balck);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
		}
	

		imgBtn_upSure = (ImageButton) findViewById(R.id.fireinfo_imgBtn_upSure);
		returnBtn = (ImageButton) findViewById(R.id.fireinfo_returnBtn);

		imgBtn_upSure.setOnClickListener(new MyListener());
		returnBtn.setOnClickListener(new MyListener());


		//selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layoutView);
		scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);

		// initSelectImage();
		// 初始化照片上传模块
		InitTakePhoto();
	}
	
	
	
	public void InitTakePhoto() {
		/*selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layoutView);
		scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);*/
		pop = new PopupWindow(UpFireActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 拍照
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				takePhoto();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 从相册选择
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UpFireActivity.this,AlbumActivity.class);
				//startActivity(intent);
				startActivityForResult(intent, ALBUM);
				overridePendingTransition(R.anim.activity_translate_in,R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(UpFireActivity.this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(UpFireActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(UpFireActivity.this,GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}
	
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == PublicWay.num) {
				return PublicWay.num;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,parent, false);
				LayoutParams layoutParams = convertView.getLayoutParams();
				layoutParams.width = MapActivity.mScreenW/5;
				layoutParams.height = layoutParams.width;
				convertView.setLayoutParams(layoutParams);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == PublicWay.num) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// Progress.dismiss();
			Bimp.tempSelectBitmap.clear();
			if (msg.what == 1) {
				String str = (String) msg.obj;
				Toast.makeText(UpFireActivity.this, str, Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {

				if (msg.obj != null) {
					boolean isInsertSuccess = Boolean.parseBoolean(msg.obj.toString());

					if (isInsertSuccess) {
						Toast.makeText(UpFireActivity.this, "照片上传成功！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(UpFireActivity.this, "网络错误，照片上传失败!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(UpFireActivity.this, "网络错误，照片上传失败!",
							Toast.LENGTH_SHORT).show();
				}
			} else if (msg.what == 3) {
				Toast.makeText(getApplicationContext(), "火情登记成功！",
						Toast.LENGTH_SHORT).show();
				UpFireActivity.this.finish();
			} else if (msg.what == 4) {
				String str = (String) msg.obj;
				Toast.makeText(UpFireActivity.this, str, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	// ��ť����¼�
	public class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.startTime:
				final Dialog startDateDialog = new DateDialog(
						UpFireActivity.this, startTime);
				startDateDialog.show();
				break;

			case R.id.endTime:
				Dialog endDateDialog = new DateDialog(UpFireActivity.this,
						endTime);
				endDateDialog.show();

				break;

			// 提交
			case R.id.fireinfo_imgBtn_upSure:
				 CITY = city_text.getText().toString();
				 TOWN = town_text.getText().toString();
				 VILLAGE = viliage_text.getText().toString();
				 PLACE = huozai_address.getText().toString();
				 FIREStartTime = startTime.getText().toString();
				String FIREEND = endTime.getText().toString();
			     REMARK = remark.getText().toString();
				 X = longitude.getText().toString();
				 Y = latitude.getText().toString();
				 FireType=firetype.getSelectedItem().toString();
				double lon = Double.parseDouble(X);
				double lat = Double.parseDouble(Y);
				//final String isUp = isUpsj.isChecked()+"";
				if(countryValue.equals("")){
					Toast.makeText(getApplicationContext(), "区县不能为空",
							Toast.LENGTH_SHORT).show();
				}else if(TOWN.equals("")){
					Toast.makeText(getApplicationContext(), "所在镇不能为空",
							Toast.LENGTH_SHORT).show();
				}else if(VILLAGE.equals("")){
					Toast.makeText(getApplicationContext(), "所在村不能为空",
							Toast.LENGTH_SHORT).show();
				}else if (X.equals(".000000") || X.equals("")) {
					Toast.makeText(getApplicationContext(), "经度不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (lon > 180 || lon < -180) {
					Toast.makeText(getApplicationContext(),
							"经度值范围：-180度  ~ 180度", Toast.LENGTH_SHORT).show();
				} else if (Y.equals(".000000") || Y.equals("")) {
					Toast.makeText(getApplicationContext(), "纬度不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (lat > 90 || lat < -90) {
					Toast.makeText(getApplicationContext(),
							"纬度值范围：-90度  ~ 90度", Toast.LENGTH_SHORT).show();
				} else if (PLACE.equals("")) {
					Toast.makeText(getApplicationContext(), "地点不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (FIREStartTime.equals("")
						|| FIREStartTime.equals("点击选择时间")) {
					Toast.makeText(getApplicationContext(), "开始时间不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (FireType.equals("--请选择--")) {
					Toast.makeText(getApplicationContext(), "请选择火灾类型",
							Toast.LENGTH_SHORT).show();
				} else {
					final String result = websUtil
							.selMobileInfo(MyApplication.SBH);// 获取设备
					if (result.equals("网络异常")) {
						ToastUtil
								.setToast(UpFireActivity.this, "网络异常,设备信息获取失败");
						return;
					} else {
						try {
							JSONObject obj = new JSONObject(result);
							JSONArray arr = obj.optJSONArray("ds");
							if (arr != null) {
								JSONObject object = arr.optJSONObject(0);
								REALNAME = object.getString("SYZNAME");
								TELNO = object.getString("SYZPHONE");
							/*	str[2] = object.getString("DZ");
								str[3] = object.getString("SBMC");
								str[4] = object.getString("DJTIME");*/
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					sendRequset();// 数据上传

				}
				break;
			// 返回按钮
			case R.id.fireinfo_returnBtn:
				selectedDataList.clear();
				UpFireActivity.this.finish();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 函数名称 : sendRequset 功能描述 : 参数及返回值说明：
	 * 描述 ：发送上传请求
	 */
	private void sendRequset() {
		// downLoadTip();
		new Thread() {
			@Override
			public void run() {
				super.run();
				Message msg = new Message();
				if (selectedDataList != null && selectedDataList.size() > 0) {
					upLoads.clear();
					for (int i = 0; i < selectedDataList.size(); i++) {
						try {
							Image image = new Image();
							//image.setPath(Base64.encodeFromFile(selectedDataList.get(i).toString()));
							image.setPath(PictureUtil.bitmapToString(selectedDataList.get(i).toString()));
							upLoads.add(image);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					if (upLoads.size() > 0) {
						//Gson gson = new GsonBuilder().serializeNulls().create();
						Gson gson = new Gson();
						String send = gson.toJson(upLoads);
						//upImageResult = websUtil.uploadPhotoByService(send);

						//upImageResult = websUtil.uploadPhotoByService(send,userID, X,  Y, REMARK, FIREStartTime, "0");
						
				
						upImageResult = websUtil.uploadPhotoByService1(send);
						if(upImageResult.equals("0")){
							// Toast.makeText(getApplicationContext(),
							// "图片上传失败",Toast.LENGTH_SHORT).show();
							msg.what = 4;
							msg.obj = "图片上传失败！";
							handler.sendMessage(msg);
						}else{
							addFireResult=websUtil.updateAlarmFireInfor(CITY,countryValue, TOWN, VILLAGE,PLACE,REALNAME,TELNO,userID,X,Y,devisionID,FireType,fireStateValue + "",REMARK,upImageResult);
							if (addFireResult) {
								
								msg.what = 3;
								msg.obj = addFireResult;
								handler.sendMessage(msg);
								selectedDataList.clear();
							} else {
								msg.what = 4;
								msg.obj = "网络错误，请稍候再试！";
								handler.sendMessage(msg);
							}
						}
						
						// upImageResult = websUtil.uploadPhotoByService(send,
						// "经度", "纬度", remark, startTime,1);

					}
				/*	Message msg = new Message();
					msg.what = 2;
					msg.obj = upImageResult;
					handler.sendMessage(msg);
					selectedDataList.clear();*/
				} else {
					msg.what = 1;
					msg.obj = "未选择图片";
					handler.sendMessage(msg);
					selectedDataList.clear();
					return;
				}
			}
		}.start();
	}



	public void removeImage() {
		if (selectedDataList == null)
			return;
		selectedImageLayout.removeAllViews();

	}
    @Override
    protected void onStop() {
    	//
    	super.onStop();
    }
    
	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}
	
	/**
	 * ���������
	 */
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PHOTO);
	}
	
	/**
	 * 拍照
	 */
	private String mCurrentPhotoPath;// 图片路径
	private void takePhoto() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// 指定存放拍摄照片的位置
			File f = createImageFile();
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(takePictureIntent, TAKE_PHOTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File createImageFile() throws IOException {
		String fileName = FileUtils.SDPATH + String.valueOf(System.currentTimeMillis())+".jpg";
		File image = new File(fileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}
	
	protected void onActivityResult(int requestCode, final int resultCode, final Intent intent) {
		switch (requestCode) {
		case ALBUM:// 相册选择
			if (Bimp.tempSelectBitmap.size() < PublicWay.num && resultCode == RESULT_OK) {
				for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
					String path = Bimp.tempSelectBitmap.get(i).getImagePath();
					//Bitmap bm = PictureUtil.getSmallBitmap(path);
					selectedDataList.add(path);
				}
				adapter.notifyDataSetChanged();
			}
			break;
			
		case TAKE_PHOTO:// 拍照
			if (resultCode == RESULT_OK) {
				Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);
				dealPhotoFile(mCurrentPhotoPath);
				selectedDataList.add(mCurrentPhotoPath);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
				adapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
	}
	
	private void dealPhotoFile(final String file) {
		PhotoTask task = new PhotoTask(file);
		task.start();
	}
	
	private class PhotoTask extends Thread {
		private String file;

		private boolean isFinished;

		public PhotoTask(String file) {
			this.file = file;
		}

		@Override
		public void run() {
			BufferedOutputStream bos = null;
			Bitmap icon = null;
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(file, options); // 此时返回bm为空
				float percent = options.outHeight > options.outWidth ? options.outHeight / 960f
						: options.outWidth / 960f;

				if (percent < 1) {
					percent = 1;
				}
				int width = (int) (options.outWidth / percent);
				int height = (int) (options.outHeight / percent);
				icon = Bitmap
						.createBitmap(width, height, Bitmap.Config.RGB_565);

				// 初始化画布 绘制的图像到icon上
				Canvas canvas = new Canvas(icon);
				// 建立画笔
				Paint photoPaint = new Paint();
				// 获取跟清晰的图像采样
				photoPaint.setDither(true);
				// 过滤一些
				// photoPaint.setFilterBitmap(true);
				options.inJustDecodeBounds = false;

				Bitmap prePhoto = BitmapFactory.decodeFile(file);
				if (percent > 1) {
					prePhoto = Bitmap.createScaledBitmap(prePhoto, width,
							height, true);
				}

				canvas.drawBitmap(prePhoto, 0, 0, photoPaint);

				if (prePhoto != null && !prePhoto.isRecycled()) {
					prePhoto.recycle();
					prePhoto = null;
					System.gc();
				}

				// 设置画笔
				Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
						| Paint.DEV_KERN_TEXT_FLAG);
				// 字体大小
				textPaint.setTextSize(20.0f);
				// 采用默认的宽度
				textPaint.setTypeface(Typeface.DEFAULT);
				// 采用的颜色
				textPaint.setColor(Color.YELLOW);
				// 阴影设置
				// textPaint.setShadowLayer(3f, 1, 1, Color.DKGRAY);

				// 时间水印
				String mark = getCurrTime("yyyy-MM-dd HH:mm:ss");
				float textWidth = textPaint.measureText(mark);
				canvas.drawText(mark, width - textWidth - 10, height - 26,textPaint);

				bos = new BufferedOutputStream(new FileOutputStream(file));

				int quaility = (int) (100 / percent > 80 ? 80 : 100 / percent);
				icon.compress(CompressFormat.JPEG, quaility, bos);
				bos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isFinished = true;
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (icon != null && !icon.isRecycled()) {
					icon.recycle();
					icon = null;
					System.gc();
				}
			}
		}
	}
	@SuppressLint("SimpleDateFormat")
	private static String getCurrTime(String pattern) {
		if (pattern == null) {
			pattern = "yyyyMMddHHmmss";
		}
		return (new SimpleDateFormat(pattern)).format(new Date());
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*for (int i = 0; i < PublicWay.activityList.size(); i++) {
			if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);*/
			Bimp.tempSelectBitmap.clear();
			UpFireActivity.this.finish();
		}
		return true;
	}
	
}
