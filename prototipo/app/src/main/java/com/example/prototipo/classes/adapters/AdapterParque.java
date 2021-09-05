package com.example.prototipo.classes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototipo.R;
import com.example.prototipo.classes.models.ModelParque;


import java.util.ArrayList;

public class AdapterParque extends RecyclerView.Adapter<AdapterParque.MyViewHolder>{
    Context context;
    ArrayList<ModelParque> pList;
    ArrayList<ModelParque> listFull;
    OnItemClickListener listener;

    public AdapterParque(ArrayList<ModelParque> pList, OnItemClickListener listener) {
        this.pList = pList;
        this.listener = listener;
    }

    public AdapterParque(Context context, ArrayList<ModelParque> pList, OnItemClickListener listener) {
        this.context = context;
        this.pList = pList;
        this.listener = listener;
        this.listFull = new ArrayList<>(pList);
    }


    public AdapterParque(Context context, ArrayList<ModelParque> pList) {
        this.context = context;
        this.pList = pList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.parque,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterParque.MyViewHolder holder, int position) {
        ModelParque model = pList.get(position);
        holder.nome.setText(model.getNome());
        holder.estado.setText(model.getEstado());
        holder.cidade.setText(model.getCidade());

        holder.itemView.setOnClickListener(view->{
            listener.onItemClick(pList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }
    public interface OnItemClickListener {
        void onItemClick(ModelParque mp);
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView nome, estado, cidade;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.valorNomeE);
            estado = itemView.findViewById(R.id.valorEstado);
            cidade = itemView.findViewById(R.id.valorTipoE);
        }
    }
}
