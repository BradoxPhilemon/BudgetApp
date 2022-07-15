package com.mybudgetmanager.mybudget.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;




import java.util.List;

import android.graphics.Color;

import com.mybudgetmanager.mybudget.R;

public class ColorAdapter extends ArrayAdapter<Integer> {

    private final Context context;
    private final int resource;
    private final List<Integer> colors;
    private final LayoutInflater layoutInflater;

    private int selectedIndex = 0;

    public ColorAdapter(@NonNull Context context,
                        int resource,
                        @NonNull List<Integer> colors,
                        LayoutInflater layoutInflater) {
        super(context, resource, colors);
        this.context = context;
        this.resource = resource;
        this.colors = colors;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(resource, parent, false);

        if (colors.size() > 0) {
            if (position < colors.size()) {
                buildColor(view, colors.get(position));
                if (position == selectedIndex) {
                    view.setBackgroundColor(Color.argb(20, 238, 0, 0));
                }
            }
        }
        return view;
    }

    private void buildColor(View view, int color) {
        CardView cv_color = view.findViewById(R.id.row_item_color_view);
        cv_color.setCardBackgroundColor(context.getResources().getColor(color));
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
