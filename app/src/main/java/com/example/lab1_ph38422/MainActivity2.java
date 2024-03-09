package com.example.lab1_ph38422;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    private String mID;

    private TextInputEditText otp, sdt;
    private Button btnGetOtp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        sdt = findViewById(R.id.txtphone);
        otp = findViewById(R.id.txtotp);
        btnGetOtp = findViewById(R.id.btnotp);
        btnLogin = findViewById(R.id.btndangnhap);
        mAuth = FirebaseAuth.getInstance();

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //khi số điện thoại đăng nhập của bạn nằm trong máy bạn thực hiện getOTP
                //có thể lập tức lấy mã OTP và setText lên ô nhập otp mà không cần phải nhập
                otp.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //khi fail chạy hàm này
                Log.e("Firebase Verification", "onVerificationFailed: " + e.getMessage());

                Toast.makeText(MainActivity2.this, "Lỗi rồi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verifycationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //hàm này sẽ được chạy khi otp gửi thành công, ta sẽ lấy verificationID
                mID = verifycationId;
            }
        };
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SDT = sdt.getText().toString();
                if(!SDT.isEmpty()){
                    getOTP(SDT);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP = otp.getText().toString();
                if(!OTP.isEmpty()){
                    verifyOTP(OTP);
                }
            }
        });
    }
    private void getOTP(String phoneNumber) {
        if (!phoneNumber.isEmpty()) {
            // Tiến hành lấy OTP
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber("+84" + phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(mCallBack)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else {
            // Xử lý trường hợp số điện thoại trống rỗng
            Toast.makeText(MainActivity2.this, "Nhập số điện thoại trước", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOTP(String code) {
        if (!code.isEmpty()) {
            // Tiến hành xác minh OTP
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mID, code);
            signInWithPhone(credential);
        } else {
            // Xử lý trường hợp mã OTP trống rỗng
            Toast.makeText(MainActivity2.this, "Nhập mã OTP trước", Toast.LENGTH_SHORT).show();
        }
    }


    private void signInWithPhone(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Thành công
                            Toast.makeText(MainActivity2.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        } else {
                            // Thất bại
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // Mã xác minh không hợp lệ
                                Toast.makeText(MainActivity2.this, "Mã xác minh không hợp lệ", Toast.LENGTH_SHORT).show();
                            } else {
                                // Xử lý các trường hợp thất bại khác nếu cần
                                Toast.makeText(MainActivity2.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }
}