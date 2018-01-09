package com.kree.keehoo.mdpro.RVAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kree.keehoo.mdpro.KeysAndConstants.Consts;
import com.kree.keehoo.mdpro.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SimpleViewAdapter extends RecyclerView.Adapter<SimpleViewAdapter.SimpleViewHolder> {

    private final List<ElementOfTheTappticList> mValues;
    private Context context;
    OnElementClickListener listener;
    OnElementFocusListener focusListener;
    public Consts consts;

    boolean mTwoPanes;


    public SimpleViewAdapter(Context context, List<ElementOfTheTappticList> items, boolean mTwoPanes) {
        mValues = items;
        this.context = context;
        this.mTwoPanes = mTwoPanes;
        this.consts = new Consts(context);
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
        if (consts.getLastSelectionId() == position) {
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
        public final TextView name;
        public final ImageView image;
        public ElementOfTheTappticList elementOfTheTappticList;
        private RelativeLayout itemLayout;
        int currentPosition;
        SimpleViewAdapter adapter;

        public SimpleViewHolder(final View view, SimpleViewAdapter adapter) {
            super(view);
            name = (TextView) view.findViewById(R.id.id);
            image = (ImageView) view.findViewById(R.id.content);
            itemLayout = (RelativeLayout) view.findViewById(R.id.item_layout);
            this.adapter = adapter;

            itemView.setOnClickListener(this);
            itemView.setOnFocusChangeListener(this);

/*
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case ACTION_DOWN:
                            name.setTextColor(Color.WHITE);
                            itemView.setBackgroundColor(Color.GREEN);
                            break;
                        case ACTION_UP:
                            name.setTextColor(Color.BLACK);
                            v.performClick();
                            break;
                        default:
                            name.setTextColor(Color.BLACK);
                            break;
                    }

                    return true;
                }

            });*/

            //  name.setTextColor(context.getResources().getColorStateList(R.color.text_color_statelist));

            //     name.setTextColor(Color.BLACK);
        }

        @Override
        public void onClick(View view) {
            if (adapter.listener != null) {
                listener.onClick(elementOfTheTappticList, currentPosition);
                this.getItemLayout().setSelected(true);
                consts.saveCurrentOnClickId(currentPosition);
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
            }

            else {
                name.setTextColor(Color.BLACK);

            }

        }

        public RelativeLayout getItemLayout() {
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

