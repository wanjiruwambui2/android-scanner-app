package com.learntodroid.ProductAuthentifierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.learntodroid.ProductAuthentifierApp.MainActivity;
import com.learntodroid.ProductAuthentifierApp.R;

public class SignUpActivity extends AppCompatActivity {

    Button SignUpBT;
    EditText EmailET,PasswordET,cpassET,UnameET;
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
                final String email=EmailET.getText().toString().trim();
                final String password=PasswordET.getText().toString().trim();
                final String cpass=cpassET.getText().toString().trim();
                final String uname=UnameET.getText().toString().trim();

                if(uname.isEmpty()){
                    UnameET.setError("Kindly fill This");
                    UnameET.setText("");

                }else if(email.isEmpty()){

                    EmailET.setError("Kindly fill This");
                    EmailET.setText("");

                }else if(password.isEmpty()){

                    PasswordET.setError("Password cant be Empty!");
                    PasswordET.setText("");

                }else if(cpass.isEmpty()){
                    cpassET.setError("Password cant be Empty!");
                    cpassET.setText("");

                } else if(password != cpass){

                    PasswordET.setError("Password do not match!");
                    PasswordET.setError("Password do not match!");

                }else{
                    Intent intent=new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                }

            }
        });
    }
}
