package practice.notes.com.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar statusToolbar;
    private TextInputLayout status;
    private Button update;
    private ProgressBar pro;

    //firebase
    private DatabaseReference ref;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        statusToolbar = findViewById(R.id.statusAppbar);
        setSupportActionBar(statusToolbar);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        status=findViewById(R.id.statusInput);
        update=findViewById(R.id.updateStatus);
        pro=findViewById(R.id.statusProgressBar);
        pro.setVisibility(View.INVISIBLE);

        //firebase
        ref=FirebaseDatabase.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        final String user_id=user.getUid().toString();

        String status_string=getIntent().getStringExtra("statusValue");
        status.getEditText().setText(status_string);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro.setVisibility(View.VISIBLE);
                final String Status_string=status.getEditText().getText().toString();
                ref.child("users").child(user_id).child("status").setValue(Status_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            pro.setVisibility(View.INVISIBLE);
                            Toast.makeText(StatusActivity.this,"Status Updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StatusActivity.this,SettingsActivity.class));
                        }
                        else
                        {
                            pro.setVisibility(View.INVISIBLE);
                            Snackbar.make(findViewById(R.id.statusLayout),"An Error Occured",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
