package com.example.reproductor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.print.PageRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.R;
import com.example.reproductor.ViewReproductor;
import com.example.reproductor.databinding.RowMainBinding;

public class firstRowAdapter extends RecyclerView.Adapter<firstRowAdapter.ViewHolder> {

    Context context;
    public firstRowAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
       RowMainBinding rowBinding = RowMainBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowMainBinding binding;
        public ViewHolder(RowMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    void startActivity(int position){

        Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ViewReproductor.class);
        intent.putExtra("name","probando" + position);
        context.startActivity(intent);

    }
}
