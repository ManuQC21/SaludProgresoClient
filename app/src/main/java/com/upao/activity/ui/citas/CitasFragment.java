package com.upao.activity.ui.citas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.upao.R;
import com.upao.databinding.FragmentCitasBinding;

public class CitasFragment extends Fragment {

    private FragmentCitasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_citas,container,false);
    }

}