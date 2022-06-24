package com.example.david0926.templete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity{

    EditText salary;
    private Handler mHandler;
    private String ip = "192.168.98.49";
    private int port = 8080;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_login:
                salary = findViewById(R.id.salary);
                connect(salary.getText().toString());
//                password = findViewById(R.id.login_password);
//                Toast.makeText(this, "Hello, "+salary.getText().toString()+"!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
//            case R.id.btn_newaccount:
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                break;

        }
    }

    void connect(String salary_value){
        mHandler = new Handler();
        Thread checkUpdate = new Thread(){
            public void run(){
                try{
                    socket = new Socket(ip, port);
                    Log.w("서버 접속됨", "서버 접속됨");
                } catch (IOException e1){
                    Log.w("서버 접속못함", "서버 접속못함");
                    e1.printStackTrace();
                }

                try{
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                    dos.writeUTF(salary_value);
                } catch (IOException e){
                    e.printStackTrace();
                    Log.w("서버", "버퍼 생성 잘못됨");
                }
                Log.w("서버", "버퍼 생성 잘됨");

                try{
//                    String line = (String)dis.readUTF();
                    byte[] buf = new byte[100];
                    int read_byte = dis.read(buf);
                    String expenses = new String(buf, 0, read_byte);
//                    Log.w("서버에서 받아온 값", ""+line);
                    Log.w("서버에서 받아온 값", ""+expenses);
                } catch (Exception e){
                }
            }
        };
        checkUpdate.start();

    }
}
