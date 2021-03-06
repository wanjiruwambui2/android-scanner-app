package com.learntodroid.ProductAuthentifierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button SignUpBT;
    EditText EmailET, PasswordET, cPassET, uNameET;


    private PreferenceHelper preferenceHelper;
//    private final String registerURL = "http://localhost:1337/auth/local/register";
    private final String registerURL = "http://192.168.208.89:8080/auth/local/register";
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        uNameET = (EditText) findViewById(R.id.UnameEt);
        EmailET = (EditText) findViewById(R.id.EmailEt);
        PasswordET = (EditText) findViewById(R.id.PasswordEt);
        cPassET = (EditText) findViewById(R.id.cPasswordEt);
        SignUpBT = (Button) findViewById(R.id.signUPBT);
        preferenceHelper = new PreferenceHelper(SignUpActivity.this);
        SignUpBT.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        final String email = EmailET.getText().toString().trim();
        final String password = PasswordET.getText().toString().trim();
        final String cPass = cPassET.getText().toString().trim();
        final String uName = uNameET.getText().toString().trim();

        if (uName.isEmpty()) {
            uNameET.setError("Kindly fill This");
            uNameET.setText("");
        } else if (email.isEmpty()) {
            EmailET.setError("Kindly fill This");
            EmailET.setText("");
        } else if (password.isEmpty()) {
            PasswordET.setError("Password cant be Empty!");
            PasswordET.setText("");
        } else if (cPass.isEmpty()) {
            cPassET.setError("Password cant be Empty!");
            cPassET.setText("");

        } else if (!password.equals(cPass)) {
            PasswordET.setError("Password do not match!");
            PasswordET.setError("Password do not match!");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, registerURL,
                    response -> {
                        rQueue.getCache().clear();
                        Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            parseData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    error -> Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", uName);
                    params.put("email", email);
                    params.put("password", password);

                    return params;
                }

            };
            rQueue = Volley.newRequestQueue(SignUpActivity.this);
            rQueue.add(stringRequest);

        }
    }

    private void parseData(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("true")) {

            saveInfo(response);

            Toast.makeText(SignUpActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        } else {

            Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }


    private void saveInfo(String response) {
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
