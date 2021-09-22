package com.glowingsoft.zvandiri.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.glowingsoft.zvandiri.Interfaces.GetMedicineCheckStatus;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.models.MedicineActivityModel;

import java.util.List;

/**
 * Created by Asif on 2/8/2019.
 */

public class MedicineActivityAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    List<MedicineActivityModel> model;
    GetMedicineCheckStatus checkStatus;

    public MedicineActivityAdapter(Context context, List<MedicineActivityModel> model, GetMedicineCheckStatus checkStatus) {
        this.context = context;
        this.model = model;
        this.checkStatus = checkStatus;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.medicine_views, parent, false);
        TextView title = view.findViewById(R.id.title);

        final CheckBox checkBox = view.findViewById(R.id.is_joined);
        title.setText("" + model.get(position).getMedicineTitle());
        if (model.get(position).isIs_joined()) {
            checkBox.setChecked(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkStatus.getId(model.get(position).getId());
                }
            }
        });

        return view;
    }
}
