package DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Nota")
public class Nota {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "titulo")
    private String titulo;

    @NonNull
    @ColumnInfo(name = "descricao")
    private String descricao;

    @NonNull
    @ColumnInfo(name = "tipoDescricao")
    private String tipoDescricao;

    public Nota(@NonNull String titulo, @NonNull String descricao,
                @NonNull String tipoDescricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoDescricao = tipoDescricao;
    }

    public String getTitulo(){
        return this.titulo;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public String getTipoDescricao(){
        return this.tipoDescricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(@NonNull String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(@NonNull String descricao) {
        this.descricao = descricao;
    }

    public void setTipoDescricao(@NonNull String tipoDescricao) {
        this.tipoDescricao = tipoDescricao;
    }
}
