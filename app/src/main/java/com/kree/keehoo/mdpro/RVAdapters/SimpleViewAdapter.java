package com.kree.keehoo.mdpro.RVAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kree.keehoo.mdpro.Fragments.DataDetailFragment;
import com.kree.keehoo.mdpro.KeysAndConstants.Keys;
import com.kree.keehoo.mdpro.KeysAndConstants.Obj;
import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.activities.DataDetailActivity;
import com.kree.keehoo.mdpro.activities.DataListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;



public class SimpleViewAdapter
        extends RecyclerView.Adapter<SimpleViewAdapter.SimpleViewHolder> {

    private final List<Obj> mValues;
    Context context;
    private int focusedItem = 0;

    boolean mTwoPanes;


    public SimpleViewAdapter(Context context, List<Obj> items, boolean mTwoPanes) {
        mValues = items;
        this.context = context;
        this.mTwoPanes = mTwoPanes;

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_list_content, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        holder.obj = mValues.get(position);
        holder.name.setText(holder.obj.getName());
        setTestImage(holder.obj, context, holder.image);
        holder.mView.setSelected(focusedItem == position);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPanes) {
                    holder.mView.setSelected(true);
                    Bundle arguments = new Bundle();
                    arguments.putString(Keys.KLUCZ, holder.obj.getName());  // tutaj musze przeslac Id
                    DataDetailFragment fragment = new DataDetailFragment();
                    fragment.setArguments(arguments);


                    ((DataListActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.data_detail_container, fragment)
                            .commit();
                } else {
                    holder.mView.setSelected(true);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DataDetailActivity.class);
                    intent.putExtra(Keys.KLUCZ, holder.obj.getName());
                    Log.d(Keys.KLUCZ, holder.obj.getName());
                    Log.d("DataListActivity", "obj.getName = " + holder.obj.getName());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void setTestImage(Obj dt, Context context, ImageView imageView) {
        Picasso.with(context)
                .load(dt.getImage())
                .placeholder(R.drawable.c)
                .error(R.drawable.e)
                .into(imageView);
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final ImageView image;
        public final View mView;
        public Obj obj;

        public SimpleViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.id);
            image = (ImageView) view.findViewById(R.id.content);
            view.setClickable(true);
        }

    }
}

