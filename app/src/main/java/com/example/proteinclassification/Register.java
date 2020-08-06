package com.example.proteinclassification;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Register extends AppCompatActivity {
    DatabaseHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // setContentView (R.layout.activity_signup);
        db = new DatabaseHelper(this);
        mTextUsername = (EditText)findViewById(R.id.em1);
        mTextPassword = (EditText)findViewById(R.id.ps1);
        mTextCnfPassword = (EditText)findViewById(R.id.pr3);
        mButtonRegister = (Button)findViewById(R.id.buttonw);
       // mTextViewLogin = (TextView)findViewById(R.id.textview_login);

        mButtonRegister.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();

                if(user.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(Register.this,"Fill empty fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (pwd.equals(cnf_pwd)) {
                        long val = db.addUser(user, pwd);
                        if (val > 0) {
                            Toast.makeText(Register.this, "You have registered", Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(Register.this, Congrats.class);
                            startActivity(moveToLogin);
                        } else {
                            Toast.makeText(Register.this, "Registeration Error", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Register.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

