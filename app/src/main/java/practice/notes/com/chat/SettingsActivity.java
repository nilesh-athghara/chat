package practice.notes.com.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseUser currentUser;
    private TextView displayName,displaystatus;
    private CircleImageView image;
    private Button statusChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        image=findViewById(R.id.settingsCircleImageView);
        displayName=findViewById(R.id.settingDisplayName);
        displaystatus=findViewById(R.id.settingStatus);
        //extracting data from firebase
        ref=FirebaseDatabase.getInstance().getReference();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
        {
            String Uid=currentUser.getUid();
            ref.child("users").child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name=dataSnapshot.child("name").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();
                    String image=dataSnapshot.child("image").getValue().toString();
                    String thumb=dataSnapshot.child("thumb_image").getValue().toString();
                    displayName.setText(name);
                    displaystatus.setText(status);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        statusChange=findViewById(R.id.settingChangeStatus);
        statusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(new Intent(SettingsActivity.this,StatusActivity.class));
                String status=displaystatus.getText().toString();
                i.putExtra("statusValue",status);
                startActivity(i);
            }
        });
    }
}
