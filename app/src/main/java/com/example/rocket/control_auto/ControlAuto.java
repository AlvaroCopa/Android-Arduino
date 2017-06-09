package com.example.rocket.control_auto;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.io.IOException;
import java.util.UUID;

public class ControlAuto extends AppCompatActivity {

    Button iz,der,at,ad,st;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(Activity_Device.EXTRA_ADDRESS); //recivimos la mac address obtenida en la actividad anterior

        setContentView(R.layout.activity_control_auto);
        iz = (Button)findViewById(R.id.izquierda);
        der = (Button)findViewById(R.id.derecha);
        at = (Button)findViewById(R.id.atras);
        ad = (Button)findViewById(R.id.adelante);
        st = (Button)findViewById(R.id.stop);
        new ConnectBT().execute();
        iz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                izquierda();
            }
        });
        der.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                derecha();
            }
        });
        at.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                atras();
            }
        });
        ad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adelante();
            }
        });
        st.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                parar();
            }
        });
    }
    private void izquierda(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("3".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void derecha(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("5".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void adelante(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("2".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private  void atras(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("6".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private  void parar(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("4".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ControlAuto.this, "Conectando...", "Espere Porfavor!!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//conectamos al dispositivo y chequeamos si esta disponible
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Conexión Fallida");
                finish();
            }
            else
            {
                msg("Conectado");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
