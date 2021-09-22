package com.glowingsoft.zvandiri.Adapaters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glowingsoft.zvandiri.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Asif on 2/8/2019.
 */

public class QuizFragmentAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    JSONArray mData;
    JSONObject questionsModel;
    JSONObject mcqOptionModel;

    public QuizFragmentAdapter(Context context, JSONArray mData) {
        this.context = context;
        this.mData = mData;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mData.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Typeface typeface = ResourcesCompat.getFont(context, R.font.aldrich);

        View view = layoutInflater.inflate(R.layout.quiz_view, parent, false);
        LinearLayout linearLayout = view.findViewById(R.id.radioGroupLinear);
        TextView question = view.findViewById(R.id.question);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256 - 1) + 1, rnd.nextInt(256 - 1) + 1, rnd.nextInt(256 - 1) + 1);
        RelativeLayout maincontainer = view.findViewById(R.id.mainContainer);
        maincontainer.setBackgroundColor(color);
        RadioGroup radioGroup = new RadioGroup(context);
        LinearLayout.LayoutParams radiogroupparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        radioGroup.setLayoutParams(radiogroupparams);

        try {
            questionsModel = mData.getJSONObject(position);
            question.setText("" + questionsModel.getString("question"));
            JSONArray answer = questionsModel.getJSONArray("answers");
            radioGroup.setTag(position);
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < answer.length(); i++) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(answer.getJSONObject(i).getString("option"));
                radioButton.setTypeface(typeface);
                radioGroup.addView(radioButton);
                radioButton.setTag(i);
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int radioPos = (int) view.getTag();
                        Log.d("response radioPos", radioPos + " ");
                        View parentViewRadioGroupd = (View) view.getParent();
                        int radioGroupPos = (int) parentViewRadioGroupd.getTag();
                        Log.d("response radioGroupPos", radioGroupPos + " ");
                        try {
                            String is_correct = mData.getJSONObject(radioGroupPos).getJSONArray("answers").getJSONObject(radioPos).getString("is_correct");
                            Log.d("response is_correct", is_correct + " ");
                            LinearLayout linearLayout1 = (LinearLayout) parentViewRadioGroupd.getParent();
                            TextView textView = (TextView) linearLayout1.getChildAt(1);
                            if (is_correct.equals("0")) {
                                textView.setTypeface(typeface);
                                textView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
                                textView.setText("Wrong Answer. Please try another option");
                                textView.setTextColor(Color.RED);
                            } else {
                                textView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
                                textView.setText(mData.getJSONObject(radioGroupPos).getString("answer"));
                                textView.setTextColor(Color.GREEN);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        linearLayout.addView(radioGroup);
        TextView textView = new TextView(context);
        textView.setId(111);
        textView.setPadding(10, 10, 10, 10);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(textView);


        return view;
    }
}
