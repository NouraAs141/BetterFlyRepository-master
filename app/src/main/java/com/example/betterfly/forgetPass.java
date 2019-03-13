package com.example.betterfly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPass extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail;

    private Button btnReset;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_pass);

        inputEmail = findViewById(R.id.editTextEmail);

        btnReset = findViewById(R.id.buttonSet);



        auth = FirebaseAuth.getInstance();


        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String email = inputEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "Enter your registered email", Toast.LENGTH_SHORT).show();
            return;
        }


        auth.sendPasswordResetEmail(email)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(forgetPass.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(forgetPass.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(forgetPass.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
