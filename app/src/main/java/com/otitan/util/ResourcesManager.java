package com.otitan.util;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.otitan.gyslfh.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResourcesManager implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String PATH_MAPS = "/maps";
	public static String Code_Path = "";
	public static Context context;
	public static String packageName;
	public static String appname;
	public static String image = "/image";
	public static String otms = "/otms";
	public static String shape = "/shape";
	public static String otitan_map = "/otitan.map";
	public static String excel = "/excel";
	public static String sqlite = "/sqlite";
	public static String navi = "/navi";
	public static String picture = "/picture";

	public static String otitan = "/Otitan";
	public static String ldlj = "/林地落界";
	public static String ed = "/二调";
	public static String gyl = "/公益林";
	public static String lq = "/林权";
	public static ResourcesManager resourcesManager;

	public synchronized static ResourcesManager getInstance(Context context)
			throws Exception
	{
		if (resourcesManager == null)
		{
			resourcesManager = new ResourcesManager(context);
		}
		packageName = context.getPackageName();
		appname = context.getResources().getString(R.string.app_name);
		Code_Path = PATH_MAPS + packageName + "/maps";
		return resourcesManager;
	}

	/**
	 * Reset the {@link ResourcesManager}.
	 */
	public static void resetManager()
	{
		resourcesManager = null;
	}

	private ResourcesManager(Context context) throws Exception
	{
		this.context = context;
	}

	/**
	 * 获取内存地址
	 * 
	 * @return
	 */
	public static String[] getPath()
	{

		StorageManager sm = (StorageManager) context
				.getSystemService(Context.STORAGE_SERVICE);
		String[] paths = null;
		try
		{

			/*paths = (String[]) sm.getClass().getMethod("getVolumePaths", new Class<Object>)
					.invoke(sm, new Object[0]);*/
			paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);

		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return paths;
	}
	
	
	

	/** 取文件可用地址 */
	public static String getFilePath(String path)
	{
		String dataPath = "文件可用地址";
		String[] memoryPath = getPath();
		for (int i = 0; i < memoryPath.length; i++)
		{
			File file = new File(memoryPath[i] + PATH_MAPS + path);
			if (file.exists() && file.isFile())
			{
				dataPath = memoryPath[i] + PATH_MAPS + path;
				break;
			}
		}
		return dataPath;
	}

	/** 获取文件夹可用地址 */
	public static String getFolderPath(String path) {
		String dataPath = "文件夹可用地址";
		String[] memoryPath = getPath();
		for (int i = 0; i < memoryPath.length; i++)
		{
			File file = new File(memoryPath[i] + PATH_MAPS + path);
			if (file.exists()) {
				dataPath = memoryPath[i] + PATH_MAPS + path;
				break;
			}
		}
		return dataPath;
	}

	/**
	 * 创建系统使用的文件夹
	 */
	public void createFolder()
	{
		/*
		 * if (hasSDcard()) { createFolder(getPath()[1] + PATH_MAPS);
		 * createFolder(getPath()[1] + PATH_MAPS + excel);
		 * createFolder(getPath()[1] + PATH_MAPS + otitan_map);
		 * createFolder(getPath()[1] + PATH_MAPS + otms);
		 * createFolder(getPath()[1] + PATH_MAPS + sqlite);
		 * createFolder(getPath()[1] + PATH_MAPS + image);
		 * createFolder(getPath()[1] + PATH_MAPS + navi); }
		 */
		createFolder(getPath()[0] + PATH_MAPS + sqlite);//本地数据库
	    createFolder(getPath()[0] + PATH_MAPS + otitan_map);//
		createFolder(getPath()[0] + PATH_MAPS + navi);
		createFolder(getPath()[0] + PATH_MAPS + otms);//图层数据
		//createFolder(getDataPath() + PATH_MAPS + sqlite);
		createFolder(getPath()[0] + PATH_MAPS + picture);//
		createFolder(getPath()[0] + PATH_MAPS + navi + "/" + appname);//存储导航数据
	}

	/**
	 * 判断是否存sd卡
	 * 
	 * @return
	 */
	public boolean hasSDcard()
	{
		String path = this.getPath()[1];
		String fileDir = path + "/OtitanTest";
		File f1 = new File(fileDir);
		// boolean dd=f1.mkdir();
		if (f1.mkdir())
		{
			boolean flag = false;
			if (f1.exists())
			{
				deleteFile(f1);
				flag = true;
			}
			// deleteDirectory(fileDir);
			// SD������
			return flag;
		} else
		{
			deleteFile(f1);
			return false;
		}
	}

	/**
	 */
	public static void createFolder(String path)
	{
		File file = new File(path);
		if (!file.exists())
		{
			boolean ismkd=file.mkdirs();// �������� �Զ������ϼ�Ŀ¼
		}
	}

	/**
	 *
	 */
	public void deleteFile(File file)
	{

		if (file.exists())
 { // �ж��ļ��Ƿ����
			if (file.isFile())
 { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������˼;
			} else if (file.isDirectory())
 { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++)
 { // ����Ŀ¼�����е��ļ�
					this.deleteFile(files[i]); // ��ÿ���ļ� ������������е���
				}
			}
			file.delete();
		}
	}

	/**
	 * 优先获取本地存储文件
	 * @return
	 */
	public static String getDataPath(String path)
	{
		String dataPath = "";
		for (int i = 0; i < getPath().length; i++)
		{
			File file = new File(getPath()[i] + PATH_MAPS + path);
			if (file.exists())
			{
				dataPath = getPath()[i] + PATH_MAPS + path;
				break;
			}
		}
		return dataPath;
	}
	/**
	 * 获取数据存储路径（优先使用外存）
	 * @return
	 */
	/*public static String getDataPath()
	{
		String dataPath = "";
		int t=getPath().length;
		for (int i=getPath().length-1 ; i >=0; i--)
		{
			File file = new File(getPath()[i] + "/test" );
		
			if (file.mkdir())
			{
				dataPath = getPath()[i];
				break;
			}
		}
		return dataPath;
	}*/

	public String getXBDataPath(String path)
	{
		String dataPath = "";
		for (int i = 0; i < getPath().length; i++)
		{
			File file = new File(getPath()[i] + PATH_MAPS + path);
			if (file.exists())
			{
				dataPath = getPath()[i] + PATH_MAPS + path;
				File[] files = new File(dataPath).listFiles();
				if ((files != null) && (files.length > 0))
				{
					return dataPath;
				}
			}
		}
		return dataPath;
	}

	public String getExcelPath()
	{
		String excelPath = "";
		excelPath = getDataPath(excel);
		return excelPath;
	}
	// 获取本地合肥市图层
	public String getArcGISLocalTiledHFLayerPath()
	{
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/title.tpk";
		arcGISLocalTiledLayerPath = getDataPath(str);
		return arcGISLocalTiledLayerPath;
	}
	// 获取本地贵阳市图层
	public String getArcGISLocalTiledLayerPath()
	{
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/title.tpk";
		arcGISLocalTiledLayerPath = getDataPath(str);
		return arcGISLocalTiledLayerPath;
	}
	// 获取本地安顺市图层
		public String getArcGISLocalTiledASLayerPath()
		{
			String arcGISLocalTiledLayerPath = "";
			String str = otitan_map + "/anshun.tpk";
			arcGISLocalTiledLayerPath = getDataPath(str);
			return arcGISLocalTiledLayerPath;
		}

	// 获取本地全国市界图层
	public String getArcGISLocalCityTiledLayerPath()
	{
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/City.tpk";
		arcGISLocalTiledLayerPath = getDataPath(str);
		return arcGISLocalTiledLayerPath;
	}

	public String getArcGISLocalImageLayerPath()
	{
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/image.tpk";
		arcGISLocalTiledLayerPath = getDataPath(str);
		return arcGISLocalTiledLayerPath;
	}

	public String getArcGISLocalyyLayerPath()
	{
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/yy.tpk";
		arcGISLocalTiledLayerPath = getDataPath(str);
		return arcGISLocalTiledLayerPath;
	}

	/**
	 * ��ȡ��������·��
	 * 
	 * @return
	 */
	public String getNaviPath()
	{
		String str = navi;
		String navipathString = getDataPath(str);
		return navipathString;
	}

	public List<Map<String, Object>> getDateFor(String name)
	{
		String mapPath = otms + "/" + name;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		File[] files = new File(getXBDataPath(mapPath)).listFiles();
		for (int i = 0; i < files.length; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			if (files[i].getName().endsWith(".otms"))
			{
				String str = files[i].getName().replace(".otms", "");
				if (str.equals(name))
				{
					map.put("cbbox", false);
					map.put("path", files[i].toString());
					map.put("filename", files[i].getName().replace(".otms", ""));
					list.add(map);
					break;
				}
			}
		}
		return list;
	}

	public List<Map<String, Object>> getAllGeodatabaseName(String type,
			String name)
	{
		String[] str =
 { "������", "��ɽ����", "������", "������", "�ڵ���",
				"��Ϫ��", "������", "���ü���������", "������", "������",
				"Ϣ����", "������", "˳���ֳ�", "�������ֳ�" };
		String mapPath = otms + "/" + type;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		File[] files = new File(getXBDataPath(mapPath)).listFiles();
		if (!name.equals(""))
		{
			for (int i = 0; i < files.length; i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				if (files[i].getName().endsWith(".otms"))
				{
					String filename = files[i].getName().replace(".otms", "");
					if (filename.equals(name))
					{
						map.put("cbbox", false);
						map.put("path", files[i].toString());
						map.put("filename",
								files[i].getName().replace(".otms", ""));
						list.add(map);
						files[i].delete();
						break;
					}
				} else
				{
					files[i].delete();
				}
			}

			for (int i = 0; i < files.length; i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cbbox", false);
				map.put("path", files[i].toString());
				map.put("filename", files[i].getName().replace(".otms", ""));
				list.add(map);
			}
		} else
		{
			for (int j = 0; j < str.length; j++)
			{
				for (int i = 0; i < files.length; i++)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					if (files[i].getName().endsWith(".otms"))
					{
						String filename = files[i].getName().replace(".otms",
								"");
						if (filename.equals(str[j]))
						{
							map.put("cbbox", false);
							map.put("path", files[i].toString());
							map.put("filename", filename);
							list.add(map);
							files[i].delete();
							break;
						}
					} else
					{
						files[i].delete();
					}
				}
			}
			for (int i = 0; i < files.length; i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cbbox", false);
				map.put("path", files[i].toString());
				map.put("filename", files[i].getName().replace(".otms", ""));
				list.add(map);
			}

		}
		return list;
	}

	public static String getDataBase(Context context2, String filename)
			throws FileNotFoundException
	{
		File db = null;
		if (filename == null)
			return "";
		db = new File(getDataPath(sqlite + "/" + filename));
		if (db.exists())
		{
			return db.toString();
		}
		throw new FileNotFoundException("文件不存在");
	}
	public static String getDataBase( String filename)
			throws FileNotFoundException
	{
		File db = null;
		if (filename == null)
			return "";
		db = new File(getDataPath(sqlite + "/" + filename));
		if (db.exists())
		{
			return db.toString();
		}
		throw new FileNotFoundException("文件不存在");
	}
    /**
     * 获取tpk文件
     */
	public static String gettpk( String filename)
			throws FileNotFoundException
	{
		File file = null;
		if (filename == null)
			return "";
		file = new File(getDataPath(otitan_map + "/" + filename));
		if (file.exists())
		{
			return file.toString();
		}
		throw new FileNotFoundException("文件不存在");
	}

	public boolean saveTxt(Context context, String SBH)
	{

		// ����ļ����Ƿ����
		String path = "/txt";
		String str = getDataPath(path);
		if (str.equals(""))
		{
			path = getPath()[1] + PATH_MAPS + "/maps" + path;
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
		}

		String p = str + File.separator + "SBH.txt";
		FileOutputStream outputStream = null;
		try
		{
			System.out.println("�����ļ�����д������");
			// �����ļ�����д������
			outputStream = new FileOutputStream(new File(p));
			String msg = new String("�豸��:" + SBH + "\n");
			outputStream.write(msg.getBytes("UTF-8"));// "UTF-8"
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (outputStream != null)
			{
				try
				{
					outputStream.flush();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				try
				{
					outputStream.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public void saveSBH(Context context, String SBH, String XLH)
	{

		File path = Environment.getExternalStorageDirectory();

		File file = new File(path, SBH + ".PUID");
		if (file.exists())
			return;
		FileOutputStream outputStream = null;
		try
		{
			System.out.println("�����ļ�����д������");
			// �����ļ�����д������
			outputStream = new FileOutputStream(file);
			String msg = new String("�豸��:" + SBH + "\n" + "���кţ�" + XLH);
			outputStream.write(msg.getBytes("UTF-8"));// "UTF-8"
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (outputStream != null)
			{
				try
				{
					outputStream.flush();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				try
				{
					outputStream.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ��ȡС�������ͼƬ
	 *
	 */
	public File[] getImage(String path)
	{
		String mapPath = "/image/" + path;
		File[] files = new File(getDataPath(mapPath)).listFiles();
		return files;
	}

	// 获取父级目录
	public List<File> getGeodatabaseName()
	{
		String mapPath = otms;
		File[] files = new File(getXBDataPath(mapPath)).listFiles();
		List<File> groups = new ArrayList<File>();
		int files_lenght = files.length;
		for (int i = 0; i < files_lenght; i++)
		{
			if (!files[i].isDirectory())
			{
				continue;
			}
			groups.add(files[i]);
		}
		return groups;
	}

	// 获取子目录
	public List<List<File>> getChildeName(String name)
	{
		String[] str = context.getResources().getStringArray(R.array.data_sx);
		List<File> groups = getGeodatabaseName();
		int groups_length = groups.size();
		List<List<File>> childs = new ArrayList<List<File>>();
		if (!name.equals(""))
		{
			for (int i = 0; i < groups_length; i++)
			{
				String mapPath = otms + "/" + groups.get(i).getName();
				File[] files = new File(getXBDataPath(mapPath)).listFiles();
				ArrayList<File> list = new ArrayList<File>();
				for (int j = 0; j < files.length; j++)
				{
					if (files[j].isDirectory())
					{
						continue;
					}
					if ((files[j].getName().endsWith(".otms") || files[j]
							.getName().endsWith(".geodatabase"))
							&& (files[j].getName().contains(name)))
					{
						list.add(files[j]);
					}
				}
				childs.add(list);
			}
		} else
		{
			for (int i = 0; i < groups_length; i++)
			{
				ArrayList<File> list_0 = new ArrayList<File>();
				ArrayList<File> list = new ArrayList<File>();
				String mapPath = otms + "/" + groups.get(i).getName();
				File[] files = new File(getXBDataPath(mapPath)).listFiles();

				if ((files == null) || (files.length == 0))
				{
					childs.add(list_0);
					continue;
				}
				for (int j = 0; j < files.length; j++)
				{
					if (files[j].getName().endsWith(".otms")
							|| files[j].getName().endsWith(".geodatabase"))
					{
						list.add(files[j]);
					}
				}
				for (int m = 0; m < str.length; m++)
				{
					for (int k = 0; k < list.size(); k++)
					{
						String filename = "";
						if (files[k].getName().endsWith(".otms"))
						{
							filename = list.get(k).getName()
									.replace(".otms", "");
						}
						if (files[k].getName().endsWith(".geodatabase"))
						{
							filename = list.get(k).getName()
									.replace(".geodatabase", "");
						}
						if (filename.equals(str[m]))
						{
							list_0.add(list.get(k));
							list.remove(k);
							break;
						}
					}
				}
				if (list.size() > 0)
				{
					list_0.addAll(list);
				}
				childs.add(list_0);
			}
		}
		return childs;
	}

	public List<File> getShapeLayer()
	{
		String mapPath = shape;
		List<File> list = new ArrayList<File>();
		File[] files = new File(getXBDataPath(mapPath)).listFiles();
		int m = files.length;
		for (int i = 0; i < m; i++)
		{
			if (files[i].getName().endsWith(".shp"))
			{
				list.add(files[i]);
			}
		}
		return list;
	}
}
