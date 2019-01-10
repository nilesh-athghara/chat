package practice.notes.com.chat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Toolbar appBar;
    private ViewPager viewFragments;//to display tabs ..we also need a viewpager adapter
    private ViewPagerAdapter viewFragmentsAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        appBar = findViewById(R.id.mainToolBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Chat");
        viewFragments = findViewById(R.id.mainTabPager);


        //seting adapter for view pager
        viewFragmentsAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewFragments.setAdapter(viewFragmentsAdapter);

        //settingTabLayout
        tabLayout = findViewById(R.id.mainTabLayout);
        tabLayout.setupWithViewPager(viewFragments);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();//get current user
        if (currentUser == null) {
            /*startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();//this clears the intent record so user doesnt comes back to the same screen after preswsing back button*/
            //we need this at time of signout also so we will create a function
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent i = new Intent(MainActivity.this, StartActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    //for menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.mainLogout) {
            auth.signOut();
            sendToStart();
        }
        if (item.getItemId() == R.id.mainSettings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        return true;
    }
}
