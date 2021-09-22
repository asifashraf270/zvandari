package com.glowingsoft.zvandiri.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.glowingsoft.zvandiri.Adapaters.VideoFragmentAdapter;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.models.VideoFragmentModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class VideosFragment extends Fragment {
    VideoFragmentAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    ListView listView;
    AsyncHttpClient client;
    List<VideoFragmentModel> models;
    ProgressDialog progressDialog;
    String nextPageToken = null;
    LinearLayout linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        bindViews(view);
        return view;
    }


    private void bindViews(View view) {
        final View view1 = getActivity().getLayoutInflater().inflate(R.layout.footer_view, null);
        client = new AsyncHttpClient();
        linearLayout = view.findViewById(R.id.linearLayout);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        listView = view.findViewById(R.id.listview);
        listView.setOnScrollListener(new OnScrollFinishListener() {
            @Override
            protected void onScrollFinished() {
                final List<VideoFragmentModel> dataModel = new ArrayList<>();
                if (nextPageToken != null) {
                    listView.addFooterView(view1);
                    client.get(ServerConnection.endpoint + "&pageToken=" + nextPageToken, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            listView.removeFooterView(view1);
                            super.onSuccess(statusCode, headers, response);
                            JSONArray jsonArray = null;
                            try {
                                nextPageToken = response.getString("nextPageToken");
                                jsonArray = response.getJSONArray("items");
                                nextPageToken = response.getString("nextPageToken");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    dataModel.add(new VideoFragmentModel(jsonObject.getJSONObject("id").getString("videoId"), jsonObject.getJSONObject("snippet").getString("publishedAt"), jsonObject.getJSONObject("snippet").getString("title"), jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url")));
                                }
                                adapter.append(dataModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                nextPageToken = null;
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Snackbar snackbar = Snackbar.make(linearLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
                            View view = snackbar.getView();
                            view.setBackgroundColor(Color.RED);
                            snackbar.show();
                        }
                    });
                }
            }
        });

        dataApi();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataApi();
            }
        });

    }

    /**
     * calling youtube data api
     */
    private void dataApi() {
        models = new ArrayList<>();
        client.get(ServerConnection.endpoint, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    nextPageToken = response.getString("nextPageToken");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        models.add(new VideoFragmentModel(jsonObject.getJSONObject("id").getString("videoId"), jsonObject.getJSONObject("snippet").getString("publishedAt"), jsonObject.getJSONObject("snippet").getString("title"), jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url")));
                        adapter = new VideoFragmentAdapter(getContext(), models);
                        listView.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    nextPageToken = null;
                    Snackbar snackbar = Snackbar.make(linearLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(Color.RED);
                    snackbar.show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("exception", responseString);
                Snackbar snackbar = Snackbar.make(linearLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                progressDialog.dismiss();
            }
        });
    }


    public abstract class OnScrollFinishListener implements AbsListView.OnScrollListener {

        int mCurrentScrollState;
        int mCurrentVisibleItemCount;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mCurrentScrollState = scrollState;
            if (isScrollCompleted()) {
                onScrollFinished();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mCurrentVisibleItemCount = visibleItemCount;
        }

        private boolean isScrollCompleted() {
            return mCurrentVisibleItemCount > 0 && mCurrentScrollState == SCROLL_STATE_IDLE;
        }

        protected abstract void onScrollFinished();
    }

}
