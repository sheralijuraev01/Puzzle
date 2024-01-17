package uz.sher.puzzle15.screens;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import uz.sher.puzzle15.R;
import uz.sher.puzzle15.databinding.ActivityMainBinding;
import uz.sher.puzzle15.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppUpdateManager appUpdateManager;

    private static final int REQUEST_CODE = 123;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());


        checkForUpdates();
        showFeedbackDialog();
        appUpdateManager.registerListener(installStateUpdatedListener);


        //status bar color change
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.black));
            window.setNavigationBarColor(getColor(R.color.black));

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new MenuFragment(), "MenuFragment").commit();
    }


    InstallStateUpdatedListener installStateUpdatedListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(this, "Downloaded, Restart the app in 5 seconds", Toast.LENGTH_SHORT).show();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    appUpdateManager.completeUpdate();
                }
            }, 5000);
//            showCompletedUpdate();
        }

    };


    private void showFeedbackDialog() {
        ReviewManager reviewManager = ReviewManagerFactory.create(this);
        reviewManager.requestReviewFlow().addOnCompleteListener(it -> {
            if (it.isSuccessful()) {
                reviewManager.launchReviewFlow(this, it.getResult());
            }
        });
    }

    private void checkForUpdates() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            boolean isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
            boolean isUpdateAllowed = info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE);

            if (isUpdateAvailable && isUpdateAllowed) {
                try {
                    appUpdateManager.startUpdateFlowForResult(info, AppUpdateType.FLEXIBLE, this, REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }


        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {

                Toast.makeText(this, "Nimadir xato ketdi", Toast.LENGTH_SHORT).show();
                Log.e("Error", "Nimadir xato ketdi");
            }
        }
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdateManager.unregisterListener(installStateUpdatedListener);

    }
}