package com.glowingsoft.zvandiri.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.models.QuizCategoryModel;

import java.util.List;

/**
 * Created by Tausif ur Rehman on 2/10/2019.
 */

public class QuizCategoryAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<QuizCategoryModel> listModel;

    public QuizCategoryAdapter(Context context, List<QuizCategoryModel> models) {
        this.context = context;
        listModel = models;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.quiz_categories, parent, false);
        TextView quizName = view.findViewById(R.id.quizName);
        quizName.setText("" + listModel.get(position).getCategoryName());


        return view;
    }
}
