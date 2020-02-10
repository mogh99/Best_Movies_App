package com.example.bestmovies.dataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteMovie>> favListLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        DataBase dataBase = DataBase.getInstance(this.getApplication());
        favListLiveData = dataBase.favoriteDao().loadAllFavoriteMovies();
    }

    public LiveData<List<FavoriteMovie>> getFavListLiveData() {
        return favListLiveData;
    }
}
