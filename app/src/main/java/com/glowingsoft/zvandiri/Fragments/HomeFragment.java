package com.glowingsoft.zvandiri.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.activities.CatsGuideActivity;
import com.glowingsoft.zvandiri.activities.FactSheetActivity;
import com.glowingsoft.zvandiri.activities.MediaActivity;
import com.glowingsoft.zvandiri.activities.PrivacyPolicyactivity;
import com.glowingsoft.zvandiri.activities.QuizCategoryActivity;


public class HomeFragment extends Fragment implements View.OnClickListener {
    RelativeLayout catsGuide, audio_video, factSheet, quizess, promises;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        catsGuide = view.findViewById(R.id.catsGuide);
        catsGuide.setOnClickListener(this);
        audio_video = view.findViewById(R.id.audio_video);
        audio_video.setOnClickListener(this);
        factSheet = view.findViewById(R.id.fSheet);
        factSheet.setOnClickListener(this);
        quizess = view.findViewById(R.id.quizess);
        quizess.setOnClickListener(this);
        promises = view.findViewById(R.id.promises);
        promises.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.catsGuide:
                Intent intent = new Intent(getContext(), CatsGuideActivity.class);
                intent.putExtra("name", "Cats Guide");
                startActivity(intent);
                break;
            case R.id.audio_video:
                startActivity(new Intent(getContext(), MediaActivity.class));
                break;
            case R.id.fSheet:
                Intent intent1 = new Intent(getContext(), FactSheetActivity.class);
                intent1.putExtra("name", "Fact Sheets");
                startActivity(intent1);
                break;
            case R.id.quizess:
                startActivity(new Intent(getContext(), QuizCategoryActivity.class));
                break;
            case R.id.promises:
                Intent aboutUS = new Intent(getContext(), PrivacyPolicyactivity.class);
                aboutUS.putExtra("title", SharedPreferencesClass.sharedPreference.getString("promisesTitle", ""));
                aboutUS.putExtra("value", SharedPreferencesClass.sharedPreference.getString("promisesDesc", ""));
                startActivity(aboutUS);
                break;

        }
    }


}
