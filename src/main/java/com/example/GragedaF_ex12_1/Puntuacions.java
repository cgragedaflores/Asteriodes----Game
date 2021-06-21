package com.example.GragedaF_ex12_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Puntuacions extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorPuntuacions adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuacions);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adaptador = new AdaptadorPuntuacions(this, MainActivity.magatzem.llistaPuntuacions(10));
        recyclerView.setAdapter(adaptador);

        adaptador.setOnItemClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int pos = recyclerView.getChildAdapterPosition(v);
                String s = MainActivity.magatzem.llistaPuntuacions(10).get(pos);
                Toast.makeText(Puntuacions.this,"Selecció: "+pos+" - "+s,Toast.LENGTH_LONG).show();
            }
        });
    }
}
