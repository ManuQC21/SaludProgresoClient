package com.upao.activity.ui.MisCitas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.upao.R;
public class MisCitasFragment extends Fragment {
    private TabLayout tabLayoutCitas;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_citas, container, false);

        tabLayoutCitas = view.findViewById(R.id.tabLayoutCitas);

        cargarFragmento(new CitasVigentesFragment());

        tabLayoutCitas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0: // Citas Vigentes
                        fragment = new CitasVigentesFragment();
                        break;
                    case 1: // Citas Vencidas
                        fragment = new CitasVencidasFragment();
                        break;
                }
                cargarFragmento(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No necesitamos hacer nada aquí
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No necesitamos hacer nada aquí
            }
        });

        return view;
    }

    private void cargarFragmento(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
