package uz.sher.puzzle15.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import uz.sher.puzzle15.databinding.FragmentMenuBinding;


public class MenuFragment extends Fragment {
    private FragmentMenuBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isContinue=false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences = requireActivity().getSharedPreferences("SaveItems", Context.MODE_PRIVATE);
         isContinue = sharedPreferences.getBoolean("isContinue", false);
        if (isContinue) binding.menuContinue.setVisibility(View.VISIBLE);
        else binding.menuContinue.setVisibility(View.GONE);
        loadAnimation();

        binding.menuContinue.setOnClickListener(v -> {
          updateFragment();
        });
        binding.menuNewGame.setOnClickListener(v -> {
            editor = sharedPreferences.edit();
            editor.putBoolean("isContinue", false);
            editor.apply();
            updateFragment();
        });
        binding.menuAboutApp.setOnClickListener(v->{
            aboutAppDialog();
        });

        binding.menuExit.setOnClickListener(v->{
            requireActivity().finish();
        });

    }

    private void loadAnimation() {
        Animation animation1 = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.lefttorightextra);
        Animation animation2 = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.righttoleftextra);
        if(isContinue) binding.menuContinue.startAnimation(animation1);
        binding.menuNewGame.startAnimation(animation2);
        binding.menuAboutApp.startAnimation(animation1);
        binding.menuExit.startAnimation(animation2);

    }

    @SuppressLint("InflateParams")
    private void aboutAppDialog(){

        Dialog dialog=new Dialog(binding.getRoot().getContext());
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(getLayoutInflater().inflate(R.layout.about_app,null));
        Window window=dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();



        Button exitBtn=dialog.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(v->{
            dialog.dismiss();

        });

    }
    private void updateFragment(){
        getParentFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new GameFragment(),"GameFragment").commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}