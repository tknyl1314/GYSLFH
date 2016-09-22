package com.otitan.gyslfh.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
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
import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.otitan.entity.DateDialog;
import com.otitan.entity.Image;
import com.otitan.gyslfh.R;
import com.otitan.util.DateTool;
import com.otitan.util.PadUtil;
import com.otitan.util.PictureUtil;
import com.otitan.util.WebServiceUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HuiJingActivity extends Activity {

	private ArrayAdapter<String> iSureAdapter, leixingAdapter, currentAdapter;
	private String isFire, fireType, fireStuation;
	private String userid, RECEIPTID, backId;
	private WebServiceUtil websUtil;
	private ImageButton huijing_returnBtn, huijing_upSure;
	EditText suoshuquchujing_text, heshiren_text, beizhu_text;
	Spinner huozaileixing_spinner, current_spinner, issure_spinner;
	TextView huijing_time_text,BELONGNAME;
	private String currentTime,realname,telno,huijing_timeStr,remark,policesiuation,checker,belongname;
	private String ADMINDIVISION = null, dqname = null, backState = null;
	private Button selectPictures;
	String [] iSureDate;
	private ArrayList<String> dataList = new ArrayList<String>();
	private ArrayList<String> selectedDataList=new ArrayList<String>();
	private LinearLayout selectedImageLayout;
	private HorizontalScrollView scrollview;
	private ArrayList<Image> upLoads = new ArrayList<Image>();
	 Intent intent=getIntent();
	//take photo
		private GridView noScrollgridview;
		private GridAdapter adapter;
		private View parentView;
		private PopupWindow pop = null;
		private LinearLayout ll_popup;
		private static final int TAKE_PHOTO = 0x000001;
		private static final int ALBUM = 0x000002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PadUtil padUtil = new PadUtil();
		if (padUtil.isPad(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		parentView = getLayoutInflater().inflate(R.layout.activity_hui_jing,null);
		setContentView(parentView);
 
		websUtil = new WebServiceUtil(this);
		websUtil.initWebserviceTry();

		final Intent intent = getIntent();
		userid = intent.getStringExtra("USERID");
		RECEIPTID = intent.getStringExtra("RECEIPTID");
		backId = intent.getStringExtra("ID");
		ADMINDIVISION = intent.getStringExtra("ADMINDIVISION");
		dqname = intent.getStringExtra("dqname");
		backState = intent.getStringExtra("backState");
		realname = intent.getStringExtra("REALNAME");
		telno = intent.getStringExtra("TELNO");

		huijing_upSure = (ImageButton) findViewById(R.id.huijing_upSure);
		huijing_returnBtn = (ImageButton) findViewById(R.id.huijing_returnBtn);
		huijing_returnBtn.setOnClickListener(new MyListener());
		huijing_upSure.setOnClickListener(new MyListener());
		// 时间
		huijing_time_text = (TextView) findViewById(R.id.huijing_time_text);
		huijing_time_text.setOnClickListener(new MyListener());
		//所属地区
		BELONGNAME=(TextView) findViewById(R.id.BELONGNAME_text);
		//区县出警情况
		suoshuquchujing_text = (EditText) findViewById(R.id.suoshuquchujing_text);
		//备注
		beizhu_text = (EditText) findViewById(R.id.beizhu_text);
		//核实人
		heshiren_text = (EditText) findViewById(R.id.heshiren_text);
		heshiren_text.setText(realname);
		heshiren_text.setEnabled(false);
		
		DateTool dateTool = new DateTool();
		huijing_time_text.setText(dateTool.getDateStr());
		/*selectPictures = (Button) findViewById(R.id.selectPicture);
		selectPictures.setOnClickListener(new MyListener());*/

		//selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layoutView);
		scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
		// 当前状况
		current_spinner = (Spinner) findViewById(R.id.current_spinner);
		final String[] currentDate = getResources().getStringArray(R.array.currentqingkuang);
		currentAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, currentDate);
		currentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		current_spinner.setAdapter(currentAdapter);
		current_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv=(TextView)view;
				tv.setTextColor(getResources().getColor(R.color.balck));
				fireStuation = currentDate[position].toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// 火灾类型
		huozaileixing_spinner = (Spinner) findViewById(R.id.huozaileixing_spinner);
		final String[] leixing = getResources().getStringArray(R.array.huozaileixing);
		leixingAdapter = new ArrayAdapter<String>(HuiJingActivity.this,android.R.layout.simple_spinner_item, leixing);
		leixingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		huozaileixing_spinner.setAdapter(leixingAdapter);
		huozaileixing_spinner.setSelection(0);
		huozaileixing_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
				TextView tv=(TextView)view;
				tv.setTextColor(getResources().getColor(R.color.balck));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// 是否火情
		issure_spinner = (Spinner) findViewById(R.id.issure_spinner);
		iSureDate = getResources().getStringArray(R.array.issure);
		iSureAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, iSureDate);
		iSureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		issure_spinner.setSelection(0);
		issure_spinner.setAdapter(iSureAdapter);
	/*	if (backState.equals("2")) {
			//0未查看，1查看未回，2已回复
			issure_spinner.setSelection(3);
		}*/
		issure_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					final int position, long id) {
				
				TextView tv=(TextView)view;
				tv.setTextColor(getResources().getColor(R.color.balck));
				isFire = iSureDate[position].toString();
				if(position == 0){
					isFire="";
				}
				if (position == 1) {//是
					current_spinner.setClickable(true);
					HuiJingActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							//huozaileixing_spinner.setClickable(false);
							final String[] leixing = getResources().getStringArray(R.array.senlinhuozai);
							leixingAdapter = new ArrayAdapter<String>(HuiJingActivity.this,android.R.layout.simple_spinner_item, leixing);
							leixingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							huozaileixing_spinner.setAdapter(leixingAdapter);
							huozaileixing_spinner.setSelection(1);
							huozaileixing_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
									TextView tv=(TextView)view;
									tv.setTextColor(getResources().getColor(R.color.balck));
								}

								@Override
								public void onNothingSelected(AdapterView<?> arg0) {
									// TODO Auto-generated method stub
									
								}
							});
							fireType = leixing[1].toString();
							current_spinner.setSelection(2);
							fireStuation = currentDate[1].toString();
						}
					});
				} else if (position == 2) {//不是
					current_spinner.setClickable(true);
					huozaileixing_spinner.setClickable(true);
					HuiJingActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							final String[] leixing = getResources().getStringArray(R.array.huozaileixing);
							leixingAdapter = new ArrayAdapter<String>(HuiJingActivity.this,android.R.layout.simple_spinner_item, leixing);
							leixingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							huozaileixing_spinner.setAdapter(leixingAdapter);
							huozaileixing_spinner.setSelection(1);
							huozaileixing_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
									TextView tv=(TextView)view;
									tv.setTextColor(getResources().getColor(R.color.balck));
								}

								@Override
								public void onNothingSelected(AdapterView<?> arg0) {
									// TODO Auto-generated method stub
									
								}
							});
							fireType = leixing[1].toString();
							current_spinner.setSelection(2);
							fireStuation = currentDate[1].toString();
						}
					});
				}else if (position == 4){
					BELONGNAME.setEnabled(true);
				}
				/*else {
					//huozaileixing_spinner.setClickable(false);
					//current_spinner.setClickable(false);
					
				
					HuiJingActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							final String[] leixingDate = getResources().getStringArray(R.array.huozaileixing);
							leixingAdapter = new ArrayAdapter<String>(HuiJingActivity.this,android.R.layout.simple_spinner_item,leixingDate);
							leixingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							huozaileixing_spinner.setAdapter(leixingAdapter);
							huozaileixing_spinner.setSelection(0);
							current_spinner.setSelection(0);
							fireStuation = "";
							fireType = "";
//							if (position == 3) {
//								huozaileixing_spinner.setClickable(false);
//								huozaileixing_spinner.setSelection(0);
//								current_spinner.setClickable(false);
//								// current_spinner.setSelection(0);
//								fireType = "";
//								fireStuation = "";
//							} else {
//								huozaileixing_spinner
//										.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//											@Override
//											public void onItemSelected(
//													AdapterView<?> parent,
//													View view, int position,
//													long id) {
//												fireType = leixingDate[position]
//														.toString();
//											}
//
//											@Override
//											public void onNothingSelected(
//													AdapterView<?> arg0) {
//
//											}
//										});
//							}
						}

						@SuppressWarnings("unused")
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
				}
				isFire = iSureDate[position].toString();*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
        
	/*	heshiren_text = (EditText) findViewById(R.id.heshiren_text);
		if (backState.equals("2")) {
			heshiren_text.setText(intent.getStringExtra("CHECKER"));
		}
		suoshuquchujing_text = (EditText) findViewById(R.id.suoshuquchujing_text);
			beizhu_text = (EditText) findViewById(R.id.beizhu_text);
		if (backState.equals("2")) {
			suoshuquchujing_text.setText(intent.getStringExtra("POLICESIUATION"));
		}
		beizhu_text = (EditText) findViewById(R.id.beizhu_text);
		if (backState.equals("2")) {
			beizhu_text.setText(intent.getStringExtra("REMARK"));
		}*/
		
		
		//初始化照片上传模块
		InitTakePhoto();


	}

	private void InitTakePhoto() {
		pop = new PopupWindow(HuiJingActivity.this);

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
		//拍照
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				takePhoto();
				pop.dismiss();
				ll_popup.clearAnimation();
			}

		
		});
		//从相册选择
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HuiJingActivity.this,AlbumActivity_HuiJing.class);
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
		
		noScrollgridview = (GridView) findViewById(R.id.huijing_noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(HuiJingActivity.this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(HuiJingActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(HuiJingActivity.this,GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	public void removeImage() {
		if (selectedDataList == null)
			return;
		selectedImageLayout.removeAllViews();

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

	public class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*case R.id.selectPicture:
				removeImage();
				Bundle bundle = new Bundle();
				bundle.putInt("size", 10);
				bundle.putStringArrayList("dataList", dataList);
				imageIntent.putExtras(bundle);
				startActivityForResult(imageIntent, 0);
				break;*/
			case R.id.huijing_time_text:

				Dialog hjTimeDialog = new DateDialog(HuiJingActivity.this,
						huijing_time_text);
				hjTimeDialog.show();

				break;

			case R.id.huijing_returnBtn:
				selectedDataList.clear();
				adapter.notifyDataSetChanged();
				HuiJingActivity.this.setResult(RESULT_OK);
				HuiJingActivity.this.finish();
				/*if ("0".equals(backState)) {
					websUtil.updateBack(backId);
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
				}
				finish();*/
				break;
			case R.id.huijing_upSure:
				 checker = heshiren_text.getText().toString();//核实人
				 policesiuation = suoshuquchujing_text.getText().toString();
				 remark = beizhu_text.getText().toString();
				 huijing_timeStr = huijing_time_text.getText().toString();
				 belongname =BELONGNAME.getText().toString();//所属地区字段
				if (huijing_timeStr.equals("")) {
					Toast.makeText(getApplicationContext(), "回警时间不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (isFire.equals("")) {
					Toast.makeText(getApplicationContext(), "是否火情不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (checker.equals("")) {
					Toast.makeText(getApplicationContext(), "核实人不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					
					sendRequset();//数据上传
				/*	if (result) {
						Toast.makeText(HuiJingActivity.this, "回警成功！",Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					} else {
						Toast.makeText(HuiJingActivity.this, "网络错误！稍候重试！",
								Toast.LENGTH_SHORT).show();
					}*/
				}

				break;
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * 函数名称 : sendRequset 功能描述 : 参数及返回值说明：
	 * 
	 * 描述 ：发送上传请求
	 * 
	 */
	private void sendRequset() {
		// downLoadTip();
		new Thread() {

			@Override
			public void run() {
				super.run();
				System.out.println("什么时间会执行");
				Message msg = new Message();
				if (selectedDataList != null && selectedDataList.size() > 0) {
					upLoads.clear();
					for (int i = 0; i < selectedDataList.size(); i++) {
						try {
							Image image = new Image();
							// image.setPath(Base64.encodeFromFile(selectedDataList.get(i).toString()));
							image.setPath(PictureUtil.bitmapToString(selectedDataList.get(i).toString()));
							upLoads.add(image);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (upLoads.size() > 0) {
						Gson gson = new Gson();
						String send = gson.toJson(upLoads);
						/*String upImageResult = websUtil.uploadPhotoByhuijing(send);
						if(upImageResult.equals("0")){
							//Toast.makeText(getApplicationContext(), "图片上传失败",Toast.LENGTH_SHORT).show();
							msg.what = 1;
							msg.obj = "图片上传失败！";
							handler.sendMessage(msg);
						}else{*/
							 boolean result = websUtil.addHuiJing(backId, RECEIPTID,userid, huijing_timeStr, isFire, fireType,fireStuation,checker, policesiuation, remark,ADMINDIVISION, dqname,belongname,send);
							 if (result) {
									msg.what = 2;
									msg.obj = result;
									handler.sendMessage(msg);
								} else {
									msg.what = 1;
									msg.obj = "网络错误，请稍候再试！";
									handler.sendMessage(msg);
							}
						
						
					
					}
				
				} else {
					msg.what = 1;
					msg.obj = "未选择图片";
					handler.sendMessage(msg);
				
					return;
				}
			}
		}.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			selectedDataList.clear();
			Bimp.tempSelectBitmap.clear();
			if (msg.what == 1) {
				String str = (String) msg.obj;
				Toast.makeText(HuiJingActivity.this, str, Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {

				if (msg.obj != null) {
					boolean isInsertSuccess = Boolean.parseBoolean(msg.obj.toString());

					if (isInsertSuccess) {
						Toast.makeText(HuiJingActivity.this, "回警成功！",Toast.LENGTH_SHORT).show();
						HuiJingActivity.this.setResult(RESULT_OK, intent);
						HuiJingActivity.this.finish();
					} else {
						Toast.makeText(HuiJingActivity.this, "网络错误，照片上传失败!",Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HuiJingActivity.this, "网络错误，照片上传失败!",Toast.LENGTH_SHORT).show();
				}
			}
		};
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, final int resultCode, final Intent intent) {

	/*	if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				selectedDataList = (ArrayList<String>) bundle
						.getSerializable("dataList");
				if (selectedDataList != null) {
					dataList.clear();
					dataList.addAll(selectedDataList);
					initSelectImage();
				}
			}
		}*/
	/*	if (Bimp.tempSelectBitmap.size() < PublicWay.num && resultCode == RESULT_OK) {
			Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);
			dealPhotoFile(mCurrentPhotoPath);
			selectedDataList.add(mCurrentPhotoPath);
			ImageItem takePhoto = new ImageItem();
			takePhoto.setBitmap(bm);
			Bimp.tempSelectBitmap.add(takePhoto);
			adapter.notifyDataSetChanged();
		}*/
		switch (requestCode) {
		case ALBUM:
			if (Bimp.tempSelectBitmap.size() < PublicWay.num && resultCode == RESULT_OK) {
				for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
					String path = Bimp.tempSelectBitmap.get(i).getImagePath();
					//Bitmap bm = PictureUtil.getSmallBitmap(path);
					selectedDataList.add(path);
				}
				adapter.notifyDataSetChanged();
			}
			break;
			
		case TAKE_PHOTO:
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

	// 监听退出键
/*	@Override
	public void onBackPressed() {
		if ("0".equals(backState)) {
			websUtil.updateBack(backId);
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
		}
		finish();
	}*/
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
		/*	selectedDataList.clear();
			adapter.notifyDataSetChanged();*/
			HuiJingActivity.this.setResult(RESULT_OK, intent);
			HuiJingActivity.this.finish();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
