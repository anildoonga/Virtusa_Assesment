package com.virtusa.assesment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtusa.assesment.R;
import com.virtusa.assesment.interfaces.ItemClickListener;
import com.virtusa.assesment.model.Row;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anil on 2/12/2018.
 */

@SuppressWarnings("DefaultFileTemplate")
public class FeedActivityRecyclerAdapter extends RecyclerView.Adapter {
    private static ItemClickListener mItemClickListener;
    private final Context context;
    private List<Row> rows = new ArrayList<>();

    public FeedActivityRecyclerAdapter(final Context context) {
        this.context = context;
    }

    /**
     * @param mListRowFeed : Rows feed from json
     */
    public void mRecyclerViewAdapterData(List<Row> mListRowFeed) {
        this.rows = mListRowFeed;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FeedActivityRecyclerAdapter.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_view, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).feed_id.setText(Integer.toString(rows.get(position).getId()));
        ((ViewHolder) holder).feed_title.setText((rows.get(position).getTitle()));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public void setOnItemClickListener(ItemClickListener clickListener) {
        FeedActivityRecyclerAdapter.mItemClickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView feed_id;
        public final TextView feed_title;

        public ViewHolder(View itemView) {
            super(itemView);
            feed_id = (TextView) itemView.findViewById(R.id.textView_id);
            feed_title = (TextView) itemView.findViewById(R.id.textView_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}