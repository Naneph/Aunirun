package com.example.auniruntest3;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.auniruntest3.databinding.ActivityMainBinding;
import com.example.auniruntest3.ui.aboutPage.AboutFragment;
import com.example.auniruntest3.ui.configPage.ConfigFragment;
import com.example.auniruntest3.ui.startPage.StartFragment;

public class MainActivity extends AppCompatActivity {


    /*
    *
    *
    *
    *
    * ef8a42橙色的颜色号
    *
    *
    *
    * */

    private ActivityMainBinding binding;
    private Button startPageBtn;
    private Button configPageBtn;
    private Button aboutPageBtn;
    private Fragment startFragment;
    private Fragment configFragment;
    private Fragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        startPageBtn = findViewById(R.id.start_page);
//        configPageBtn = binding.configPage;
        configPageBtn = findViewById(R.id.config_page);
        aboutPageBtn = findViewById(R.id.about_page);

        startFragment = new StartFragment();
        configFragment = new ConfigFragment();
        aboutFragment = new AboutFragment();


        /*
         * initMainPage
         * */
        replaceFragment(startFragment);
        startPageBtn.setEnabled(false);
        setDrawable(R.drawable.star_orange,startPageBtn);


        startPageBtn.setOnClickListener(v -> {
            replaceFragment(startFragment);
            statusChanged(v);
            setDrawable(R.drawable.star_orange,startPageBtn);
        });

        configPageBtn.setOnClickListener(v -> {
            replaceFragment(configFragment);
            statusChanged(v);
            setDrawable(R.drawable.config_orange,configPageBtn);
        });

        aboutPageBtn.setOnClickListener(v -> {
            replaceFragment(aboutFragment);
            statusChanged(v);
            setDrawable(R.drawable.about_orange,aboutPageBtn);
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        @SuppressLint("CommitTransaction") FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    private void statusChanged(View v) {
        if (!startPageBtn.isEnabled()) {
            startPageBtn.setEnabled(true);
            //设置v的icon和背景
            setDrawable(R.drawable.star_light,startPageBtn);
        }
        if (!configPageBtn.isEnabled()) {
            configPageBtn.setEnabled(true);
            setDrawable(R.drawable.config_light,configPageBtn);
        }
        if (!aboutPageBtn.isEnabled()) {
            aboutPageBtn.setEnabled(true);
            setDrawable(R.drawable.about_light,aboutPageBtn);
        }

        v.setEnabled(false);
    }

    private void setDrawable(int drawableId,Button btn){
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        btn.setCompoundDrawables(null, drawable, null, null);
    }


}