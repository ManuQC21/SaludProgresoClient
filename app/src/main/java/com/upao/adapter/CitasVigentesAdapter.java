package com.upao.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.upao.R;
import com.upao.ViewHolder.CitasVigentesViewHolder;
import com.upao.entity.service.Citas;
import com.upao.viewmodel.CitasViewModel;

import java.util.ArrayList;
import java.util.List;

public class CitasVigentesAdapter extends RecyclerView.Adapter<CitasVigentesViewHolder> {

    private List<Citas> listaCitas;
    private CitasViewModel citasViewModel;

    public CitasVigentesAdapter(CitasViewModel citasViewModel) {
        this.listaCitas = new ArrayList<>();
        this.citasViewModel = citasViewModel;
    }

    @NonNull
    @Override
    public CitasVigentesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_citas, parent, false);
        return new CitasVigentesViewHolder(view, this, citasViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull CitasVigentesViewHolder holder, int position) {
        Citas cita = listaCitas.get(position);
        holder.bind(cita);
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    public void updateCitas(List<Citas> nuevasCitas) {
        this.listaCitas = nuevasCitas;
        notifyDataSetChanged();
    }

    public List<Citas> getListaCitas() {
        return listaCitas;
    }
}
