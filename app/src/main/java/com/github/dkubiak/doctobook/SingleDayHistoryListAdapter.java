package com.github.dkubiak.doctobook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.dkubiak.doctobook.model.Visit;

import java.util.List;

/**
 * Created by dawid.kubiak on 10/01/16.
 */
public class SingleDayHistoryListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Visit> visitItems;

    public SingleDayHistoryListAdapter(Activity activity, List<Visit> visitItems) {
        this.visitItems = visitItems;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return visitItems.size();
    }

    @Override
    public Object getItem(int position) {
        return visitItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return visitItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.single_day_list_row, null);

        TextView tvPatientName = (TextView) convertView.findViewById(R.id.tvPatientName);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.tvPoint);

        Visit visit = visitItems.get(position);

        tvPatientName.setText(visit.getPatientName());
        tvAmount.setText(String.valueOf(visit.getAmount()) + " zł");
        tvPoint.setText(String.valueOf(visit.getPoint()) + " pkt.");

        return convertView;
    }
}
