package tnc.emergency;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 12/8/2015.
 */
public class UserFunctions {

    private static String NumberURL = "http://alizaibilalkhan.com/map/APIs/api.return_location_on_lat_long.php";
    private static String DataURL = "http://alizaibilalkhan.com/map/APIs/api.backup_data_to_phone.php";
    private JSONParser jsonParser;

    public UserFunctions() {

        jsonParser = new JSONParser();
}

    public JSONArray getNumbers(String Latitude , String Longitude ) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        // params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("Latitude", Latitude));
        params.add(new BasicNameValuePair("Longitude", Longitude));
        JSONArray json = jsonParser.getJSONArrayFromUrl(NumberURL, params);
        // return json
        Log.e("JSON", json.toString());
        return json;
    }

    public JSONArray getData(String ID){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        // params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("ID", ID));
        JSONArray json = jsonParser.getJSONArrayFromUrl(DataURL, params);
        // return json
//       Log.e("JSON", json.toString());
        return json;
    }


}
