package practice.notes.com.chat;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Toolbar appBar;
    private Button loginButton;
    private ProgressBar pro;
    private TextInputLayout email,password;
    private FirebaseAuth auth;
    private ConstraintLayout c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hiding keyboard
        c=findViewById(R.id.loginLayout);
       c.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus)
               {
                   hideKeyboard(v);
               }
           }
       });

        //setting up toolbar
        appBar=findViewById(R.id.loginToolBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Login");

        //settingup back Button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email=findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);

        //progress bar
        pro=findViewById(R.id.loginProgressBar);

        //get firebase instance
        auth=FirebaseAuth.getInstance();

        //onclick listener for login button
        loginButton=findViewById(R.id.loginSubmit);
        pro.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String email_string=email.getEditText().getText().toString().trim();
                String password_string=password.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(email_string))
                {
                    Snackbar.make(findViewById(R.id.loginLayout),"Please enter Email",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password_string))
                {
                    Snackbar.make(findViewById(R.id.loginLayout),"Please enter Password",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    pro.setVisibility(View.VISIBLE);
                    login(email_string,password_string);
                }
            }
        });
    }


    //funtion to hide keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//function for login
    private void login(String email_string, String password_string) {
        auth.signInWithEmailAndPassword(email_string,password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    pro.setVisibility(View.INVISIBLE);
                    Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);

                    //even aftetr using finish() it will not go to login activity but it can go back to start activitry
                    //so we will create a new task and clear all the previous tasks
                    Toast.makeText(LoginActivity.this," Login Sucess!!",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    pro.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
