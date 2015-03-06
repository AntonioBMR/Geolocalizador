package com.antonio.android.geolocalizador;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity{

private Button btnActualizar;
private ObjectContainer bd;
private ArrayList<Localizacion>datos;
private ListView lv;
private  Adaptador ad;
private TextView messageTextView;
private Localizacion localizacion;
private TextView messageTextView2;
/****************************************************************************************/
/**********************************ONCREATE***********************************/
/****************************************************************************************/

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            messageTextView = (TextView) findViewById(R.id.message_id);
            messageTextView2 = (TextView) findViewById(R.id.message_id2);
            localizacion=new Localizacion();
            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            MyLocationListener mlocListener = new MyLocationListener();
            mlocListener.setMainActivity(this);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    (LocationListener) mlocListener);
            messageTextView.setText(R.string.esperando);
            messageTextView2.setText("");
            open();
            lv=(ListView)findViewById(R.id.listView);
            datos = new ArrayList();
            leerDatos();
            ad=new Adaptador(this,R.layout.detalle,datos);
            lv.setAdapter(ad);
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String b = ("borrar");
                    String m = ("modificar");
                    String o = ("opciones");
                    String[] opc = new String[]{b, m};
                    final int posicion = position;
                    AlertDialog opciones = new AlertDialog.Builder(
                            MainActivity.this)
                            .setTitle(o)
                            .setItems(opc,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int selected) {
                                            if (selected == 0) {
                                                borrar(datos.get(posicion));
                                            } else if (selected == 1) {

                                                Calendar cal = new GregorianCalendar();
                                                Date date = cal.getTime();
                                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                                String fecha = df.format(date);

                                                Localizacion l1=new Localizacion();
                                                l1=getLocation();
                                                l1.setFecha(fecha);
                                                editar(datos.get(posicion),l1);
                                            }
                                        }
                                    }).create();
                    opciones.show();
                    return true;
                }
            });
            btnActualizar = (Button)findViewById(R.id.BtnActualizar);
            btnActualizar.setEnabled(false);
            btnActualizar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Calendar cal = new GregorianCalendar();
                    Date date = cal.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String fecha = df.format(date);
                    Localizacion l=new Localizacion();
                    l=getLocation();
                    l.setFecha(fecha);
                    grabar(l);
                }
            });
        }
    /****************************************************************************************/
/***********************************ABRIR BD**************************************/
    /****************************************************************************************/

    private void open(){
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) + "bd.db4o");
    }
/****************************************************************************************/
/*********************************PAUSAR BD********************************************/
/****************************************************************************************/

    @Override
    protected void onPause() {
        super.onPause();
        bd.close();
    }
/****************************************************************************************/
/*********************************CRUD BD********************************************/
/****************************************************************************************/

    public void grabar(Localizacion l){
        bd.store(l);
        //bd.commit();
        datos = new ArrayList();
        leerDatos();
        bd.close();
        open();
    }
    public void editar(Localizacion l,Localizacion l1){
        ObjectSet<Localizacion> locs =  bd.queryByExample(l);
        if (locs.hasNext()){
            System.out.println("entra ");
            Localizacion p = locs.next();
            System.out.println("entra "+p.toString());
            p.setAltitud(l1.getAltitud());
            p.setLocalidad(l1.getLocalidad());
            p.setLongitud(l1.getLongitud());
            p.setLocation(l1.getLocation());
            p.setCalle(l1.getCalle());
            p.setFecha(l1.getFecha());
            bd.store(p);
            bd.commit();
            datos=new ArrayList();
        }
        leerDatos();
    }
    public void borrar(Localizacion l){
        bd.delete(l);
        bd.commit();
        datos = new ArrayList();
        leerDatos();
    }
    public void leerDatos(){
        List<Localizacion> locs= bd.query(Localizacion.class);
        for(Localizacion t: locs){
            datos.add(t);
        }
        ad=new Adaptador(this,R.layout.detalle,datos);
        lv.setAdapter(ad);

    }

/****************************************************************************************/
/********************************SETANDGETLOCATION*************************************/
/****************************************************************************************/


    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    btnActualizar.setEnabled(true);
                    Address address = list.get(0);
                    messageTextView2.setText("La dirección actual es: \n"
                            + address.getAddressLine(0)+" "+address.getAddressLine(1)+""+address.getAddressLine(2));
                    localizacion.setLocalidad(address.getAddressLine(1) + "" + address.getAddressLine(2));
                    localizacion.setCalle(address.getAddressLine(0) + " ");
                    localizacion.setLongitud(loc.getLongitude() + "");
                    localizacion.setAltitud(loc.getAltitude() + "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Localizacion getLocation() {
        return this.localizacion;
    }
    /****************************************************************************************/
/**********************************ListenerLocation***************************************/
    /****************************************************************************************/

    public class MyLocationListener implements LocationListener {
        MainActivity mainActivity;
        public MainActivity getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();
            String Text = "La ubicación actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            messageTextView.setText(Text);
            this.mainActivity.setLocation(loc);
        }


        @Override
        public void onProviderDisabled(String provider) {
            messageTextView.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            messageTextView.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}