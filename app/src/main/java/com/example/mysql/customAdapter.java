package com.example.mysql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customAdapter extends BaseAdapter {
    Context mContext;
    private ArrayList<User> user = new ArrayList<>();

    public customAdapter(Context context, ArrayList<User> user) {
        mContext = context;
        this.user = user;
    }

    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_user, parent, false);
        }
        User tempUser = (User) getItem(position);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvSurName = (TextView) convertView.findViewById(R.id.tvSurName);
        TextView tvMidName = (TextView) convertView.findViewById(R.id.tvMidName);
        TextView tvAccess = (TextView) convertView.findViewById(R.id.tvAccess);

        tvId.setText(tempUser.getId());
        tvName.setText(tempUser.getName());
        tvSurName.setText(tempUser.getSurname());
        tvMidName.setText(tempUser.getMiddlename());
        tvAccess.setText(tempUser.getAccess());

        return convertView;
    }
}
