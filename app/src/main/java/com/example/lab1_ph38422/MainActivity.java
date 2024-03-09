package com.example.lab1_ph38422;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edtEmail, edtPass;
    Button btnSave;
    private FirebaseAuth mAuth;

    TextView txtSignUp, txtForgot;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // neu user da dang nhap vao tu phien truoc thi su dung user luon
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnSave = findViewById(R.id.btnLogin);
        txtSignUp = findViewById(R.id.txtSignUp);
        txtForgot = findViewById(R.id.txtForgot);

        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        if (intent!=null) {
            Bundle ex = intent.getExtras();
            if (ex!=null){
                edtEmail.setText(ex.getString("email"));
                edtPass.setText(ex.getString("password"));
            }}
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();
                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(MainActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(MainActivity.this, "Đăng Nhập Thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,Home.class);
                                    startActivity(intent);

                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Sai Tài Khoản Hoặc Mật khẩu!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(MainActivity.this, SignUp.class);
                startActivity(signup);
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(MainActivity.this, Forgot.class);
                startActivity(forgot);
            }
        });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
}