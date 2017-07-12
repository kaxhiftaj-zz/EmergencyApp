package tnc.emergency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnsendsms ;
    List<String> phonenumber ;
    DbManipulation dbManipulation ;
    Button btncallpolice,btnCallHospital;
    SharedPreferences sharedpreferencces ;
    String policeNumber,hospitalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phonenumber = new ArrayList<>();
        dbManipulation = new DbManipulation();
            btncallpolice = (Button)findViewById(R.id.btncallpolice);
            btnCallHospital = (Button)findViewById(R.id.btncallhospital);
        sharedpreferencces = getSharedPreferences("Settings", 0);
        policeNumber = sharedpreferencces.getString("police", "999");
        hospitalNumber = sharedpreferencces.getString("hospital", "999");
        phonenumber = dbManipulation.getPhoneNumber(MainActivity.this);
        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
        String latitude = String.valueOf(gpsTracker.getLatitude());
        String longitude = String.valueOf(gpsTracker.getLongitude());
        final String message = "Help me, I am at " + "https://www.google.com/maps/@" + latitude + "," + longitude;
        btnsendsms = (Button)findViewById(R.id.btnsendsms);
        btnsendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i= 0 ; i<phonenumber.size(); i++){

                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(phonenumber.get(i), null, message, null, null);
                }
            }
        });

        btncallpolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:"+ policeNumber;
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(dialIntent);
            }
        });
        btnCallHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:"+ hospitalNumber;
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(dialIntent);
            }
        });
    }
}
