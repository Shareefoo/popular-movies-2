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
import com.udacity.popularmovies2.models.ReviewResult;
import com.udacity.popularmovies2.models.TrailerResult;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<ReviewResult> mResults;
    private Context mContext;

    public ReviewAdapter(Activity context, List<ReviewResult> results) {
        mContext = context;
        mResults = results;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.review_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder viewHolder, int position) {
        final ReviewResult result = mResults.get(position);

        TextView textViewAuthor = viewHolder.textViewAuthor;
        textViewAuthor.setText(result.author);

        TextView textViewContent = viewHolder.textViewContent;
        textViewContent.setText(result.content);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textViewContent;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewAuthor = (TextView) itemView.findViewById(R.id.textView_author);
            textViewContent = (TextView) itemView.findViewById(R.id.textView_content);
        }
    }
}
