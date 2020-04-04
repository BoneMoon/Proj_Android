package com.example.proj_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.proj_android.NotaActivity.EXTRA_DATA_UPDATE_NOTA;

public class NewNotaActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY =
            "com.example.android.roomwordssample.REPLY";
    public static final String EXTRA_REPLY_ID =
            "com.android.example.roomwordssample.REPLY_ID";

    private EditText editTitulo;
    private  EditText editDesc;
    private  EditText editTipo;
    private Integer id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nota);
        String[] nota = getIntent().getStringArrayExtra("notaParams");

        final Bundle extras = getIntent().getExtras();

        editTitulo = findViewById(R.id.editTitulo);
        editDesc = findViewById(R.id.editDesc);
        editTipo = findViewById(R.id.editTipo);
        id = -1;

        if (extras != null) {
            String notas = extras.getString(EXTRA_DATA_UPDATE_NOTA, "");
            if (!notas.isEmpty()) {
                id = Integer.valueOf(nota[0]);
                editTitulo.setText(nota[1]);
                editDesc.setText(nota[2]);
                editTipo.setText(nota[3]);
            }
        }

        final Button btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent replyIntent = new Intent();

                if(TextUtils.isEmpty(editTitulo.getText()) &&
                        TextUtils.isEmpty(editDesc.getText()) &&
                        TextUtils.isEmpty(editTipo.getText())){

                    setResult(RESULT_CANCELED, replyIntent);
                } else {


                    String titulo = editTitulo.getText().toString();
                    String descricao = editDesc.getText().toString();
                    String tipo = editTipo.getText().toString();
                    String[] nota = {id.toString(),titulo,descricao, tipo};

                    replyIntent.putExtra(EXTRA_REPLY, nota);
                    setResult(RESULT_OK, replyIntent);
                }

                finish();
            }
        });
    }
}
