package Visualizations;

import java.awt.Color;
import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class Plotter extends ApplicationFrame {

    public Plotter(ArrayList<Integer> bests, ArrayList<Integer> worsts, ArrayList<Float> avgs) {
        super("Fitness Graphs");
        JFreeChart chart = ChartFactory.createXYLineChart("Fitness Graphs",
                "Generation", "Fitness",
                createData(bests, worsts, avgs),
                PlotOrientation.VERTICAL, true, false, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        final XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        renderer.setSeriesStroke(2, new BasicStroke(3.0f));
        plot.setRenderer(renderer);
        setContentPane(chartPanel);

        try {
            ChartUtilities.saveChartAsJPEG( new File( "FitnessChart.jpg" ), chart, 640, 480);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }

    private XYDataset createData(ArrayList<Integer> bests, ArrayList<Integer> worsts, ArrayList<Float> avgs) {
        final XYSeries worst = new XYSeries("Worst");
        for (int i = 0; i < worsts.size(); i++) worst.add(i, worsts.get(i));


        final XYSeries best = new XYSeries("Best");
        for (int i = 0; i < bests.size(); i++) best.add(i, bests.get(i));

        final XYSeries average = new XYSeries("Average");
        for (int i = 0; i < avgs.size(); i++) average.add(i, avgs.get(i));

        final XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(worst);
        data.addSeries(best);
        data.addSeries(average);
        return data;
    }
}