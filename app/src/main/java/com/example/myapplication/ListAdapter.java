package com.example.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import java.util.List;

public class ListAdapter extends ArrayAdapter {

    private Activity mContext;
    List<Palabras>palabrasList;

    public ListAdapter(Activity mContext, List<Palabras> palabrasList){
        super(mContext, R.layout.list_item,palabrasList);
        this.mContext = mContext;
        this.palabrasList = palabrasList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_item,null,true);

        TextView txpais = listItemView.findViewById(R.id.txpais);
        TextView txpalabra = listItemView.findViewById(R.id.txpalabra);
        TextView txsignificado = listItemView.findViewById(R.id.txsignificado);

        Palabras palabras = palabrasList.get(position);

        txpais.setText(palabras.getPais());
        txpalabra.setText(palabras.getPalabra());
        txsignificado.setText(palabras.getSignificado());

        return listItemView;

    }
}
