package com.hyperether.getgoing.repository.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hyperether.getgoing.repository.room.entity.DbRoute;

import java.util.List;

@Dao
public interface DbRouteDao {

    @Query("SELECT * FROM DbRoute")
    LiveData<List<DbRoute>> getAll();

    @Insert
    long insertRoute(DbRoute dbRoute);

    @Query("SELECT * FROM DbRoute WHERE id = :id")
    DbRoute getRouteById(long id);

    @Query("SELECT * FROM DbRoute WHERE id = :id")
    LiveData<DbRoute> getRouteByIdAsLiveData(long id);

    @Delete
    void deleteRoutes(DbRoute... routes);

    @Query("DELETE FROM DbRoute WHERE id = :id")
    void deleteRouteById(long id);

    @Query("SELECT * FROM DbRoute WHERE goal > 0 ORDER BY id DESC LIMIT 1")
    LiveData<DbRoute> getLatestRouteAsLiveData();

    @Query("SELECT * FROM DbRoute WHERE goal > 0 ORDER BY id DESC LIMIT 1")
    DbRoute getLatestRoute();

    @Update
    void updateRoute(DbRoute route);

}
