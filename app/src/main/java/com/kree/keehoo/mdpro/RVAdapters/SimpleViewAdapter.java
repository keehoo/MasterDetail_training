package com.kree.keehoo.mdpro.RVAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kree.keehoo.mdpro.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class SimpleViewAdapter extends RecyclerView.Adapter<SimpleViewAdapter.SimpleViewHolder> {

    private final List<ElementOfTheTappticList> mValues;
    private Context context;
    private int focusedItem = 0;
    OnElementClickListener listener;

    boolean mTwoPanes;


    public SimpleViewAdapter(Context context, List<ElementOfTheTappticList> items, boolean mTwoPanes) {
        mValues = items;
        this.context = context;
        this.mTwoPanes = mTwoPanes;

    }

    public void setListener(OnElementClickListener listener) {
        this.listener = listener;
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
        holder.mView.setSelected(focusedItem == position);
        holder.currentPosition = position;
        holder.elementOfTheTappticList = mValues.get(position);


/*        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getName(), "Clicked on the list item");
                if (mTwoPanes) {
                    holder.mView.setSelected(true);
                    Bundle arguments = new Bundle();
                    arguments.putString(Keys.KLUCZ, holder.elementOfTheTappticList.getName());
                    arguments.putString(Keys.KLUCZ_IMAGE, holder.elementOfTheTappticList.getImageUrl());   // tutaj musze przeslac Id
                    DataDetailFragment fragment = new DataDetailFragment();
                    fragment.setArguments(arguments);


                    ((DataListActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.data_detail_container, fragment)
                            .commit();
                } else {
                    holder.mView.setSelected(true);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DataDetailActivity.class);
                    intent.putExtra(Keys.KLUCZ, holder.elementOfTheTappticList.getName());
                    Log.d(Keys.KLUCZ, holder.elementOfTheTappticList.getName());
                    Log.d("DataListActivity", "elementOfTheTappticList.getName = " + holder.elementOfTheTappticList.getName());
                    context.startActivity(intent);
                }
            }
        });*/
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

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        public final TextView name;
        public final ImageView image;
        public final View mView;
        public ElementOfTheTappticList elementOfTheTappticList;
        int currentPosition;
        SimpleViewAdapter adapter;

        public SimpleViewHolder(View view, SimpleViewAdapter adapter) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.id);
            image = (ImageView) view.findViewById(R.id.content);
            this.adapter = adapter;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (adapter.listener != null) {
                listener.onClick(elementOfTheTappticList, currentPosition);
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return false;
        }
    }

    public interface OnElementClickListener {
        void onClick(ElementOfTheTappticList currentObject, int currentPosition);

    }

    interface OnElementTouchListener {
        void onTouch(ElementOfTheTappticList currentObject, int currentPosition);
    }
}

