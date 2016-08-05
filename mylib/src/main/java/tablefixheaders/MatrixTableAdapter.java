package tablefixheaders;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.table.FeatureTable;
import com.esri.core.tasks.query.QueryParameters;
import com.mylib.R;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import Util.ProgressDialogUtil;
import Util.ToastUtil;

public class MatrixTableAdapter<T> extends BaseTableAdapter{

	private final static int WIDTH_DIP = 110;
	private final static int HEIGHT_DIP = 32;

	private final Context context;
	Context mcontext;
	private T[][] table;

	private final int width;
	private final int height;

	FeatureLayer featureLayer;
	int loadcount;
	private LayoutInflater Inflater=null;
	// the interface to trigger refresh and load more.
	private IMatrixTableListener mListener;
	private Handler handler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			switch ( msg.what) {
				case 1:
					ProgressDialogUtil.stopProgressDialog();
					notifyDataSetChanged();
					break;

				default:
					break;
			}
		}

	};
	//接口
	public interface IMatrixTableListener {


		public void onLoadMore(int row);
	}
	public MatrixTableAdapter(Context context) {
		this(context, null);
	}

	public MatrixTableAdapter(Context context, T[][] table) {
		this.context = context;
		Resources r = context.getResources();

		width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP, r.getDisplayMetrics()));
		height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, r.getDisplayMetrics()));

		setInformation(table);
	}
	//加载图层小班数据适配
	public MatrixTableAdapter(Context context, T[][] table,FeatureLayer featureLayer,int loadcount) {
		this.context = context;
		this.featureLayer=featureLayer;
		this.loadcount=loadcount;
		this.table=table;
		Resources r = context.getResources();

		width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP, r.getDisplayMetrics()));
		height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, r.getDisplayMetrics()));

		setInformation(table);
	}
	//加载接警数据
	public MatrixTableAdapter(Context context, T[][] table,int loadcount) {
		this.context = context;
		this.loadcount=loadcount;
		this.table=table;
		Resources r = context.getResources();

		width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP, r.getDisplayMetrics()));
		height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, r.getDisplayMetrics()));

		setInformation(table);
	}

	public void setInformation(T[][] table) {
		this.table = table;
	}

	public void setMatrixTableListener(IMatrixTableListener listener) {
		mListener = listener;
	}

	@Override
	public int getRowCount() {
		return table.length - 1;
	}

	@Override
	public int getColumnCount() {
		return table[0].length - 1;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(final int row, int column, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TextView(context);
			((TextView) convertView).setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
			//((TextView) convertView).setTextColor(color.black);
		}
		//设置表格样式
		/*switch (getItemViewType(row, column)) {
		case 0:
			convertView = getItem(row, column, convertView, parent);
			break;
		case 1:
			convertView = getFirstHeader(row, column, convertView, parent);
			break;
		default:
			break;
		}*/
		if(table[row + 1][0] == null||table[row + 1][0].toString().equals(""))
		{
			ProgressDialogUtil.startProgressDialog(context);

			new Thread(new Runnable() {

				@Override
				public void run() {

					onLoadMore(row);
				}
			}).start();
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		((TextView) convertView).setText(table[row + 1][column + 1].toString());
		return convertView;

	}

	private void onLoadMore(int objid) {
		if ( null != mListener) {
			mListener.onLoadMore(objid);
			Message msg =new Message();
			msg.what=1;
			handler.sendMessage(msg);
		}
		else {
			FeatureTable featureTable=featureLayer.getFeatureTable();
			long featureCount=featureTable.getNumberOfFeatures();
			QueryParameters query = new QueryParameters();
			long[] objectIds=new long[20];
			for (int t=0;t<loadcount;t++)
			{
				objid++;
				objectIds[t]=objid ;
			}
			query.setObjectIds(objectIds);
			query.setOutFields(new String[] { "*" });
			Future<FeatureResult> resultFuture = featureTable.queryFeatures(query, null);
			try {
				for (Object result : resultFuture.get()) {
					Feature feature = (Feature) result;
					long id = feature.getId();
					if(id==featureCount)
					{
						ToastUtil.makeText(context, "数据全部加载完成", 0);

						//xlistview.setPullLoadEnable(false);

					}else {
						int count = (int) featureLayer.getFeatureTable().getFields().size();
						String[] row = new String[count];
						for(int i=0;i<count;i++){
							row[i] = feature.getAttributeValue(featureLayer.getFeatureTable().getFields().get(i).getName())+"";
						}
						table[(int) id]=(T[]) row;
					}
				}
				//Thread.currentThread().start();
				Message msg =new Message();
				msg.what=1;
				handler.sendMessage(msg);

			} catch (InterruptedException e) {
				ToastUtil.makeText(context,  e.toString(), 0);
				//ToastUtil.setToast(mcontext, e.toString());
				e.printStackTrace();
			} catch (ExecutionException e) {
				ToastUtil.makeText(context,  e.toString(), 0);
				//ToastUtil.setToast(LoadLayerinfoActivity.this, e.toString());
				e.printStackTrace();
			}
		}

	}

	@Override
	public int getHeight(int row) {
		return height;
	}

	@Override
	public int getWidth(int column) {
		return width;
	}

	@Override
	public int getItemViewType(int row, int column) {
		/*int itemViewType = 0;
		if (row == -1 ) {
			itemViewType = 1;
		} 
		return itemViewType;*/
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}
	//
	private View getFirstHeader(int row, int column, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView =  Inflater.inflate(R.layout.item_table_header_first, parent, false);
			((TextView) convertView).setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
		}

		return convertView;
	}
	private View getItem(int row, int column, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TextView(context);
			((TextView) convertView).setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
		}

		return convertView;
	}
}
