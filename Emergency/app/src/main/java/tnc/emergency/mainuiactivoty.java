package tnc.emergency;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 12/17/2015.
 */
public class mainuiactivoty extends Activity {


    List<String> listPhoneNumber, listLatitude, listLongitude, listType, listFriendNumbers;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String ID;
    boolean status = false;
    boolean gpsStatus = false;
    GPSTracker gpsTracker;
    Double latitude, longitude;
    ImageView callPolice;
    Button smsFreinds, callHospital;
    String HospitalNumber="911", PoliceNumber="911";
    DbManipulation dbManipulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainui);
        gpsTracker = new GPSTracker(mainuiactivoty.this);
        callPolice = (ImageView) findViewById(R.id.callPolice);
        smsFreinds = (Button) findViewById(R.id.smsFreind);
        callHospital = (Button) findViewById(R.id.callHospital);
        listPhoneNumber = new ArrayList<>();
        listLatitude = new ArrayList<>();
        listLongitude = new ArrayList<>();
        listType = new ArrayList<>();
        listFriendNumbers = new ArrayList<>();
        dbManipulation = new DbManipulation();
        listFriendNumbers = dbManipulation.getPhoneNumber(mainuiactivoty.this);
        sharedPreferences = getSharedPreferences("Settings", 0);
        editor = sharedPreferences.edit();
        ID = sharedPreferences.getString("ID", "0");
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        if (CheckNetwork.isInternetAvailable(mainuiactivoty.this)) {
            new GetData().execute();
        } else {
            numbers();
        }


        callHospital.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(Intent.ACTION_CALL);

            @Override
            public void onClick(View view) {
                if (!sharedPreferences.getString("ID", "0").equals("0")) {
                    intent.setData(Uri.parse("tel:" + HospitalNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(mainuiactivoty.this, "Connect to Internet to get number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        callPolice.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(Intent.ACTION_CALL);

            @Override
            public void onClick(View view) {
                if (!sharedPreferences.getString("ID", "0").equals("0")) {
                    intent.setData(Uri.parse("tel:" + PoliceNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(mainuiactivoty.this, "Connect to Internet to get number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final String message = "Help me, I am at " + "http://www.maps.google.com/maps?q=" + latitude + "," + longitude;
        smsFreinds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPreferences.getBoolean("FriendList", false)) {
                    for (int i = 0; i < listFriendNumbers.size(); i++) {

                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(listFriendNumbers.get(i), null, message, null, null);
                    }
                } else {
                    startActivity(new Intent(mainuiactivoty.this, Configuration.class));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread t = new Thread() {
            long startTime = System.currentTimeMillis();

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("Location", "Updated");
                                if (latitude != 0.0) {
                                    latitude = gpsTracker.getLatitude();
                                    longitude = gpsTracker.getLongitude();
                                    gpsStatus = true;
                                } else {
                                    gpsStatus = false;
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };

        t.start();

    }

    public void numbers() {
        Log.d("ID", ID);
        if (!sharedPreferences.getString("ID", "0").equals("0")) {
            GetNumbers getNumbers = new GetNumbers(mainuiactivoty.this);
            HospitalNumber = getNumbers.getHospitalNumber(latitude, longitude);
            Log.d("zma HospitalNumber", HospitalNumber);
            PoliceNumber = getNumbers.getPoliceNumber(latitude, longitude);
            Log.d("zma PoliceNumber", PoliceNumber);
        }
    }

    public void afterRequest() {
        DbManipulation obj = new DbManipulation();
        for (int i = 0; i < listPhoneNumber.size(); i++) {
            obj.setData(mainuiactivoty.this, listLatitude.get(i), listLongitude.get(i), listType.get(i), listPhoneNumber.get(i));
            Log.d("zma Data", listLatitude.get(i) + listLongitude.get(i) + listType.get(i) + listPhoneNumber.get(i));
        }
        editor.putString("ID", ID);
        editor.commit();
        Log.d("zma ID", ID);
        numbers();

    }


    private class GetData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mainuiactivoty.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait while database is sync");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                UserFunctions userFunctions = new UserFunctions();
                JSONArray parent = userFunctions.getData(ID);
                if (parent.length() > 0) {
                    JSONObject temp = null;
                    for (int i = 0; i < parent.length(); i++) {
                        temp = parent.getJSONObject(i);
                        listLatitude.add(temp.getString("latitude"));
                        listLongitude.add(temp.getString("longitude"));
                        listPhoneNumber.add(temp.getString("phone"));
                        if(temp.getString("location_type").equals("Police Station")){
                            listType.add("Police");
                        }else{
                            listType.add(temp.getString("location_type"));
                        }

                    }
                    ID = temp.getString("location_id");
                    status = true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (status) {
                afterRequest();
            }
            progressDialog.dismiss();
        }
    }
}
