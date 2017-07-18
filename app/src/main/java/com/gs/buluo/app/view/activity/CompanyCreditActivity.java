package com.gs.buluo.app.view.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gs.buluo.app.R;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Solang on 2017/7/18.
 */

public class CompanyCreditActivity extends BaseActivity
{
    @Bind(R.id.chart_company_credit)
    PieChart pieChart;

    @Override
    protected void bindView(Bundle savedInstanceState) {
initChart();
    }
    private void initChart() {
        // 设置饼图是否接收点击事件，默认为true
        pieChart.setTouchEnabled(true);
        //设置饼图是否使用百分比
        pieChart.setUsePercentValues(true);
        //设置饼图右下角的文字描述
        pieChart.setDescription("测试");
        //设置饼图右下角的文字大小
        pieChart.setDescriptionTextSize(16);

        //是否显示圆盘中间文字，默认显示
        pieChart.setDrawCenterText(false);
        //设置圆盘中间文字
        pieChart.setCenterText("我在中间");
        //设置圆盘中间文字的大小
        pieChart.setCenterTextSize(20);
        //设置圆盘中间文字的颜色
        pieChart.setCenterTextColor(Color.WHITE);
        //设置圆盘中间文字的字体
        pieChart.setCenterTextTypeface(Typeface.DEFAULT);

        //设置中间圆盘的颜色
        pieChart.setHoleColor(Color.TRANSPARENT);
        //设置中间圆盘的半径,值为所占饼图的百分比
        pieChart.setHoleRadius(60);

        //设置中间透明圈的半径,值为所占饼图的百分比
        pieChart.setTransparentCircleRadius(60);

        //是否显示饼图中间空白区域，默认显示
        pieChart.setDrawHoleEnabled(true);
        //设置圆盘是否转动，默认转动
        pieChart.setRotationEnabled(true);
        //设置初始旋转角度
        pieChart.setRotationAngle(0);

        //设置比例图
        Legend mLegend = pieChart.getLegend();
        //设置比例图显示在饼图的哪个位置
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        //设置比例图的形状，默认是方形,可为方形、圆形、线性
        mLegend.setForm(Legend.LegendForm.CIRCLE);
//        mLegend.setXEntrySpace(7f);
//        mLegend.setYEntrySpace(5f);

        //设置X轴动画
        pieChart.animateX(1800);
//        //设置y轴动画
//        pieChart.animateY(1800);
//        //设置xy轴一起的动画
//        pieChart.animateXY(1800, 1800);

        //绑定数据
        bindData(2);

        // 设置一个选中区域监听
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Toast.makeText(CompanyCreditActivity.this,dataSetIndex+""+e.toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });
    }

    /**
     *
     * @param count 分成几部分
     */
    private void bindData(int count) {
        /**
         * nameList用来表示每个饼块上的文字内容
         * 如：部分一，部分二，部分三
         */
        ArrayList<String> nameList = new ArrayList<String>();
        nameList.add("可用额度");
        nameList.add("已使用额度");
        /**
         * valueList将一个饼形图分成三部分，各个区域的百分比的值
         * Entry构造函数中
         * 第一个值代表所占比例，
         * 第二个值代表区域位置
         * （可以有第三个参数，表示携带的数据object）这里没用到
         */
        ArrayList<Entry> valueList = new ArrayList<Entry>();
        valueList.add(new Entry(20, 0));
        valueList.add(new Entry(80, 1));

        //显示在比例图上
        PieDataSet dataSet = new PieDataSet(valueList, "不同颜色代表的含义");
        //设置个饼状图之间的距离
        dataSet.setSliceSpace(3f);
        // 部分区域被选中时多出的长度
        dataSet.setSelectionShift(5f);

        // 设置饼图各个区域颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        dataSet.setColors(colors);

        PieData data = new PieData(nameList, dataSet);
        //设置以百分比显示
        data.setValueFormatter(new PercentFormatter());
        //区域文字的大小
        data.setValueTextSize(11f);
        //设置区域文字的颜色
        data.setValueTextColor(Color.WHITE);
        //设置区域文字的字体
        data.setValueTypeface(Typeface.DEFAULT);

        pieChart.setData(data);

        //设置是否显示区域文字内容
        pieChart.setDrawSliceText(pieChart.isDrawSliceTextEnabled());
        //设置是否显示区域百分比的值
        for (IDataSet<?> set : pieChart.getData().getDataSets()){
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_credit;
    }
}
