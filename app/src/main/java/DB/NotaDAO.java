package DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Nota nota);

    @Query("DELETE FROM Nota")
    void deleteAll();

    @Delete
    void deleteNota(Nota nota);

    @Query("SELECT * FROM Nota ORDER BY titulo ASC")
    LiveData<List<Nota>> getAllNotas();

    @Query("SELECT * FROM Nota")
    Nota[] getAnyNota();

    @Update
    void update(Nota... nota);

    @Query("SELECT * FROM  Nota WHERE id = :id")
    LiveData<Nota>nota(int id);
}
