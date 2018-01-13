package com.kree.keehoo.mdpro.view.RVAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.model.KeysAndConstants.PersistentValues;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.SimpleViewHolder> {

    private final List<ElementOfTheTappticList> mValues;
    private Context context;
    private OnElementClickListener listener;
    private OnElementFocusListener focusListener;
    private PersistentValues persistentValues;


    public ListAdapter(Context context, List<ElementOfTheTappticList> items) {
        mValues = items;
        this.context = context;
        this.persistentValues = new PersistentValues(context);
    }

    public void setListener(OnElementClickListener listener) {
        this.listener = listener;
    }

    public void setFocusListener(OnElementFocusListener focusListener) {
        this.focusListener = focusListener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_list_content, parent, false);
        return new SimpleViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        holder.elementOfTheTappticList = mValues.get(position);
        holder.name.setText(holder.elementOfTheTappticList.getName());
        setImage(holder.elementOfTheTappticList, context, holder.image);
        holder.currentPosition = position;
        holder.elementOfTheTappticList = mValues.get(position);
        holder.getItemLayout().setSelected(false);
        if (persistentValues.getLastSelectionId() == position) {
            holder.getItemLayout().setSelected(true);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void setImage(ElementOfTheTappticList dt, Context context, ImageView imageView) {
        Picasso.with(context)
                .load(dt.getImageUrl())
                .placeholder(R.drawable.c)
                .error(R.drawable.e)
                .into(imageView);
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {
        final TextView name;
        final ImageView image;
        ElementOfTheTappticList elementOfTheTappticList;
        private RelativeLayout itemLayout;
        int currentPosition;
        ListAdapter adapter;

        SimpleViewHolder(final View view, ListAdapter adapter) {
            super(view);
            name = (TextView) view.findViewById(R.id.id);
            image = (ImageView) view.findViewById(R.id.content);
            itemLayout = (RelativeLayout) view.findViewById(R.id.item_layout);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            itemView.setOnFocusChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            if (adapter.listener != null) {
                listener.onClick(elementOfTheTappticList, currentPosition);
                this.getItemLayout().setSelected(true);
                persistentValues.saveCurrentOnClickId(currentPosition);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (adapter.focusListener != null) {
                focusListener.onFocus(v, hasFocus, currentPosition);
            }

            if (hasFocus) {
                name.setTextColor(Color.WHITE);
            } else {
                name.setTextColor(Color.BLACK);

            }
        }

        RelativeLayout getItemLayout() {
            return itemLayout;
        }
    }

    public interface OnElementClickListener {
        void onClick(ElementOfTheTappticList currentObject, int currentPosition);

    }

    public interface OnElementFocusListener {
        void onFocus(View v, boolean hasFocus, int position);

    }
}

