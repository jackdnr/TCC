package com.example.prototipo.classes.adapters;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.prototipo.R;
import com.example.prototipo.classes.objetos.Elemento;
//import com.example.prototipo.classes.models.Elemento;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterElemento extends RecyclerView.Adapter<AdapterElemento.MyViewHolder> {
    Context context;
    ArrayList<Elemento> eList;
    AdapterElemento.OnItemClickListener listener;
    StorageReference reference;
    Bitmap img;

    public AdapterElemento(ArrayList<Elemento> eList, AdapterElemento.OnItemClickListener listener) {
        this.eList = eList;
        this.listener = listener;
    }

    public AdapterElemento(Context context, ArrayList<Elemento> eList, AdapterElemento.OnItemClickListener listener) {
        this.context = context;
        this.eList = eList;
        this.listener = listener;
    }

    public AdapterElemento(Context context, ArrayList<Elemento> eList) {
        this.context = context;
        this.eList = eList;
    }
    @GlideModule
    public static class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }


    @NonNull
    @Override
    public AdapterElemento.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.elemento,parent,false);
        return new AdapterElemento.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterElemento.MyViewHolder holder, int position) {
        Elemento model = eList.get(position);
        reference = FirebaseStorage.getInstance()
                .getReference()
                .child(model.getCaminhoFoto());
        holder.nome.setText(model.getNome());
        holder.tipo.setText(model.getTipo());

        final File localFile;
        try {
            localFile = File.createTempFile("foto","jpg");
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    img = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.foto.setImageBitmap(img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context,"Imagem não pôde ser carregada",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Glide.with(holder.foto.getContext())
//                .load(reference)
//                .into(holder.foto);

        holder.itemView.setOnClickListener(view->{
            listener.onItemClick(eList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }
    public interface OnItemClickListener {
        void onItemClick(Elemento mp);
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView nome, tipo;
        CircleImageView foto;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.valorNomeE);
            tipo = itemView.findViewById(R.id.valorTipoE);
            foto = itemView.findViewById(R.id.foto);
        }
    }
}

