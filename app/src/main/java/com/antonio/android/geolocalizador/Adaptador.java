package com.antonio.android.geolocalizador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Antonio on 06/03/2015.
 */
public class Adaptador extends ArrayAdapter<Localizacion> {
    private Context contexto;
    private ArrayList<Localizacion> lista;
    private int recurso;
    private static LayoutInflater i;
    private ViewHolder vh;

    static class ViewHolder{
        public TextView tvLatitud, tvlongitud, tvlocalidad, tvfecha;
        public int posicion;
    }
    public Adaptador(Context context, int resource, ArrayList<Localizacion> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvLatitud = (TextView) convertView.findViewById(R.id.tvlat);
            vh.tvlongitud = (TextView) convertView.findViewById(R.id.tvlong);
            vh.tvlocalidad =(TextView)convertView.findViewById(R.id.tvloc);
            vh.tvfecha=(TextView)convertView.findViewById(R.id.tvfecha);
            //vh.tv=(TextView)convertView.findViewById(R.id.tvProfD);
            convertView.setTag(vh);
        }else{
            //comentario
            vh=(ViewHolder)convertView.getTag();
        }
        vh.posicion=position;
        Localizacion l;
        l=lista.get(position);
        vh.tvLatitud.setText("latitud: "+l.getAltitud()+"");
        vh.tvlongitud.setText("longitud: "+l.getLongitud()+"");
        vh.tvlocalidad.setText(l.getLocalidad()+" "+l.getCalle());
        vh.tvfecha.setText(l.getFecha());
        return convertView;
    }

}