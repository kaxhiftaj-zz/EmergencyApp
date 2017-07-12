package tnc.emergency;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class GetNumbers {
    Context context;

    List<String> listPhoneNumber, listLatitude, listLongitude;
    DbManipulation dbManipulation;

    public GetNumbers(Context context) {
        this.context = context;
        listPhoneNumber = new ArrayList<>();
        listLatitude = new ArrayList<>();
        listLongitude = new ArrayList<>();
        dbManipulation = new DbManipulation();

    }


    public String getHospitalNumber(double latitude, double longitude) {
        if (dbManipulation.getNumberCount(context, "Hospital")) {
            listLatitude = dbManipulation.getLatitude(context, "Hospital");
            listLongitude = dbManipulation.getLongitude(context, "Hospital");
            listPhoneNumber = dbManipulation.getPhone(context, "Hospital");
            double minDistance = Haversine.distance(latitude, longitude, Double.valueOf(listLatitude.get(0)), Double.valueOf(listLongitude.get(0)));
            String phonenumber = listPhoneNumber.get(0);
            double temp;
            for (int i = 1; i < listPhoneNumber.size(); i++) {
                temp = Haversine.distance(latitude, longitude, Double.valueOf(listLatitude.get(i)), Double.valueOf(listLongitude.get(i)));
                Log.d("zma temp",String.valueOf(temp));
                if (temp <= minDistance) {
                    phonenumber = listPhoneNumber.get(i);
                }
            }
            Log.d("zma hospital",phonenumber);
            return phonenumber;
        }
        return "911";
    }

    public String getPoliceNumber(double latitude, double longitude) {

        if (dbManipulation.getNumberCount(context, "Police")) {
            listLatitude = dbManipulation.getLatitude(context, "Police");
            listLongitude = dbManipulation.getLongitude(context, "Police");
            listPhoneNumber = dbManipulation.getPhone(context, "Police");
            double minDistance = Haversine.distance(latitude, longitude, Double.valueOf(listLatitude.get(0)), Double.valueOf(listLongitude.get(0)));
            String phonenumber = listPhoneNumber.get(0);;
            double temp;
            for (int i = 1; i < listPhoneNumber.size(); i++) {
                temp = Haversine.distance(latitude, longitude, Double.valueOf(listLatitude.get(i)), Double.valueOf(listLongitude.get(i)));
                if (temp <= minDistance) {
                    phonenumber = listPhoneNumber.get(i);
                }
            }
            Log.d("zma policenumber",phonenumber);
            return phonenumber;
        }
        return "911";
    }
}
