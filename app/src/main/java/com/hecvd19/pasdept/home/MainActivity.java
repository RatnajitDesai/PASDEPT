package com.hecvd19.pasdept.home;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hecvd19.pasdept.R;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(), 0);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);

        // tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);

        adapter.addFragment(new PostFragment(), "Posts");
        // adapter.addFragment(new ChannelsFragment(),"My Channels");
        viewPager.setAdapter(adapter);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            finish();
        }

        return true;
    }
}
