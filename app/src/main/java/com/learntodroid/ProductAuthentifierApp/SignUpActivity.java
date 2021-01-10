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
import com.learntodroid.ProductAuthentifierApp.SignUpActivity;
import com.learntodroid.ProductAuthentifierApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button SignUpBT;
    EditText EmailET,PasswordET,cpassET,UnameET;


    private PreferenceHelper preferenceHelper;
    private String registerURL = "auth/local/register";
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        UnameET=(EditText) findViewById(R.id.UnameEt);
        EmailET=(EditText)findViewById(R.id.EmailEt);
        PasswordET=(EditText)findViewById(R.id.PasswordEt);
        cpassET=(EditText)findViewById(R.id.cPasswordEt);

        SignUpBT=(Button)findViewById(R.id.signUPBT);
        

        SignUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    registerUser();


            }
        });
    }

    private void registerUser() {

        final String email = EmailET.getText().toString().trim();
        final String password = PasswordET.getText().toString().trim();
        final String cpass = cpassET.getText().toString().trim();
        final String uname = UnameET.getText().toString().trim();

        if (uname.isEmpty()) {
            UnameET.setError("Kindly fill This");
            UnameET.setText("");

        } else if (email.isEmpty()) {

            EmailET.setError("Kindly fill This");
            EmailET.setText("");

        } else if (password.isEmpty()) {

            PasswordET.setError("Password cant be Empty!");
            PasswordET.setText("");

        } else if (cpass.isEmpty()) {
            cpassET.setError("Password cant be Empty!");
            cpassET.setText("");

        } else if (password != cpass) {

            PasswordET.setError("Password do not match!");
            PasswordET.setError("Password do not match!");

        } else {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, registerURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            rQueue.getCache().clear();

                            Toast.makeText(SignUpActivity.this,response, Toast.LENGTH_LONG).show();

                            try {

                                parseData(response);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("username",uname);
                    params.put("email",email);
                    params.put("password",password);

                    return params;
                }

            };

            rQueue = Volley.newRequestQueue(SignUpActivity.this);
            rQueue.add(stringRequest);

        }


    }





    private void parseData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("true")){

            saveInfo(response);

            Toast.makeText(SignUpActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }else {

            Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
