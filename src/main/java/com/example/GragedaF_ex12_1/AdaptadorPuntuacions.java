package com.example.GragedaF_ex12_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class AdaptadorPuntuacions extends RecyclerView.Adapter<AdaptadorPuntuacions.ViewHolder> {
    private LayoutInflater inflador;
    private Vector<String> llista;
    private int cont=0;
    private Context context;
    protected View.OnClickListener onClickListener;

    public AdaptadorPuntuacions (Context context, Vector<String> llista){
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.llista = llista;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titol, subtitol;
        public ImageView icono;

        public ViewHolder(View itemView){
            super(itemView);
            titol = (TextView) itemView.findViewById(R.id.titol);
            subtitol = (TextView) itemView.findViewById(R.id.subtitol);
            icono = (ImageView) itemView.findViewById(R.id.icono);
        }
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = inflador.inflate(R.layout.element_puntuacio,parent,false);
        //Toast.makeText(context,"Cont "+(++cont),Toast.LENGTH_SHORT).show();
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        holder.titol.setText(llista.get(position));
        //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
        switch (Math.round((float)Math.random()*3)){
            case 0:
                holder.icono.setImageResource(R.drawable.ic_asteroid__1_);
                break;
            case 1:
                holder.icono.setImageResource(R.drawable.ic_asteroid__2_);
                break;
            case 2:
                holder.icono.setImageResource(R.drawable.ic_asteroid__3_);
                break;
        }
    }

    @Override
    public int getItemCount(){
        return llista.size();
    }
}
