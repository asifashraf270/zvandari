package com.glowingsoft.zvandiri.Adapaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.activities.youtubeVideoPlayerActivity;
import com.glowingsoft.zvandiri.models.VideoFragmentModel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asif on 2/8/2019.
 */

public class VideoFragmentAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    List<VideoFragmentModel> models;

    public VideoFragmentAdapter(Context context, List<VideoFragmentModel> models) {
        this.context = context;
        this.models = models;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void append(List<VideoFragmentModel> updateModel) {
        models.addAll(updateModel);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.video_fragment_view, parent, false);
        ImageView thumbnail;
        TextView title, createdDate, totalViews;
        CircleImageView profile = view.findViewById(R.id.image);
        Picasso.get().load(SharedPreferencesClass.sharedPreference.getString("image", "")).fit().centerCrop().placeholder(R.drawable.loading).into(profile);
        title = view.findViewById(R.id.videoTitle);
        thumbnail = view.findViewById(R.id.thumbnail);
        createdDate = view.findViewById(R.id.createdDate);
        Picasso.get().load(models.get(position).getUrl()).fit().placeholder(R.drawable.loading).into(thumbnail);
        title.setText("" + models.get(position).getTitle());
        String data = models.get(position).getPublishedAt().substring(0, models.get(position).getPublishedAt().indexOf("T"));
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat writeFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = readFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = "";
        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        createdDate.setText("" + formattedDate);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, youtubeVideoPlayerActivity.class);
                intent.putExtra("videoId", models.get(position).getVideoId());
                intent.putExtra("id", "2");
                intent.putExtra("url", ServerConnection.videoSearch + models.get(position).getVideoId());
                intent.putExtra("title", models.get(position).getTitle());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
