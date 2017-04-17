package com.noahhalpern.sweeper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.x;

/**
 * Created by noahhalpern on 4/12/17.
 */

public class GameBoardAdapter extends ArrayAdapter {
    private Context mContext;
    private int mNumColumns;
    private ArrayList<CellView> mCells;

    public GameBoardAdapter(Context context, ArrayList<CellView> cells) {
        super(context, 0, cells);
        mContext = context;
        mCells = cells;
        mNumColumns = (int) Math.sqrt(cells.size());
    }

    @Override
    public int getCount() {
        return mNumColumns * mNumColumns;
    }

    @Override
    public Object getItem(int position) {
        return mCells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return (CellView) getItem(position);
    }

}
