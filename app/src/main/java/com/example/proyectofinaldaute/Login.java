package com.example.proyectofinaldaute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    public EditText emailInput, passInput;
    private Button login, gotoRegister, btnRegistroNuevo;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);

        login = findViewById(R.id.btnLogin);
        gotoRegister = findViewById(R.id.btnGotoRegister);

        btnRegistroNuevo = (Button) findViewById(R.id.btnRegistroNuevo);

        firebaseAuth = FirebaseAuth.getInstance();


        // Verifica si el usuario ya ha iniciado sesion
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(); 
            }
        });

        btnRegistroNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "Has iniciado un nuevo registro", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, register.class);
                startActivity(intent);
                finish();
            }
        });
        
    }

    public void login(){
        String email = emailInput.getText().toString().trim();
        String password = passInput.getText().toString();

        boolean val = validarDatos(email, password);
        
        if (val) {
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

    public boolean validarDatos(String email, String password) {
        if (email.isEmpty() && password.isEmpty()) {
            emailInput.setError("Ingrese un correo");
            passInput.setError("Ingrese su contraseña");
            return false;
        } else {
            return true;
        }
    }


}