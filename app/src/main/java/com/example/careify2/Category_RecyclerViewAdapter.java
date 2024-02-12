package com.example.careify2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Category_RecyclerViewAdapter extends RecyclerView.Adapter<Category_RecyclerViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<CategoryModel> categoryModels;
    public  Category_RecyclerViewAdapter(Context context, ArrayList<CategoryModel> categoryModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.categoryModels = categoryModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Category_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bereiche_card, parent, false);
        return new Category_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Category_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.bereichName.setText(categoryModels.get(position).getBereichName());
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public void filteredList(ArrayList<CategoryModel> filteredList){
        categoryModels = filteredList;
        notifyDataSetChanged();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bereichName;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            bereichName = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onCardClick(pos);
                        }
                    }
                }
            });
        }
    }
}
