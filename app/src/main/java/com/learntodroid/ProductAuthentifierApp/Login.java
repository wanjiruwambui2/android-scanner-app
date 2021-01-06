package com.learntodroid.ProductAuthentifierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learntodroid.ProductAuthentifierApp.R;
import com.learntodroid.ProductAuthentifierApp.SignUpActivity;

public class Login extends AppCompatActivity {
    Button signUpBT, SignInBT;
    EditText EmailET,PasswordET;

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
                final String email=EmailET.getText().toString().trim();
                final String password=PasswordET.getText().toString().trim();

                if(email.isEmpty()){
                    EmailET.setError("Kindly fill This");
                    EmailET.setText("");
                }else if(password.isEmpty()){
                    PasswordET.setError("Password cant be Empty!");
                    PasswordET.setText("");
                }else{

                    Toast.makeText(getBaseContext(),"Signed in",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
