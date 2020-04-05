package com.example.chataplicationwithfirbase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistreActivity extends AppCompatActivity {
EditText user,password,email;
Button bntregistre;
FirebaseAuth auth;
DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);
        user=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.textpassword);
        email=(EditText)findViewById(R.id.email);
        bntregistre=(Button) findViewById(R.id.btnregistre);
        auth=FirebaseAuth.getInstance();


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registre");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bntregistre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=user.getText().toString();
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
if (TextUtils.isEmpty(txt_username) | TextUtils.isEmpty(txt_email) | TextUtils.isEmpty(txt_password)){
    Toast.makeText(RegistreActivity.this,"al filled are required",Toast.LENGTH_SHORT).show();
}
else if (txt_password.length() < 6){
    Toast.makeText(RegistreActivity.this,"password must be at least 6 characters",Toast.LENGTH_SHORT).show();


}
            }
        });
    }
    public void registre(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userId=firebaseUser.getUid();
                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> hashMap= new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("ImageUrl","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent=new Intent(RegistreActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegistreActivity.this,"You can't registre with this email or password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
