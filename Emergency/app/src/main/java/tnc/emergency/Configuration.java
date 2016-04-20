package tnc.emergency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by PC on 12/18/2015.
 */
public class Configuration extends Activity {

    ListView lvContacts;
    CustomListAdapter adapter;
    List<String> namesList, phoneNumberList;
    DbManipulation dbManipulation;
    Button btnSave;
    SharedPreferences sharedpreferencces;
    SharedPreferences.Editor editor;
    int counter = 0;
    HashMap<String, String> ContactList;
    Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        namesList = new ArrayList<>();
        phoneNumberList = new ArrayList<>();
        ContactList = new HashMap<String, String>();
        namesList.clear();
        phoneNumberList.clear();
        lvContacts = (ListView) findViewById(R.id.lvContacts);
        sharedpreferencces = getSharedPreferences("Settings", 0);
        editor = sharedpreferencces.edit();
        btnSave = (Button) findViewById(R.id.btnsave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == 5) {
                    onBackPressed();
                    editor.putBoolean("FriendList", true);
                    editor.commit();
                } else {
                    Toast.makeText(Configuration.this, "Select 5 contacts only", Toast.LENGTH_LONG).show();
                }

            }
        });

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (namesList.contains(name)) {

            } else if (phoneNumberList.contains(phoneNumber)) {

            } else {
                namesList.add(name);
                phoneNumberList.add(phoneNumber);
                ContactList.put(name, phoneNumber);
            }


            map = new TreeMap<String, String>(ContactList);
            namesList.clear();
            phoneNumberList.clear();
            namesList = new ArrayList<>(map.keySet());
            phoneNumberList = new ArrayList<>(map.values());


        }
        phones.close();

        adapter = new CustomListAdapter(this, namesList);
        lvContacts.setAdapter(adapter);
    }

    private class CustomListAdapter extends ArrayAdapter<String> {

        private Context context;
        private List<String> data;
        List<String> databaseNumbers;

        public CustomListAdapter(Context context, List<String> data) {
            super(context, R.layout.customlistview, data);
            this.context = context;
            this.data = data;
            dbManipulation = new DbManipulation();
            databaseNumbers = new ArrayList<>();
            databaseNumbers = dbManipulation.getPhoneNumber(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutinflater.inflate(R.layout.customlistview, parent, false);
            final TextView tvname = (TextView) v.findViewById(R.id.tvName);
            final ImageView ivCheck = (ImageView) v.findViewById(R.id.ivCheck);
            tvname.setText(data.get(position));
            //tvname.setText(map.get(position));
            ivCheck.bringToFront();
            ivCheck.setVisibility(View.INVISIBLE);
            if (dbManipulation.hasdata(context)) {
                if (databaseNumbers.contains(phoneNumberList.get(position))) {
                    ivCheck.setVisibility(View.VISIBLE);
                } else {
                    ivCheck.setVisibility(View.INVISIBLE);
                }
            }


            tvname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String temp = phoneNumberList.get(position);
                    if (counter >= 5) {
                        Toast.makeText(context, "you have already selected 5 contacts", Toast.LENGTH_SHORT).show();
                    } else {

                        if (databaseNumbers.contains(temp)) {
                            dbManipulation.dataDelete(context, temp);
                            ivCheck.setVisibility(View.INVISIBLE);
                            counter--;
                        } else {
                            dbManipulation.dataInsertion(context, "test", temp);
                            ivCheck.setVisibility(View.VISIBLE);
                            counter++;
                        }
                        databaseNumbers = dbManipulation.getPhoneNumber(context);
                    }
                }
            });


            return v;
        }
    }
}
