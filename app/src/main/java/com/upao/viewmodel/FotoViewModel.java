package com.upao.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.upao.entity.GenericResponse;
import com.upao.entity.service.Foto;
import com.upao.repository.FotoRepository;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FotoViewModel extends AndroidViewModel {
    private final FotoRepository repository;

    public FotoViewModel(@NonNull Application application) {
        super(application);
        this.repository = FotoRepository.getInstance();
    }
    public LiveData<GenericResponse<Foto>> save(MultipartBody.Part part, RequestBody requestBody){
        return this.repository.savePhoto(part, requestBody);
    }
}
