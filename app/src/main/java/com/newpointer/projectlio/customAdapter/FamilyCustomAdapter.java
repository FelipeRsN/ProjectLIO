package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.model.FamilyModel;

import java.util.List;


/**
 * Created by FelipeRsN on 7/6/16.
 */
public class FamilyCustomAdapter extends BaseAdapter {
    Activity activity;
    Context context;

    protected List<FamilyModel> listCars;
    LayoutInflater inflater;

    public FamilyCustomAdapter(Activity activity, Context context, List<FamilyModel> listCars) {
        this.activity = activity;
        this.listCars = listCars;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return listCars.size();
    }

    @Override
    public Object getItem(int i) {
        return listCars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listCars.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.layout_family, viewGroup, false);

            holder.name = (TextView) view.findViewById(R.id.tv_family_name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final FamilyModel item = listCars.get(i);

            holder.name.setText(item.getName());

        return view;
    }

    private class ViewHolder {
        TextView name;
    }
}
