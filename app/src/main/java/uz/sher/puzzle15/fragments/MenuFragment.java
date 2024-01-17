package uz.sher.puzzle15.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import uz.sher.puzzle15.R;
import uz.sher.puzzle15.data.SharedRepository;
import uz.sher.puzzle15.databinding.FragmentMenuBinding;
import uz.sher.puzzle15.util.MySoundPool;


public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;

    private SharedRepository sharedRepository;
    private boolean isContinue = false;

    private MySoundPool mySoundPool;

    private boolean soundStatus = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedRepository = new SharedRepository(binding.getRoot().getContext());

        mySoundPool = new MySoundPool(binding.getRoot().getContext());

        soundStatus = sharedRepository.getSoundStatus();

        isContinue = sharedRepository.getContinue();
        if (isContinue) binding.menuContinue.setVisibility(View.VISIBLE);
        else binding.menuContinue.setVisibility(View.GONE);
        loadAnimation();

        binding.menuContinue.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            updateFragment();
        });
        binding.menuNewGame.setOnClickListener(v -> {
            sharedRepository.saveContinue(false);
            if (soundStatus) mySoundPool.playClickButton();
            updateFragment();
        });
        binding.menuAboutApp.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            aboutAppDialog();
        });

        binding.menuSetting.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();

            getParentFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new SettingFragment(), "SettingFragment").commit();

        });

        binding.menuExit.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            requireActivity().finish();
        });

    }

    private void loadAnimation() {
        Animation animation1 = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.lefttorightextra);
        Animation animation2 = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.righttoleftextra);
        if (isContinue) binding.menuContinue.startAnimation(animation1);
        binding.menuNewGame.startAnimation(animation2);
        binding.menuSetting.startAnimation(animation1);
        binding.menuAboutApp.startAnimation(animation2);
        binding.menuExit.startAnimation(animation1);

    }


    @SuppressLint("InflateParams")
    private void aboutAppDialog() {

        Dialog dialog = new Dialog(binding.getRoot().getContext());
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(getLayoutInflater().inflate(R.layout.about_app, null));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();


        Button exitBtn = dialog.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            dialog.dismiss();
        });

    }

    private void updateFragment() {
        getParentFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new GameFragment(), "GameFragment").commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
         binding = null;
    }
}