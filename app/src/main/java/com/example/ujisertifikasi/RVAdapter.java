package com.example.ujisertifikasi;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolderClass>{

    ArrayList<ModelClass> objectModelClassList;

    public RVAdapter(ArrayList<ModelClass> objectModelClass) {
        this.objectModelClassList = objectModelClass;
    }

    @NonNull
    @Override
    public RVViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RVViewHolderClass(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVViewHolderClass holder, int position) {
        ModelClass objectModelClass = objectModelClassList.get(position);
        holder.nama.setText("Nama: "+objectModelClass.getNama());
        holder.image.setImageBitmap(objectModelClass.getImage());
        holder.jenisKelamin.setText("Jenis Kelamin: "+objectModelClass.getJenisKelamin());
        holder.alamat.setText("Alamat: "+objectModelClass.getAlamat());
        holder.latitude.setText("Latitude: "+ objectModelClass.getLatitude());
        holder.longitude.setText("Longitude: "+ objectModelClass.getLongitude());
    }

    @Override
    public int getItemCount() {
        return objectModelClassList.size();
    }

    public static class RVViewHolderClass extends RecyclerView.ViewHolder{

        TextView nama;
        ImageView image;
        TextView jenisKelamin;
        TextView alamat;
        TextView latitude;
        TextView longitude;
        Button delete;
        public RVViewHolderClass(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.sr_nameDetail);
            image = itemView.findViewById(R.id.sr_imageDetail);
            jenisKelamin = itemView.findViewById(R.id.sr_jenisKelaminDetail);
            alamat = itemView.findViewById(R.id.sr_alamatDetail);
            latitude = itemView.findViewById(R.id.sr_latitudeDetail);
            longitude = itemView.findViewById(R.id.sr_longitudeDetail);
        }
    }
}