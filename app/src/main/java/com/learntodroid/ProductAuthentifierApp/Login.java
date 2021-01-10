package com.learntodroid.ProductAuthentifierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learntodroid.ProductAuthentifierApp.R;
import com.learntodroid.ProductAuthentifierApp.SignUpActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button signUpBT, SignInBT;
    EditText EmailET,PasswordET;
    private String URLline = "auth/local";
    private PreferenceHelper preferenceHelper;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailET=(EditText)findViewById(R.id.EmailEt);
        PasswordET=(EditText)findViewById(R.id.PasswordEt);
        signUpBT=(Button)findViewById(R.id.signUPBT);
        SignInBT=(Button)findViewById(R.id.signInBT);

        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        SignInBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInUser();
            }
        });
    }

    private void signInUser() {

        final String email=EmailET.getText().toString().trim();
        final String password=PasswordET.getText().toString().trim();

        if(email.isEmpty()){
            EmailET.setError("Kindly fill This");
            EmailET.setText("");
        }else if(password.isEmpty()){
            PasswordET.setError("Password cant be Empty!");
            PasswordET.setText("");
        }else{


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            rQueue.getCache().clear();
                            Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                            parseData(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("email",email);
                    params.put("password",password);

                    return params;
                }

            };

            rQueue = Volley.newRequestQueue(Login.this);
            rQueue.add(stringRequest);
        }


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
