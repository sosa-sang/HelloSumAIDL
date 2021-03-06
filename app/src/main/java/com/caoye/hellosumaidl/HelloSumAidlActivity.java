package com.caoye.hellosumaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HelloSumAidlActivity extends AppCompatActivity {

    IAdditionService service;
    AdditionServiceConnection connection;
    Button buttonCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_sum_aidl);

        initService();

        buttonCalc = (Button)findViewById(R.id.buttonCalc);
        buttonCalc.setOnClickListener(new View.OnClickListener() {
            TextView result = (TextView)findViewById(R.id.result);
            EditText value1 = (EditText)findViewById(R.id.value1);
            EditText value2 = (EditText)findViewById(R.id.value2);

            @Override
            public void onClick(View v) {
                int v1, v2, res = -1;
                v1 = Integer.parseInt(value1.getText().toString());
                v2 = Integer.parseInt(value2.getText().toString());

                try {
                    res = service.add(v1, v2);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                result.setText(Integer.valueOf(res).toString());
            }
        });
    }

    /*
     * This inner class is used to connect to the service
     */
    class AdditionServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IAdditionService.Stub.asInterface((IBinder)boundService);
            Toast.makeText(HelloSumAidlActivity.this, "Service connected", Toast.LENGTH_LONG).show();
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Toast.makeText(HelloSumAidlActivity.this, "Service disconnected", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * This function connects the Activity to the service
     */
    private void initService() {
        connection = new AdditionServiceConnection();
        Intent i = new Intent();
        i.setClassName("com.caoye.hellosumaidl", com.caoye.hellosumaidl.AdditionService.class.getName());
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    /*
     * This function disconnects the Activity from the service
     */
    private void releaseService() {
        unbindService(connection);
        connection = null;
    }
}
