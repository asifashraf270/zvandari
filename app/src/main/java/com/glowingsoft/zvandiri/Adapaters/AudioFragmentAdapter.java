package com.glowingsoft.zvandiri.Adapaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.activities.youtubeVideoPlayerActivity;
import com.glowingsoft.zvandiri.models.AudioFragmentModel;

import java.util.List;

/**
 * Created by Asif on 2/8/2019.
 */

public class AudioFragmentAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    PopupMenu popupMenu;
    List<AudioFragmentModel> models;

    public AudioFragmentAdapter(Context context, List<AudioFragmentModel> models) {
        this.models = models;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View view = layoutInflater.inflate(R.layout.audio_view, parent, false);
        TextView title;
        ImageView play = view.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, youtubeVideoPlayerActivity.class);
                intent.putExtra("videoId", models.get(position).getUrl());
                intent.putExtra("name", "Audio");
                intent.putExtra("id", "1");
                intent.putExtra("title", models.get(position).getTitle());
                intent.putExtra("url", models.get(position).getUrl());
                context.startActivity(intent);
            }
        });

        title = view.findViewById(R.id.title);
        title.setText("" + models.get(position).getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, youtubeVideoPlayerActivity.class);
                intent.putExtra("videoId", models.get(position).getUrl());
                intent.putExtra("title", models.get(position).getTitle());
                intent.putExtra("url", models.get(position).getUrl());
                intent.putExtra("id", "1");
                context.startActivity(intent);
            }
        });
        return view;
    }
}
