package com.learntodroid.ProductAuthentifierApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class Login extends AppCompatActivity {
    Button SignInBT;
    EditText EmailET, PasswordET;
    TextView forgotPassword, signUpET;

    LinearLayout forgotPass;
    //    private final String URL_Line = "http://localhost:1337/auth/local";
    private final String URL_Line = "http://192.168.43.86:1337/auth/local";
    private PreferenceHelper preferenceHelper;
    private RequestQueue rQueue;
    // Instantiate the cache
    private Cache cache;
    // Set up the network to use HttpURLConnection as the HTTP client.
    private Network network;

// Instantiate the RequestQueue with the cache and network.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        network = new BasicNetwork(new HurlStack());
        rQueue = new RequestQueue(cache, network);
        EmailET = findViewById(R.id.EmailEt);
        PasswordET = findViewById(R.id.PasswordEt);
        signUpET = findViewById(R.id.signUPBT);
        SignInBT = findViewById(R.id.signInBT);
        forgotPassword = findViewById(R.id.forgotpassword);
        preferenceHelper = new PreferenceHelper(this);

        forgotPassword.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            EditText mEmail = new EditText(Login.this);
            mEmail.setPadding(25, 0, 25, 20);
            mEmail.setHint("Enter email");
            mEmail.setMaxWidth(100);
            alert.setTitle("Recover Password");
            alert.setView(mEmail);
            alert.setPositiveButton("Submit", (dialogInterface, i) -> {
                String email = mEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    mEmail.setError("Can't be empty");
                } else {
                    if (!Patterns.EMAIL_ADDRESS.equals(email)) {
                        mEmail.setError("Enter a valid email address");
                    } else {
                        sendResetEmail(email);
                    }
                }
            });
            alert.show();
        });

        signUpET.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
            startActivity(intent);
        });

        SignInBT.setOnClickListener(view -> signInUser());
    }

    private void sendResetEmail(String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.POST, "reset url" + email, response -> {
            if (Objects.equals(response, RESULT_OK)) {
                Toast.makeText(this, "Reset Email sent", Toast.LENGTH_LONG).show();
            }

        }, Throwable::printStackTrace);
        requestQueue.add(req);
    }

    private void signInUser() {
        if(SignInBT.getText() == "Loading..") return;
        final String email = EmailET.getText().toString().trim();
        final String password = PasswordET.getText().toString().trim();
        Intent scanner = new Intent(getApplicationContext(), QRCodeImageAnalyzer.class);
        return;
       /* if (email.isEmpty()) {
            EmailET.setError("Kindly fill This");
            EmailET.setText("");
        } else if (password.isEmpty()) {
            PasswordET.setError("Password cant be Empty!");
            PasswordET.setText("");
        } else {
            //Start the queue
            rQueue.start();
            SignInBT.setText("Loading...");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Line,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(Login.this, "Wagwan",Toast.LENGTH_LONG).show();
                            Intent scanner = new Intent(getApplicationContext(), QRCodeImageAnalyzer.class);
                            //Start the queue
                            rQueue.stop();
                            SignInBT.setText("Sign In");
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String message = "Generic Error";
                            if(error instanceof TimeoutError || error instanceof NoConnectionError){
                                message = "Error: no connection. Check internet";
                                Log.e("Error!",message);
                            } if(error instanceof AuthFailureError){
                                message = "Error: Wrong credentials provided";
                                Log.e("Error!",message);
                            } if(error instanceof ServerError){
                                message = "Error: Server Error";
                                Log.e("Error!",message);
                            } if(error instanceof NetworkError){
                                message = "Error: Network Error";
                                Log.e("Error!",message);
                            } if(error instanceof ParseError){
                                message = "Error: Parse Error";
                                Log.e("Error!",message);
                            }
                           // error.printStackTrace();

                            //System.out.println(error.getMessage());
                            try {
                                Log.e("error",new String(error.networkResponse.data,"utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                            //Start the queue
                            rQueue.stop();
                            SignInBT.setText("Sign In");
                        }
                    });


            rQueue.add(stringRequest);
//                    response -> {
//                        rQueue.getCache().clear();
//                        Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
//                        parseData(response);
//                    },
            {
             *//*   @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };*//*
                rQueue = Volley.newRequestQueue(Login.this);
                rQueue.add(stringRequest);
            }
        }*/
    }

    private void parseData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                saveInfo(response);
                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void saveInfo(String response){
        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {

                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putName(dataobj.getString("names"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
