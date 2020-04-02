package DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Nota.class}, version = 1, exportSchema = false)
public abstract class NotaRoomDatabase extends RoomDatabase {

    public abstract NotaDAO notaDAO();
    private static NotaRoomDatabase INSTANCE;

    public static NotaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NotaRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Criação da base de dados
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotaRoomDatabase.class, "nota_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static NotaRoomDatabase.Callback sRoomDatabaseCallback =
            new NotaRoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NotaDAO mnotaDAO;

        String[] titulos = {"Titulo 1", "Titulo 2", "Titulo 3"};
        String[] descricoes = {"Descrição 1", "Descrição 2", "Descrição 3"};
        String[] tipo = {"Tipo 1", "Tipo 2", "Tipo 3"};

        PopulateDbAsync(NotaRoomDatabase db){
            mnotaDAO = db.notaDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(mnotaDAO.getAnyNota().length < 1){
                for(int i = 0; i <= titulos.length - 1; i++){
                    Nota nota = new Nota(titulos[i], descricoes[i], tipo[i]);
                    mnotaDAO.insert(nota);
                }
            }

            return null;
        }
    }

    }
