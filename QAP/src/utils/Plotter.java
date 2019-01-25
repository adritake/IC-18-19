package utils;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class Plotter extends ApplicationFrame{

	public Plotter(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public void insertData(ArrayList<Integer> y) {
		
		final XYSeries series = new XYSeries("Random Data");
		
		for( int i = 0; i < y.size(); i++)
			series.add(i, y.get(i));
		
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
		        "Fitness variation",
		        "Generation", 
		        "Fitness", 
		        data,
		        PlotOrientation.VERTICAL,
		        true,
		        true,
		        false
		    );
		XYPlot xyPlot = chart.getXYPlot();
		ValueAxis domainAxis = xyPlot.getDomainAxis();
		ValueAxis rangeAxis = xyPlot.getRangeAxis();
		rangeAxis.setRange(45000000, 55000000);
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);
	    

	}
	
	

}
