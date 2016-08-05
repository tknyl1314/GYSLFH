package symbol;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.TableException;

public class SymbolUtil {

	public enum MODE {
		RIGHT, LEFT, TOP, BOTTOM
	}

	/**
	 * ��������Ľ�ͼ
	 * 
	 * @param vw
	 *            ����Ҫ��ʾ�Ĳ���View
	 * @return PictureMarkerSymbol��
	 */
	public static PictureMarkerSymbol pictureSymobel(Context context,
			int layoutID) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View vw = inflater.inflate(layoutID, null);
		Bitmap bt = ArcGISUtils.convertViewToBitmap(vw);
		BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

		return new PictureMarkerSymbol(temp);
	}

	/**
	 * @param context
	 * @param view
	 *            Ҫ����view����
	 * @return PictureMarkerSymbol��
	 */
	public static PictureMarkerSymbol pictureSymobel(Context context, View view) {

		Bitmap bt = ArcGISUtils.convertViewToBitmap(view);
		BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

		return new PictureMarkerSymbol(temp);
	}

	/**
	 * 
	 * @param context
	 * @param label
	 *            �����������
	 * @param color
	 *            ������ɫ
	 * @param size
	 *            �����С
	 * @param imgInt
	 *            ͼƬ��Դ
	 * @param mode
	 *            ��ʾλ��
	 * @return
	 */
	public static PictureMarkerSymbol TextPicSymobel(Context context,
			CharSequence label, int color, float size, int imgInt, MODE mode) {

		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		ImageView imgView = new ImageView(context);
		imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		Drawable db = context.getResources().getDrawable(imgInt);
		imgView.setImageDrawable(db);

		TextView txtView = new TextView(context);
		txtView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		txtView.setText(label);
		txtView.setTextColor(color);
		txtView.setTextSize(size);

		switch (mode) {

		case RIGHT:
			layout.addView(txtView);
			layout.addView(imgView);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			break;
		case LEFT:
			layout.addView(imgView);
			layout.addView(txtView);
			layout.setGravity(Gravity.CENTER);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			break;
		case TOP:
			layout.addView(txtView);
			layout.addView(imgView);
			layout.setOrientation(LinearLayout.VERTICAL);
			break;
		case BOTTOM:
			layout.addView(imgView);
			layout.addView(txtView);
			layout.setOrientation(LinearLayout.VERTICAL);
			break;
		}
		Bitmap bt = ArcGISUtils.convertViewToBitmap(layout);
		BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

		return new PictureMarkerSymbol(temp);
	}

	/**
	 * 
	 * @param context
	 * @param label
	 *            �����������
	 * @param color
	 *            ������ɫ
	 * @param size
	 *            �����С
	 * @param mode
	 *            ��ʾλ��
	 * @return
	 */
	public static PictureMarkerSymbol TextPicSymobel(Context context,
			CharSequence label, int color, float size, MODE mode) {

		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		TextView txtView = new TextView(context);
		txtView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		txtView.setText(label);
		txtView.setTextColor(color);
		txtView.setTextSize(size);
		if (label.equals("���")) {
			layout.setPadding(60, 0, 0, 0);
		} else {
			layout.setPadding(100, 0, 0, 0);
		}
		switch (mode) {

		case RIGHT:
			layout.addView(txtView);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			break;
		case LEFT:
			layout.addView(txtView);
			layout.setGravity(Gravity.CENTER);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			break;
		case TOP:
			layout.addView(txtView);
			layout.setOrientation(LinearLayout.VERTICAL);
			break;
		case BOTTOM:
			layout.addView(txtView);
			layout.setOrientation(LinearLayout.VERTICAL);
			break;
		}
		Bitmap bt = ArcGISUtils.convertViewToBitmap(layout);
		BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

		return new PictureMarkerSymbol(temp);
	}

	/**
	 * 
	 * @param context
	 * @param label
	 *            �����������
	 * @param color
	 *            ������ɫ
	 * @param size
	 *            �����С
	 * @param mode
	 *            ��ʾλ��
	 * @return
	 */
	public static PictureMarkerSymbol TextPicSymobelArea(Context context,
			CharSequence label, int color, float size, MODE mode) {

		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		TextView txtView = new TextView(context);
		txtView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		txtView.setText(label);
		txtView.setTextColor(color);
		txtView.setTextSize(size);
		layout.setPadding(140, 0, 0, 0);
		switch (mode) {

		case RIGHT:
			layout.addView(txtView);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			break;
		case LEFT:
			layout.addView(txtView);
			layout.setGravity(Gravity.CENTER);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			break;
		case TOP:
			layout.addView(txtView);
			layout.setOrientation(LinearLayout.VERTICAL);
			break;
		case BOTTOM:
			layout.addView(txtView);
			layout.setOrientation(LinearLayout.VERTICAL);
			break;
		}
		Bitmap bt = ArcGISUtils.convertViewToBitmap(layout);
		BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

		return new PictureMarkerSymbol(temp);
	}

	public PictureMarkerSymbol TextPicSymobel(Context context,
			CharSequence label, int color, int size) {
		TextView txtView = new TextView(context);
		txtView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		txtView.setText(label);
		txtView.setTextColor(color);
		txtView.setTextSize(size);
		Bitmap bt = ArcGISUtils.convertViewToBitmap(txtView);
		BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);
		return new PictureMarkerSymbol(temp);
	}

	/**
	 * @param context
	 * @param label
	 *            ��������
	 * @param imgInt
	 *            ����ͼƬ��Դ
	 * @param mode
	 *            ���ֵİڷ�λ��
	 * @return
	 */
	public static PictureMarkerSymbol TextPicSymobel(Context context,
			CharSequence label, int imgInt, MODE mode) {
		return TextPicSymobel(context, label, Color.BLACK, 15, mode);
	}

	public static Bitmap createBitmapfromSymbol(Symbol symbol,
			ArcGISFeatureLayer layer) {
		Bitmap bitmap = null;
		// if (layer.getGeometryType().equals(Geometry.Type.POINT)) {
		// Point pt = new Point(20,20);
		// bitmap = SymbolHelper.getLegendImage(symbol,pt, 50, 50,Color.WHITE);
		// }
		// else if (layer.getGeometryType().equals(Geometry.Type.POLYLINE)) {
		// Polyline polyline = new Polyline();
		// polyline.startPath(0,0);
		// polyline.lineTo(40,40);
		// bitmap = SymbolHelper.getLegendImage(symbol,polyline, 50, 50,
		// Color.WHITE);
		// }
		// else if (layer.getGeometryType().equals(Geometry.Type.POLYGON)){
		// Polygon polygon = new Polygon();
		// polygon.startPath(0, 0);
		// polygon.lineTo(40, 0);
		// polygon.lineTo(40, 40);
		// polygon.lineTo(0, 40);
		// polygon.lineTo(0, 0);
		// bitmap = SymbolHelper.getLegendImage(symbol,polygon, 50, 50,
		// Color.WHITE);
		// }
		return bitmap;
	}

	// ��������Բ
	public static FillSymbol DroolCircle() {
		FillSymbol symbol = new SimpleFillSymbol(Color.RED);
		SimpleLineSymbol simplelinesymbol = new SimpleLineSymbol(
				Color.TRANSPARENT, (float) 0.1);
		symbol.setOutline(simplelinesymbol);
		symbol.setAlpha(30);
		return symbol;
	}

	/**
	 * �ٶȵ�����ת��Ϊ����80����
	 * 
	 * @return
	 */
	public static Point getPoint(double longitude, double latitude) {
		Point point = Converter.gps2gCoordinate(longitude, latitude);
		Point gpsPoint = Converter.g2bCoordinate(point.getX(), point.getY());
		double x = 2 * longitude - gpsPoint.getX(); // 106.69960848217
													// wgs1984������
		double y = 2 * latitude - gpsPoint.getY(); // 26.560757845054 wgs1984������
		Point point1 = (Point) GeometryEngine.project(new Point(x, y),
				SpatialReference.create(4326), SpatialReference.create(2343));
		Point upPoint = new Point(point1.getX() - 127.76798333,point1.getY() - 18.25273333);
		// upPoint = new Point(upPoint.getX() - 165.029087904161,upPoint.getY()
		// + 20.29954696370548);
		return upPoint;
	}
	/**
	 * ��ȡFeatureLayerͼ���symbol
	 * @param layer
	 * @return
	 */
	public static Symbol getLayerRenderer(final FeatureLayer layer){
		Symbol symbol = null;
		String typeIdField = ((GeodatabaseFeatureTable)layer.getFeatureTable()).getTypeIdField();

		if (typeIdField.equals("")) {
			List<FeatureTemplate> featureTemp = ((GeodatabaseFeatureTable) ((FeatureLayer) layer)
					.getFeatureTable()).getFeatureTemplates();
			for (FeatureTemplate featureTemplate : featureTemp) {
				GeodatabaseFeature g;
				try {
					g = ((GeodatabaseFeatureTable) layer
							.getFeatureTable()).createFeatureWithTemplate(featureTemplate, null);
					symbol = layer.getRenderer().getSymbol(g);
				} catch (TableException e) {
					e.printStackTrace();
				}
			}
		} else {
			List<FeatureType> featureTypes = ((GeodatabaseFeatureTable) ((FeatureLayer) layer)
					.getFeatureTable()).getFeatureTypes();
			for (FeatureType featureType : featureTypes) {
				FeatureTemplate[] templates = featureType.getTemplates();
				for (FeatureTemplate featureTemplate : templates) {
					GeodatabaseFeature g;
					try {
						g = ((GeodatabaseFeatureTable) layer
								.getFeatureTable()).createFeatureWithTemplate(featureTemplate, null);
						symbol = layer.getRenderer().getSymbol(g);
					} catch (TableException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return symbol;
	}

}
