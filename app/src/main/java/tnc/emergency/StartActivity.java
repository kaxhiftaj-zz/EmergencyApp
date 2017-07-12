package tnc.emergency;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    Button selectcontacts;
    ListView lvcontact;
    List<String> namelist, numberlist;
    String phoneName, phoneNumber;
    // ArrayAdapter<String> adapter;
    CustomListAdapter adapter;
    Button btnsave;
    DbManipulation dbManipulation;
    GPSTracker gpstracker ;
    String latitude ;
    String longitude ;
    String policeNumber;
    String hospitalNumber;
    SharedPreferences  sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String hnumbers  = "Number";
    public static final String pnumber   =  "Number1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sharedPreferences = getSharedPreferences("Settings", 0);
        editor = sharedPreferences.edit();
        lvcontact = (ListView) findViewById(R.id.lvContacts);
        //selectcontacts = (Button) findViewById(R.id.btncontacts);
        btnsave = (Button) findViewById(R.id.btnsave);

        dbManipulation = new DbManipulation();
        namelist = new ArrayList<>();
        numberlist = new ArrayList<>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namelist);
        //if(namelist.size() > 0) {
        adapter = new CustomListAdapter(this, namelist);
        lvcontact.setAdapter(adapter);
        //}

        selectcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                int request_Code = 0;
                startActivityForResult(i, request_Code);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < namelist.size(); i++) {
                    dbManipulation.dataInsertion(StartActivity.this, namelist.get(i), numberlist.get(i));

                    if (CheckNetwork.isInternetAvailable(StartActivity.this)) {
                        gpstracker = new GPSTracker(StartActivity.this);
                        Location location = gpstracker.getLocation();
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                        new GetNumbers().execute();
                    }
                        else{
                            Toast.makeText(StartActivity.this , "Internet Connection isn't available " , Toast.LENGTH_SHORT).show();
                        }
                    }
                    //startActivity(new Intent(StartActivity.this , MapsActivity.class));
                dbManipulation.dataSelect(StartActivity.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int RESULT_PICK_CONTACT = 0;
        if (requestCode == RESULT_PICK_CONTACT && resultCode == RESULT_OK) {

            Uri dataUri = data.getData();
            Cursor contacts = managedQuery(dataUri, null, null, null, null);
            if (contacts.moveToFirst()) {
                String name;
                int nameColumn = contacts
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                name = contacts.getString(nameColumn);
                Cursor phones = getContentResolver().query(dataUri, null, null,
                        null, null);
                if (phones.moveToFirst()) {
                    phoneName = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNumber = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                }
            }
            if (checklist(phoneNumber)) {
                namelist.add(phoneName);
                numberlist.add(phoneNumber);
                adapter.notifyDataSetChanged();
                // }
            } else if (!checklist(phoneNumber)) {
                Startprocess();
                Toast.makeText(getApplicationContext(),
                        "Contact already exists", Toast.LENGTH_LONG).show();

            }
        }
    }


    public void Startprocess() {
        Intent i = new Intent(android.content.Intent.ACTION_PICK);
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        int request_Code = 0;
        startActivityForResult(i, request_Code);
    }

    private boolean checklist(String phoneNumber) {
        if (numberlist.contains(phoneNumber)) {
            return false;
        }
//        DbManipulation datainsertion = new DbManipulation();
//        boolean flag = datainsertion.checkdatabase(getApplicationContext(),
//                phoneName);

        return true;
    }






    private class CustomListAdapter extends ArrayAdapter<String> {

        private Context context;
        private List<String> data;

        public CustomListAdapter(Context context, List<String> data) {
            super(context, R.layout.customlistview, data);
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutinflater.inflate(R.layout.customlistview, parent, false);
            final TextView tvname = (TextView) v.findViewById(R.id.tvName);
            tvname.setText(data.get(position));
            return v;
        }
    }


    private class GetNumbers extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StartActivity.this);
            progressDialog.setMessage("Please wait . . . ");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject jsonObject1,jsonObject2;
            UserFunctions userFunctions = new UserFunctions();
            JSONArray jsonArray = userFunctions.getNumbers(latitude, longitude);
            try {
                 jsonObject1 = jsonArray.getJSONObject(0);
                jsonObject2 = jsonArray.getJSONObject(1);
                hospitalNumber = jsonObject1.getString("phone");
                policeNumber = jsonObject2.getString("phone");
                Log.d("police",policeNumber);
                Log.d("hospital",hospitalNumber);

                editor.putString("police", policeNumber);
                editor.putString("hospital",hospitalNumber);
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
               CallActivity();
        }
    }

    public void CallActivity(){
        startActivity(new Intent(StartActivity.this , MainActivity.class));
    }
}