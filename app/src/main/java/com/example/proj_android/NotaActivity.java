package com.example.proj_android;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import DB.Nota;
import DB.NotaViewModel;
import adapters.NotaListAdapter;

public class NotaActivity extends AppCompatActivity {

    private NotaViewModel mNotaViewModel;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_WORD_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_NOTA = "extra_nota_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.lista);
        final NotaListAdapter adapter = new NotaListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNotaViewModel = ViewModelProviders.of(this).get(NotaViewModel.class);

        mNotaViewModel.getAllNotas().observe(this, new Observer<List<Nota>>() {
            @Override
            public void onChanged(List<Nota> notas) {
                adapter.setNotas(notas);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotaActivity.this, NewNotaActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder viewHolder1) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int diretion) {
                        int position = viewHolder.getAdapterPosition();
                        Nota mnota = adapter.getNotaPosition(position);
                        Toast.makeText(NotaActivity.this, getResources().getString(R.string.apagar) +
                                mnota.getTitulo(), Toast.LENGTH_SHORT).show();

                        mNotaViewModel.deleteNota(mnota);
                    }
                });
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NotaListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Nota nota = adapter.getNotaPosition(position);
                launchUpdateNotaActivity(nota);
            }
        });
    }

    public void launchUpdateNotaActivity( Nota nota) {
        Intent intent = new Intent(this, NewNotaActivity.class);

        intent.putExtra(EXTRA_DATA_UPDATE_NOTA, nota.getTitulo());
        intent.putExtra(EXTRA_DATA_ID, nota.getId());

        String[] notaParams = {String.valueOf(nota.getId()), String.valueOf(nota.getTitulo()),
                String.valueOf(nota.getDescricao()), String.valueOf(nota.getTipoDescricao())};
        intent.putExtra("notaParams", notaParams);
        startActivityForResult(intent, UPDATE_WORD_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String[] nota = data.getStringArrayExtra(NewNotaActivity.EXTRA_REPLY);
            Integer id = Integer.valueOf(nota[0]);
            String titulo = nota[1];
            String descricao = nota[2];
            String tipo = nota[3];


            /*if(id != -1){
                mNotaViewModel.insert(new Nota(titulo,descricao,tipo));
            }*/

            Nota notaFinal = new Nota(titulo,descricao,tipo);
            mNotaViewModel.insert(notaFinal);

        } else if(requestCode == UPDATE_WORD_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK){
            String[] nota = data.getStringArrayExtra(NewNotaActivity.EXTRA_REPLY);
            Integer ide = Integer.valueOf(nota[0]);
            final String titulo = nota[1];
            final String descricao = nota[2];
            final String tipo = nota[3];

            Log.i("id", ide.toString());
            if(ide != -1){
                final LiveData<Nota> oldnota = mNotaViewModel.getNotaId(ide);
                oldnota.observe(this, new Observer<Nota>() {
                    @Override
                    public void onChanged(@Nullable Nota nota) {
                        if(nota != null){
                            nota.setTitulo(titulo);
                            nota.setDescricao(descricao);
                            nota.setTipoDescricao(tipo);
                            mNotaViewModel.updateNota(nota);
                            oldnota.removeObserver(this);
                        }
                    }
                });

            } else {
                Toast.makeText(this, getResources().getString(R.string.updatenao),
                        Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(R.string.notanao),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.deleteAll){
            Toast.makeText(this, getResources().getString(R.string.apagarnotas), Toast.LENGTH_SHORT).show();

            mNotaViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
