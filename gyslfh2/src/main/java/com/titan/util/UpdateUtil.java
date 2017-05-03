package com.titan.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.titan.newslfh.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class UpdateUtil {
    static Context mcontext;
	/**不需要更新*/  
	private final int UPDATA_NONEED = 0;//
	/**需要更新*/  
	private final int UPDATA_NEED = 1;
	/**获取服务器信息失败*/  
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	//
	private final int SERVER_ERROR = 6;
	/**下载出错*/
	private final int DOWN_ERROR = 4;
	/**下载完成*/
	private final int DOWN_COMPLETE = 5;
	// 版本信息
	///public UpdataInfo versioninfo;
	private double currentVersionCode=0.0;
    //是否通过点击更新
	private boolean isclick=false;
    private VersionInfo versioninfo;
   // private static String serverurl="http://titanah.imwork.net:8099/GYLYEQ/apk/version.xml";
    //private static String serverurl="http://www.gyslfh.com:8037/apk/AS_version.xml";
   // private static String serverurl="http://223.99.164.236:8012/AndroidServer/APK/AS_version.xml";

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			  if(msg.obj!=null){
			 
			 }else{
			 
			switch (msg.what)
			{
			case UPDATA_NONEED:
				//通过点击按钮更新
				if(isclick){
					Toast.makeText(mcontext, "未发现新版本",
							Toast.LENGTH_SHORT).show();
					isclick=false;
				}
				break;
			case UPDATA_NEED:
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				/*Toast.makeText(mcontext, "获取服务器更新信息失败", 1)
						.show();*/
				//ToastUtil.setToast(mcontext, "获取服务器更新信息失败，请检查网络连接是否正常");
				Toast.makeText(mcontext, "获取服务器更新信息失败，请检查网络连接是否正常", Toast.LENGTH_SHORT).show();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(mcontext, "下载新版本失败", Toast.LENGTH_SHORT).show();
				break;
			case DOWN_COMPLETE:
				Toast.makeText(mcontext, "下载完成", Toast.LENGTH_SHORT).show();
				break;
                case SERVER_ERROR:
                    Toast.makeText(mcontext, "更新时服务器异常请检查后重试", Toast.LENGTH_SHORT).show();
					break;
			}


		}
	}
	};
	
	 public boolean isIsclick() {
			return isclick;
		}
		public void setIsclick(boolean isclick) {
			this.isclick = isclick;
		}
	/**
	 * 是否点击更新
	 */
	public UpdateUtil(Context context) {
		mcontext=context;
		versioninfo=new VersionInfo();
		//isclick=isClick;
	}
	//执行更新
	public void executeUpdate() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					currentVersionCode = getVersionCode();
					checkVersion(mcontext);
				}  catch (Exception e) {
					Message msg = new Message();
					msg.what = GET_UNDATAINFO_ERROR;
					handler.sendMessage(msg);
					//e.printStackTrace();
				}
			
			}
		}).start();
	}
	// 检查版本号是否一致
		
	
	/** 检查软件是否有更新版本 */
	public boolean isUpdate() {
		// 获取当前软件版本
		//new updateAsyncTask().execute("getServerVersion");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						currentVersionCode = getVersionCode();
						checkVersion(mcontext);
					}  catch (Exception e) {
						   Message msg = new Message();
						msg.what = GET_UNDATAINFO_ERROR;
						handler.sendMessage(msg);
						//e.printStackTrace();
					}
				
				}
			}).start();
			
		
		if (null != versioninfo) {
			double serviceCode = Double.valueOf(versioninfo.getVersion());
			// 版本判断
            return serviceCode > currentVersionCode;
		}
		return false;
	}
	
	
	/**
	 * 获取当前软件版本号
	 */
	public static double getVersionCode()  {
		double versionCode = 0;
		String versionName;
		try {
			versionCode = mcontext.getPackageManager().getPackageInfo(
					mcontext.getPackageName(), 0).versionCode;//0代表是获取版本信息
			//versionCode = Double.parseDouble(versionName);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;


	}
	
	/**
	 * 检查版本信息
	 * @throws Exception
	 */
	public  void  checkVersion(Context context
			)throws Exception {
		 // HashMap<String, String> mHashMap = new HashMap<String, String>();
		// 把version.xml放到网络上，然后获取文件信息
		   URL url;
		   Message msg = new Message();
			url = new URL(context.getResources().getString(R.string.updateurl));
			//URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
		//	HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setConnectTimeout(5000);
			httpConnection.setRequestMethod("GET");
			int responseCode = httpConnection.getResponseCode();
			InputStream inStream = null;
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				 parseXml(inStream);
				 
					if (null != versioninfo) {
						double serviceCode = Double.valueOf(versioninfo.getVersion());
						// 版本判断
						if (serviceCode > currentVersionCode) {
							msg.what = UPDATA_NEED;
							handler.sendMessage(msg);
						}else{
							msg.what = UPDATA_NONEED;
							handler.sendMessage(msg);
						}
					}
			}else{
				if(responseCode==404||responseCode==500){
					msg.what = SERVER_ERROR;
				}else {
					msg.what = GET_UNDATAINFO_ERROR;
				}
				handler.sendMessage(msg);
			}
			// InputStream inStream = ParseXmlService.class.getClassLoader()
			// .getResourceAsStream("version.xml");
			// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
			//mHashMap = ParseXmlService.parseXml(inStream);
			
		
		
	}

	/**
	 * 解析服务端版本信息
	 * @param inStream
	 * @return
	 * @throws Exception
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public  VersionInfo parseXml(InputStream inStream)
			throws Exception, ParserConfigurationException, SAXException, IOException {
		//HashMap<String, String> hashMap = new HashMap<String, String>();
	
		//VersionInfo versioninfo=new UpdateUtil().VersionInfo();
		// 实例化一个文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 通过文档构建器工厂获取一个文档构建器
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 通过文档通过文档构建器构建一个文档实例
		Document document = builder.parse(inStream);
		// 获取XML文件根节点
		Element root = document.getDocumentElement();
		// 获得所有子节点
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			// 遍历子节点
			Node childNode = (Node) childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				// 版本号
				if ("version".equals(childElement.getNodeName())) {
					versioninfo.setVersion(childElement.getFirstChild()
							.getNodeValue());
					/*hashMap.put("version", childElement.getFirstChild()
							.getNodeValue());*/
				}
				// 软件名称
				/*else if (("name".equals(childElement.getNodeName()))) {
					versioninfo.set
					hashMap.put("name", childElement.getFirstChild()
							.getNodeValue());
				}*/
				// 下载地址
				else if (("url".equals(childElement.getNodeName()))) {
					versioninfo.setUrl(childElement.getFirstChild()
							.getNodeValue());
					//hashMap.put("url", childElement.getFirstChild().getNodeValue());
				}
				// 更新信息
				else if (("info".equals(childElement.getNodeName()))) {
					versioninfo.setDescription(childElement.getFirstChild()
							.getNodeValue());
					//hashMap.put("url", childElement.getFirstChild().getNodeValue());
				}
			}
		}
		return versioninfo;
	}
	 /**
	 * 弹出对话框通知用户更新程序
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog()
	{
		Builder builer = new Builder(mcontext);
		builer.setTitle("发现新版本");
		if(versioninfo.getDescription()!=null){
			
			builer.setMessage(versioninfo.getDescription());
		}
		
		// 当点确定按钮时从服务器上下载 新的apk 然后安装 װ
		builer.setPositiveButton("确定更新", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				// Log.i(TAG, "下载apk,更新");
				downLoadApk();
			}
		});
		builer.setNegativeButton("以后再说", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	
	 // 从服务器中下载APK
	 
	private void downLoadApk()
	{
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(mcontext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread()
		{
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					//String kdjString = versioninfo.getUrl();
					//下载apk到本地文件夹
					File file= DownLoadManager.getFileFromServer(versioninfo.getUrl(), pd);
					
					/*try{
						
						 sleep(3000);
					}catch (IOException e) {
						ToastUtil.setToast(mcontext, "下载apk到本地发生异常："+e.toString());
					}
					
					currentThread().sleep(time)
					installApk(file);*/
					if(file!=null){
						
						msg.what = DOWN_COMPLETE;
						handler.sendMessage(msg);
						installApk(file);
					}else{
						msg.what = DOWN_ERROR;
						handler.sendMessage(msg);
					}
					pd.dismiss(); // 结束掉进度条对话框
					
				} catch (Exception e)
				{
					
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 安装apk
	protected void installApk(File file)
	{
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		mcontext.startActivity(intent);
	}

	
	public static class DownLoadManager {
		public static File getFileFromServer(String path, ProgressDialog pd) throws Exception, IOException{
			//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				//获取到文件的大小 
				pd.setMax(conn.getContentLength());
				InputStream is = conn.getInputStream();
				File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len ;
				int total=0;
				while((len =bis.read(buffer))!=-1){
					fos.write(buffer, 0, len);
					total+= len;
					//获取当前下载量
					pd.setProgress(total);
				}
				fos.close();
				bis.close();
				is.close();
				return file;
			}
			else{
				return null;
			}
		}
	}
	public class VersionInfo {
		public  VersionInfo() {
			
		}
		/**版本号*/
		private String version;
		/**服务器更新地址*/
		private String url;
		/**更新信息*/
		private String description;
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
	

}

	
	
	




