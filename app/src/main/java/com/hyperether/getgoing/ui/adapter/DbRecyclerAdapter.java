package com.hyperether.getgoing.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperether.getgoing.R;
import com.hyperether.getgoing.repository.room.entity.DbRoute;
import com.hyperether.getgoing.listeners.GgOnClickListener;

import java.util.List;

import static com.hyperether.getgoing.util.Constants.BUNDLE_PARCELABLE;


public class DbRecyclerAdapter extends RecyclerView.Adapter<DbRecyclerAdapter.ViewHolder> {

    private List<DbRoute> routes;
    private GgOnClickListener listener;


    public DbRecyclerAdapter(Context context, List<DbRoute> routes) {
        this.routes = routes;
        listener = (GgOnClickListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO setup diff screen sizes? -Ivana

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_data_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            DbRoute route = routes.get(position);

            if (!"null".equals(route.getDate())) {
                holder.chartProgress.setMax((int) route.getGoal());
                holder.chartProgress.setProgress((int) route.getLength());
                holder.chartDate.setText(route.getDate().substring(0, 5));

                holder.chartDate.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(BUNDLE_PARCELABLE, routes.get(position));

                        listener.onClick(bundle);
                    }
                }));
            }


    }

    @Override
    public int getItemCount() {
        if(routes != null) {
            return routes.size();
        }
        return 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar chartProgress;
        TextView chartDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chartProgress = itemView.findViewById(R.id.chart_progress);
            chartDate = itemView.findViewById(R.id.chart_date);
        }
    }
}