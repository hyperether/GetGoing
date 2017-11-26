package com.hyperether.getgoing.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hyperether.getgoing.R;
import com.hyperether.getgoing.adapters.DbRouteAdapter;
import com.hyperether.getgoing.db.DbRoute;
import com.hyperether.getgoing.db.GetGoingDataSource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class ShowDataActivity extends ListActivity {

    private GetGoingDataSource datasource;
    private List<DbRoute> routes;
    private ListView list;
    private DbRouteAdapter adapter;
    private BarChart chart;

    DecimalFormat df = new DecimalFormat("#.##");      // limiting output values to 8 decimal places

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_data);

        routes = new ArrayList<>();
        // Initialize database connection
        datasource = new GetGoingDataSource(this);
        datasource.open();
        routes = datasource.getAllRoutes(); // Get the list of all routes from database
        chart = (BarChart) findViewById(R.id.barChart);

        // use the custom adapter to show the
        // elements in a ListView
        populateListView();
        populateChart();
    }

    /**
     * This method is for populating list view
     */
    private void populateListView() {
        adapter = new DbRouteAdapter(ShowDataActivity.this);
        adapter.updateRoutes(routes, datasource); // populate adapter with routes
        list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(adapter);
    }

    /**
     * Monitor for the click on one row of the ListView
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DbRoute route = routes.get((int) id);
        // Passing the unique database id of the chosen route in order to draw it correctly
        Intent intent = new Intent(getBaseContext(), ShowRouteActivity.class);
        intent.putExtra("ROUTE_ID", route.getId());
        startActivity(intent);
    }

    private ArrayList<String> xValuesChart() {
        LinkedHashSet<String> charDatesSet = new LinkedHashSet<>();
        ArrayList<String> charDates = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            charDatesSet.add(routes.get(i).getDate().substring(0, 10));
        }
        charDates.addAll(charDatesSet);
        return charDates;
    }

    private ArrayList<Map<String, Float>> collectData() {
        ArrayList<Map<String, Float>> collectedData = new ArrayList<>();
        Map<String, Float> mapY1 = new LinkedHashMap<>();
        Map<String, Float> mapY2 = new LinkedHashMap<>();
        Map<String, Float> mapY3 = new LinkedHashMap<>();

        for (DbRoute item : routes) {
            if (mapY1.containsKey(item.getDate().substring(0, 10)) && item.getActivity_id() == 1) {
                float q = mapY1.get(item.getDate().substring(0, 10)) + (float) item.getEnergy();
                mapY1.put(item.getDate().substring(0, 10), q);
            } else if (item.getActivity_id() == 1) {
                mapY1.put(item.getDate().substring(0, 10), (float) item.getEnergy());
            } else if (mapY2.containsKey(item.getDate().substring(0, 10)) && item.getActivity_id() == 2) {
                float q = mapY2.get(item.getDate().substring(0, 10)) + (float) item.getEnergy();
                mapY2.put(item.getDate().substring(0, 10), q);
            } else if (item.getActivity_id() == 2) {
                mapY2.put(item.getDate().substring(0, 10), (float) item.getEnergy());
            } else if (mapY3.containsKey(item.getDate().substring(0, 10)) && item.getActivity_id() == 3) {
                float q = mapY3.get(item.getDate().substring(0, 10)) + (float) item.getEnergy();
                mapY3.put(item.getDate().substring(0, 10), q);
            } else if (item.getActivity_id() == 3) {
                mapY3.put(item.getDate().substring(0, 10), (float) item.getEnergy());
            }
        }
        collectedData.add(mapY1);
        collectedData.add(mapY2);
        collectedData.add(mapY3);
        return collectedData;
    }

    private ArrayList<IBarDataSet> prepareData() {
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> yVal1 = new ArrayList<>();
        ArrayList<BarEntry> yVal2 = new ArrayList<>();
        ArrayList<BarEntry> yVal3 = new ArrayList<>();
        ArrayList<String> x = xValuesChart();
        Map<String, Float> y1 = collectData().get(0);
        Map<String, Float> y2 = collectData().get(1);
        Map<String, Float> y3 = collectData().get(2);
        String xVal;
        float emptyValue = 0;

        for (int i = 0; i < x.size(); i++) {
            xVal = x.get(i);
            if (y1.containsKey(x.get(i))) {
                yVal1.add(new BarEntry(y1.get(xVal), i));
            } else {
                yVal1.add(new BarEntry(emptyValue, i));
            }
        }
        for (int i = 0; i < x.size(); i++) {
            xVal = x.get(i);
            if (y2.containsKey(x.get(i))) {
                yVal2.add(new BarEntry(y2.get(xVal), i));
            } else {
                yVal2.add(new BarEntry(emptyValue, i));
            }
        }
        for (int i = 0; i < x.size(); i++) {
            xVal = x.get(i);
            if (y3.containsKey(x.get(i))) {
                yVal3.add(new BarEntry(y3.get(xVal), i));
            } else {
                yVal3.add(new BarEntry(emptyValue, i));
            }
        }
        BarDataSet set1 = new BarDataSet(yVal1, getString(R.string.chart_label_walk));
        BarDataSet set2 = new BarDataSet(yVal2, getString(R.string.chart_label_run));
        BarDataSet set3 = new BarDataSet(yVal3, getString(R.string.chart_label_ride));
        set1.setColor(getResources().getColor(R.color.walk_graph_color));
        set1.setValueTextSize(10f);
        set2.setColor(getResources().getColor(R.color.run_graph_color));
        set2.setValueTextSize(10f);
        set3.setColor(getResources().getColor(R.color.ride_graph_color));
        set3.setValueTextSize(10f);
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        return dataSets;
    }

    private void populateChart() {
        BarData data = new BarData(xValuesChart(), prepareData());
        chart.setData(data);
        chart.moveViewToX(xValuesChart().size());
        chart.setDescription("");
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.animateY(2000, Easing.EasingOption.Linear);
        chart.setVisibleXRangeMaximum(8);
        chart.setDrawBarShadow(true);
        chart.getAxisLeft().setAxisMinValue(0f);
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    protected void onStop() {
        datasource.close();
        super.onStop();
    }
}
