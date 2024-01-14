package uz.sher.puzzle15.fragments;

import android.app.AlertDialog;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import uz.sher.puzzle15.R;
import uz.sher.puzzle15.data.SharedRepository;
import uz.sher.puzzle15.databinding.FragmentSettingBinding;
import uz.sher.puzzle15.util.MySoundPool;


public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private SoundPool soundPool;
    private int clickButton;
    private SharedRepository sharedRepository;
    private MySoundPool mySoundPool;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedRepository = new SharedRepository(binding.getRoot().getContext());
        mySoundPool = new MySoundPool(binding.getRoot().getContext());

        binding.settingSwitch.setChecked(sharedRepository.getSoundStatus());

        binding.settingSwitch.setOnClickListener(v -> {
            saveSoundStatus(binding.settingSwitch.isChecked());
            if (binding.settingSwitch.isChecked()) {
                mySoundPool.playClickButton();
            }
        });

        binding.switchContainer.setOnClickListener(v -> {
            binding.settingSwitch.setChecked(!binding.settingSwitch.isChecked());
            saveSoundStatus(binding.settingSwitch.isChecked());
            if (binding.settingSwitch.isChecked()) {
                mySoundPool.playClickButton();
            }
        });

        binding.deleteContainer.setOnClickListener(v -> {
            deleteHighScoreDialog();
            if (binding.settingSwitch.isChecked()) {
                mySoundPool.playClickButton();
            }
        });

        binding.settingBackBtn.setOnClickListener(v -> {
            if (binding.settingSwitch.isChecked()) mySoundPool.playClickButton();
            getParentFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new MenuFragment()).commit();
        });


    }


    private void deleteHighScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
        AlertDialog dialog = builder.setView(getLayoutInflater().inflate(R.layout.custom_dialog, null)).create();
        dialog.setCancelable(false);
        requireActivity().getWindow().setNavigationBarColor(0);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Button noBtn = dialog.findViewById(R.id.dialogNoBtn);
        Button yesBtn = dialog.findViewById(R.id.dialogYesBtn);

        noBtn.setOnClickListener(v -> {
            if (binding.settingSwitch.isChecked()) {
                mySoundPool.playClickButton();
            }
            dialog.dismiss();
        });

        yesBtn.setOnClickListener(v -> {
            if (binding.settingSwitch.isChecked()) {
                mySoundPool.playClickButton();
            }
            deleteHighScoreItems();
            dialog.dismiss();
        });
    }

    private void deleteHighScoreItems() {
        sharedRepository.saveHighMove(0);
        sharedRepository.saveHighTime("00:00");
    }


    private void saveSoundStatus(boolean status) {
        sharedRepository.saveSoundStatus(status);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
         binding = null;

    }
}