package practice.notes.com.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout displayName, email, password;
    private Button register;
    private FirebaseAuth auth;

    //toolbar displayed at top
    private Toolbar appBar;

    //progress bar
    private ProgressBar progress;
    private FirebaseDatabase data;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getting ids
        displayName = findViewById(R.id.loginEmail);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registertPassword);
        register = findViewById(R.id.registerCreateAccount);
        auth = FirebaseAuth.getInstance();//get firebase instance
        appBar = findViewById(R.id.registerToolBar);
        progress = findViewById(R.id.registerProgressBar);
        //hide progressbar
        progress.setVisibility(View.INVISIBLE);

        //Setting up top toolBar
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name = displayName.getEditText().getText().toString().trim();//trim eliminates trailing and leading spaces
                String email_string = email.getEditText().getText().toString().trim();
                String password_string = password.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(display_name)) {
                    Snackbar.make(findViewById(R.id.registerlayout), "Please enter Display Name", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(email_string)) {
                    Snackbar.make(findViewById(R.id.registerlayout), "Please enter Email", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password_string)) {
                    Snackbar.make(findViewById(R.id.registerlayout), "Please enter Password", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    progress.setVisibility(View.VISIBLE);
                    registerUser(display_name, email_string, password_string);
                }
            }
        });
    }


    //function to regtister user
    private void registerUser(final String display_name, String email_string, String password_string) {

        auth.createUserWithEmailAndPassword(email_string, password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //storing data
                    data = FirebaseDatabase.getInstance();
                    ref = data.getReference();//ref is now pointing to our root directory
                    FirebaseUser currentUser = auth.getCurrentUser();
                    String Uid = currentUser.getUid();
                    Log.i("uid", Uid);
                    //to add complex data we use a hashmanp
                    HashMap<String, String> user_details = new HashMap<>();
                    user_details.put("name", display_name);
                    user_details.put("status", "Hello! I am using Chat");
                    user_details.put("image", "default");
                    user_details.put("thumb_image", "default");
                    ref.child("users").child(Uid).setValue(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progress.setVisibility(View.INVISIBLE);
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mainIntent);
                                Toast.makeText(RegisterActivity.this, "Registration Sucessfull", Toast.LENGTH_SHORT).show();
                                finish();//when user presses back button it will not come back to this activity
                            } else {
                                progress.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
