package com.udacity.popularmovies2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.models.TrailerResult;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<TrailerResult> mResults;
    private Context mContext;

    public TrailerAdapter(Activity context, List<TrailerResult> results) {
        mContext = context;
        mResults = results;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.trailer_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder viewHolder, int position) {
        final TrailerResult result = mResults.get(position);

        TextView textViewName = viewHolder.textViewName;
        textViewName.setText(result.name);

        viewHolder.imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.youtube.com/watch?v=" + result.key;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButtonPlay;
        TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);

            imageButtonPlay = (ImageButton) itemView.findViewById(R.id.imageButton_play);
            textViewName = (TextView) itemView.findViewById(R.id.textView_name);
        }
    }
}
