package com.example.proyectofinaldaute;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public EditText emailInput, passInput;
    private Button login, gotoRegister;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);

        login = findViewById(R.id.btnLogin);
        gotoRegister = findViewById(R.id.btnGotoRegister);

        firebaseAuth = FirebaseAuth.getInstance();
    }


}

