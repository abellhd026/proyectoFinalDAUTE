package com.example.proyectofinaldaute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    public EditText emailInput, passInput;
    private Button login, gotoRegister;
    FirebaseAuth firebaseAuth;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);

        login = findViewById(R.id.btnLogin);
        gotoRegister = findViewById(R.id.btnGotoRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(); 
            }
        });
        
    }

    public void login(){
        String email = emailInput.getText().toString().trim();
        String password = passInput.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Has iniciado sesion correctamente!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Navigation_DAUTE.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}