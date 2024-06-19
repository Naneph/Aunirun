package com.example.auniruntest3.ui.aboutPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.auniruntest3.R;
import com.example.auniruntest3.databinding.FragmentAboutBinding;


public class AboutFragment extends Fragment {
    private TextView check;
    private Button thanksBtn;
    private Button authorBtn;
    private Spinner themeSpin;
    private Spinner languageSpin;
    private FragmentAboutBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);

        check = binding.textViewCheck;
        authorBtn = binding.authorBtn;
        thanksBtn = binding.thanksBtn;
        languageSpin = binding.languageSpin;
        themeSpin = binding.themeSpin;



        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 检查更新，创建dialog
                * */
            }
        });

        authorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 进入author页面
                * */
                Intent intent = new Intent(getContext(),AuthorActivity.class);
                startActivity(intent);

            }
        });

        thanksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 进入thanks页面
                * */
                Intent intent = new Intent(getContext(),ThanksActivity.class);
                startActivity(intent);
            }
        });




        return binding.getRoot();
    }
}