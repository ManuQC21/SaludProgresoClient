package com.upao.activity.ui;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.List;

public class CustomAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> originalProvincias;
    private List<String> provincias;
    private String selectedDepartamento;

    public CustomAutoCompleteAdapter(Context context, int resource, List<String> provincias) {
        super(context, resource, provincias);
        this.provincias = new ArrayList<>(provincias);
        this.originalProvincias = new ArrayList<>(provincias);
        this.selectedDepartamento = "";
    }

    public void setSelectedDepartamento(String departamento) {
        selectedDepartamento = departamento;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<String> filteredProvincias = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredProvincias.addAll(originalProvincias);
                } else {
                    for (String provincia : originalProvincias) {
                        if (provincia.contains("dept=\"" + selectedDepartamento + "\"") && provincia.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredProvincias.add(provincia);
                        }
                    }
                }

                filterResults.values = filteredProvincias;
                filterResults.count = filteredProvincias.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                provincias.clear();
                provincias.addAll((List<String>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
