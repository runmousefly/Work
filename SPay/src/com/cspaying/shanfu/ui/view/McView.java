package com.cspaying.shanfu.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.cspaying.shanfu.R;


public class McView {
	private static McView instance;
	private Context context;
	public McView(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public static McView getInstance(Context context){
		if(instance == null){
			instance = new McView(context);
		}
		
		return instance;
		
	}
	
	
	@SuppressLint("ResourceAsColor")
	public  GraphicalView getCurveIntent(Context context) {          //得到用于显示曲线图的intent
		String[] titles = new String[] {"1"}; // 图最下面显示的数据，表示曲线的名字
		List<double[]> x = new ArrayList<double[]>();
		for (int i = 0; i < titles.length; i++) {  
			//x.add(new double[] { 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			//		18, 19, 20, 21, 22, 23, 24 }); // x坐标值
			x.add(new double[] { 1,2,3,4,5,6,7 });
		}
		
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 0, 5, 10, 15}); // y坐标值
		/*values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14,
				11, 10, 9, 6.5, 2, -2, -2.5, -4 });
		values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9,
				8, 7.5, 7, 6, 5.3, 5.0, 4, -1 });*/
		int[] colors = new int[] { Color.RED }; // 用于曲线设置曲线的颜色和曲线的名字颜色
		PointStyle[] styles = new PointStyle[] { PointStyle.SQUARE}; // 曲线的样式,有多种
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(); // 最主要的一个对象，用于设置曲线的很多参数配置
		renderer.setAxisTitleTextSize(20); // 设置表示x轴，Y轴信息文本的大小
		renderer.setChartTitleTextSize(20); // 曲线图说明文字的大小
		renderer.setLabelsTextSize(30); // x,y轴上面坐标文本的字体大小
		renderer.setLegendTextSize(30); // 设置下方表示曲线名字的文本字体大小
		renderer.setGridColor(Color.LTGRAY); // 设置网格边框的颜色
		renderer.setPointSize(8f); // 设置曲线上面小图形的大小
		renderer.setMargins(new int[] { 40, 50, 10, 10 }); // 数据分别为曲线图离屏幕的上左下右的间距
		renderer.setXLabels(0); // 网格x轴的大概条数
		renderer.setYLabels(4); // 网格Y轴的大概条数
		renderer.setShowGrid(true); // 是否显示网格
		renderer.setApplyBackgroundColor(false); // 是否可以设置背景颜色，默认为false
		renderer.setMarginsColor(R.color.mc_view); //设置外面背景色，坐标颜色也会随着变话，不知道什么情况
		renderer.setBackgroundColor(R.color.mc_view); // 设置网格背景颜色，需要和上面属性一起用
		renderer.setYLabelsAlign(Align.RIGHT); // 以Y轴的哪个地方对其
		renderer.setZoomButtonsVisible(false); // 是否显示右下角的放大缩小还原按钮
		renderer.setPanLimits(new double[] { 1, 10, -10, 40 }); // 滚动後显示的数据，和这里设置一样则不滚动
																// renderer.setXAxisMin(1);
																// renderer.setXAxisMax(10);
		renderer.setZoomLimits(new double[] { 100, 50, 20, 5 }); // 点击放大缩小时候使用
		renderer.setChartTitle(""); // 设置种上文本类荣
		renderer.setXTitle(""); // 设置x轴名字
		renderer.setYTitle(""); // y轴名字
		renderer.setXAxisMin(1); // x轴坐标最小值
		renderer.setXAxisMax(7); // x轴坐标最大值
		renderer.setYAxisMin(0); // y轴最小值
		renderer.setYAxisMax(15); // y值最大值
		renderer.setLabelsColor(Color.RED);
		renderer.setPanEnabled(false);
		renderer.setZoomEnabled(false);
		renderer.setAxesColor(R.color.mc_view); // x，y轴的颜色
		int length1 = colors.length;
		for (int i = 0; i < length1; i++) { // 设置颜色
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset(); // 设置图下面显示的信息
		int length2 = titles.length;
		for (int i = 0; i < length2; i++) {
			XYSeries series = new XYSeries(titles[i], 0);
			double[] xV = x.get(i);
			double[] yV = values.get(i);
			int seriesLength = yV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		//intent = ChartFactory.getLineChartIntent(this, dataset, renderer,
		//		"天气预报");
		//startActivity(intent);
		GraphicalView mLineChartView = ChartFactory.getLineChartView(context, dataset
			, renderer);
		// view.addView(mLineChartView, new LayoutParams(LayoutParams.FILL_PARENT
				// , LayoutParams.FILL_PARENT));
		// view_line.addView(child);
		  //mLineChartView.setOnTouchListener(chartViewOnTouchListener);
		 //mLineChartView.setId(0)
		return mLineChartView;
	}

}
