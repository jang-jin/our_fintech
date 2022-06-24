package com.example.david0926.templete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity{

    EditText id, password, nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_register:
                nickname = findViewById(R.id.reg_nickname);
                id = findViewById(R.id.reg_id);
                password = findViewById(R.id.reg_password);
                //savedinst

                Toast.makeText(this, "Welcome, "+id.getText().toString()+"!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }




    }
}
