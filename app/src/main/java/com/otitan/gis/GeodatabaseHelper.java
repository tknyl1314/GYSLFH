package com.otitan.gis;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;

import java.io.FileNotFoundException;
import java.util.List;

public class GeodatabaseHelper {
	
	static Geodatabase geodatabase =null;
	/**
	 * 加载离线的geodatabase数据
	 * 
	 * @param path
	 */
	public static FeatureLayer loadGeodatabase(String path) {
		String url="";
		//ToastUtil.setToast(MapActivity.this, "加载小班数据");
		try {
			// /storage/extSdCard/maps/otms/test/长坡岭林场.otms
			// path="/storage/extSdCard/maps/otms/林权/白云区.geodatabase";
			 geodatabase = new Geodatabase(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// ToastUtil.setToast(MapActivity.this, "加载数据错误");
		} catch (RuntimeException e1) {
			//decript(path);
			/*try {
				geodatabase = new Geodatabase(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}*/
			e1.printStackTrace();
		}

		
		if (geodatabase != null) {
			List<GeodatabaseFeatureTable> list = geodatabase
					.getGeodatabaseTables();
			for (GeodatabaseFeatureTable gdbFeatureTable : list) {
				if (gdbFeatureTable.hasGeometry()) {
					final FeatureLayer featureLayer = new FeatureLayer(gdbFeatureTable);
					String layername=gdbFeatureTable.getLayerServiceInfo().getName();
					if(layername.equals("贵阳市行政界线")){
						// url=featureLayer.getQueryUrl(0);
						geodatabase.dispose();//释放资源
						return featureLayer;
					}
				/*	mapView.addLayer(featureLayer);
					featureLayerList.add(featureLayer);
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("pname", pName);
					hashMap.put("cname", cName);
					nameList.add(hashMap);*/
				}
			}

		} /*else {
			//ToastUtil.setToast(MapActivity.this, "加载数据错误");
		}*/
		return null;
	}

}
