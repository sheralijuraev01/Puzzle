package uz.sher.puzzle15.screens;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import uz.sher.puzzle15.R;
import uz.sher.puzzle15.databinding.ActivityMainBinding;
import uz.sher.puzzle15.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //status bar color change
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.black));
            window.setNavigationBarColor(getColor(R.color.black));

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new MenuFragment(), "MenuFragment").commit();
    }


    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainFragmentContainer);
        if (currentFragment != null && currentFragment.getTag() != null) {
            if (currentFragment.getTag().equals("GameFragment")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new MenuFragment()).commit();
            } else if (currentFragment.getTag().equals("SettingFragment")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new MenuFragment()).commit();
            } else {

                super.onBackPressed();
            }
        } else {

            super.onBackPressed();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}