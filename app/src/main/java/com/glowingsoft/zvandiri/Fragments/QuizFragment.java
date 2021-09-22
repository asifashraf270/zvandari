package com.glowingsoft.zvandiri.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glowingsoft.zvandiri.Adapaters.QuizFragmentAdapter;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.models.McqOptionModel;
import com.glowingsoft.zvandiri.models.QuestionsModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class QuizFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    QuizFragmentAdapter adapter;
    List<QuestionsModel> QuestionWithAnswer;
    List<McqOptionModel> mcqOptions;
    AsyncHttpClient client;
    ProgressDialog progressDialog;
    String url;
    Bundle d;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        client = new AsyncHttpClient();
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        listView = view.findViewById(R.id.listView);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        d = getArguments();

        GetQuizById(d.getString("cat_id"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetQuizById(d.getString("cat_id"));
            }
        });

    }

    /*
    Calling Rest Apis
     */
    private void GetQuizById(String cat_id) {
        QuestionWithAnswer = new ArrayList<>();
        mcqOptions = new ArrayList<>();
        url = ServerConnection.connectionIp + ServerConnection.getQuizByCategory;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        requestParams.put("category_id", cat_id);
        client.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
//                Toast.makeText(getContext(), "Called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response", response.toString());
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                try {
                    if (response.getInt("status") == 200) {
                        JSONArray jsonArray = response.getJSONArray("questions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            mcqOptions = new ArrayList<>();
                            String id = jsonArray.getJSONObject(i).getString("id");
                            String question = jsonArray.getJSONObject(i).getString("question");
                            String answer = jsonArray.getJSONObject(i).getString("answer");
                            String cat_id = jsonArray.getJSONObject(i).getString("cat_id");
                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("answers");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                mcqOptions.add(new McqOptionModel(jsonArray1.getJSONObject(i).getString("id"), jsonArray1.getJSONObject(i).getString("questionID"), jsonArray1.getJSONObject(i).getString("option"), jsonArray1.getJSONObject(i).getString("is_correct")));
                            }
                            QuestionWithAnswer.add(new QuestionsModel(question, id, answer, cat_id, mcqOptions));

                        }
//                        adapter = new QuizFragmentAdapter(getContext(), QuestionWithAnswer);
//                        listView.setAdapter(adapter);
//                        Log.d("size", String.valueOf(QuestionWithAnswer.size()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
