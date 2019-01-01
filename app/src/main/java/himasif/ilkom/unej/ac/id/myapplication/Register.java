package himasif.ilkom.unej.ac.id.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Register extends AppCompatActivity {
    EditText txtEmail, txtUser, txtPass;
    Button regis;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtUser = (EditText) findViewById(R.id.txtUser);
        regis = (Button) findViewById(R.id.btnRegis);
        auth = FirebaseAuth.getInstance();
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String username = txtUser.getText().toString().trim();
                String password = txtPass.getText().toString().trim();
                if (password.length() < 6) {
                    Toast.makeText(Register.this, "Password must be at least 6 char", Toast.LENGTH_SHORT).show();
                } else {
                    register(email, username, password);
                }
            }
        });
    }

    private void register(String email, final String username, String password) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", userId);
                    map.put("username", username);
                    map.put("imageURL", "default");
                    map.put("status","offline");
                    map.put("search",username.toLowerCase());

                    reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(Register.this, Login.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
