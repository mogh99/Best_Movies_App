package com.example.bestmovies.dataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    LiveData<List<FavoriteMovie>> loadAllFavoriteMovies();

    @Query("SELECT * FROM favorite")
    List<FavoriteMovie> loadAllFavoriteMoviesWithoutLiveData();

    @Insert
    void insertFavorite(FavoriteMovie favoriteMovie);

    @Delete
    void deleteFavorite(FavoriteMovie favoriteMovie);
}
