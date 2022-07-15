package com.mybudgetmanager.mybudget.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;


import com.mybudgetmanager.mybudget.R;
import com.mybudgetmanager.mybudget.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private final Context context;
    private final int resource;
    private final List<Category> categoryList;
    private final LayoutInflater layoutInflater;

    public CategoryAdapter(@NonNull Context context,
                           int resource,
                           @NonNull List<Category> categoryList,
                           LayoutInflater layoutInflater) {
        super(context, resource, categoryList);
        this.context = context;
        this.resource = resource;
        this.categoryList = categoryList;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(resource, parent, false);

        if (categoryList.size() > 0) {
            if (position < categoryList.size()) {
                Category category = categoryList.get(position);
                if (category != null) {
                    buildCategory(view, category);
                }
            }
        }
        return view;
    }

    private void buildCategory(View view, Category category) {
        CardView cv_category_color = view.findViewById(R.id.row_item_category_color);
        TextView tv_category_name = view.findViewById(R.id.row_item_category_name);

        cv_category_color.setCardBackgroundColor(context.getResources().getColor(category.getColor()));
        tv_category_name.setText(category.getName());
    }

}
