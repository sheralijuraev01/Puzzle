package uz.sher.puzzle15.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import uz.sher.puzzle15.R;
import uz.sher.puzzle15.data.SharedRepository;
import uz.sher.puzzle15.databinding.FragmentGameBinding;
import uz.sher.puzzle15.util.MySoundPool;

public class GameFragment extends Fragment implements View.OnClickListener {
    FragmentGameBinding binding;
    private static List<Integer> numbers = new ArrayList<>();
    private int x;
    private int y; // x va y empty button koordinatasi
    private Button emptyButton;
    private int moveCount = 0;
    private int drawablePosition = 0;

    private long saveTime = 0L;
    private boolean isContinue = false;
    private boolean exitByWinDialog = false;
    private long highTimeSave = 0L;
    private int highMoveSave = 0;
    private SharedRepository sharedRepository;

    private MySoundPool mySoundPool;

    private boolean soundStatus;

    private final int[] backDrawable = {R.drawable.stone_bg1, R.drawable.stone_bg2, R.drawable.stone_bg3, R.drawable.stone_bg4};


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedRepository = new SharedRepository(binding.getRoot().getContext());

        mySoundPool = new MySoundPool(binding.getRoot().getContext());

        soundStatus = sharedRepository.getSoundStatus();


        isContinue = sharedRepository.getContinue();
        if (isContinue) {
            saveTime = sharedRepository.getTime();
            moveCount = sharedRepository.getMove();
            binding.moveCount.setText(String.valueOf(moveCount));

        }

        String strTime = sharedRepository.getHighTime();
        highTimeSave = formatTimeLong(strTime);
        highMoveSave = sharedRepository.getHighMove();
        binding.highTimeScore.setText(strTime);
        binding.highMoveSave.setText(String.valueOf(highMoveSave));

        loadAnimation();
        game();


        controlTimer();


        binding.button1.setOnClickListener(this);
        binding.button2.setOnClickListener(this);
        binding.button3.setOnClickListener(this);
        binding.button4.setOnClickListener(this);
        binding.button5.setOnClickListener(this);
        binding.button6.setOnClickListener(this);
        binding.button7.setOnClickListener(this);
        binding.button8.setOnClickListener(this);
        binding.button9.setOnClickListener(this);
        binding.button10.setOnClickListener(this);
        binding.button11.setOnClickListener(this);
        binding.button12.setOnClickListener(this);
        binding.button13.setOnClickListener(this);
        binding.button14.setOnClickListener(this);
        binding.button15.setOnClickListener(this);
        binding.button16.setOnClickListener(this);

        binding.refreshButton.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            refreshGame();
        });
        binding.backButton.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            getParentFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new MenuFragment()).commit();
        });
    }


    @SuppressLint("ObsoleteSdkInt")


    private void refreshGame() {
        newGameShuffle();
        moveCount = 0;
        binding.moveCount.setText(String.valueOf(moveCount));
        binding.time.setBase(SystemClock.elapsedRealtime());
        binding.time.start();
        exitByWinDialog = false;
    }

    public void game() {
        numbers.clear();
        fillToArray();
        newGameShuffle();

    }

    @SuppressLint("ResourceAsColor")
    public void newGameShuffle() {
        if (isContinue) {
            String[] arr = getNumberPosition();
            numbers.clear();
            for (int i = 0; i < 16; i++) {
                numbers.add(Integer.parseInt(arr[i]));
            }
            isContinue = false;

        } else {
            solvableShuffleNumbers();
        }

        determineEmptyButtonPosition();

        for (int i = 0; i < binding.gridLayout.getChildCount(); i++) {
            View view = binding.gridLayout.getChildAt(i);
            String tag = view.getTag().toString();
            if ((x + "" + y).equals(tag)) {
                emptyButton = (Button) view;
//                emptyButton.setVisibility(View.INVISIBLE);
                emptyButton.setText("");
                emptyButton.setBackgroundResource(R.drawable.default_stone);


            } else {
                ((Button) binding.gridLayout.getChildAt(i)).setText(String.valueOf(numbers.get(i)));
                determineDrawablePosition(numbers.get(i));
                binding.gridLayout.getChildAt(i).setBackgroundResource(backDrawable[drawablePosition]);
                if (numbers.get(i) > 8)
                    ((Button) binding.gridLayout.getChildAt(i)).setTextColor(getResources().getColor(R.color.white));
                else
                    ((Button) binding.gridLayout.getChildAt(i)).setTextColor(getResources().getColor(R.color.text_color));
                (binding.gridLayout.getChildAt(i)).setVisibility(View.VISIBLE);

            }
        }


    }

    private void loadAnimation() {
        Animation animation1 = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.lefttorightextra);
        Animation animation2 = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.righttoleftextra);
        binding.topLayout.startAnimation(animation1);
        binding.secondaryLayout.startAnimation(animation2);
        binding.descText.startAnimation(animation1);
        binding.gridLayout.startAnimation(animation2);
    }


    private void determineDrawablePosition(int index) {
        if (index < 5) drawablePosition = 0;
        else if (index < 9) drawablePosition = 1;
        else if (index < 13) drawablePosition = 2;
        else drawablePosition = 3;
    }

    public static void fillToArray() {
        for (int i = 1; i <= 16; i++) {
            numbers.add(i);
        }
    }

    private void solvableShuffleNumbers() {
        do {
            Collections.shuffle(numbers);
        } while (!isSolvable(numbers));
    }

    private boolean isSolvable(List<Integer> numbers) {
        int counter = 0;
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == 16) {
                counter += i / 4 + 1;
                continue;
            }
            for (int j = i + 1; j < numbers.size(); j++) {
                if (numbers.get(i) > numbers.get(j)) {
                    counter++;
                }
            }
        }
        return counter % 2 == 0;
    }

    private void determineEmptyButtonPosition() {

        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == 16) {
                x = i / 4;
                if (x == 0) y = i;
                if (x == 1) y = i - 4;
                if (x == 2) y = i - 8;
                if (x == 3) y = i - 12;
                return;
            }
        }

    }


    private void checkWin() {
        int counter = 1;
        for (int i = 0; i < binding.gridLayout.getChildCount() - 1; i++) {
            Button check = (Button) binding.gridLayout.getChildAt(i);
            if (check.getText().toString().isEmpty()) break;
            if (Integer.parseInt(check.getText().toString()) == counter) {
                counter++;
            } else {
                break;
            }
        }
        if (counter == 16) {
            //winDialog()
            winDialog();
        }

    }

    private void winDialog() {


        String time = binding.time.getText().toString();
        binding.time.stop();
        AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
        AlertDialog dialog = builder.setView(getLayoutInflater().inflate(R.layout.game_over_layout, null)).create();
        dialog.setCancelable(false);
        requireActivity().getWindow().setNavigationBarColor(0);
        dialog.setContentView(R.layout.game_over_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        ImageView refreshBtn = dialog.findViewById(R.id.win_refresh);
        ImageView exitBtn = dialog.findViewById(R.id.win_exit);
        TextView winConditionText = dialog.findViewById(R.id.winConditionText);
        TextView winTime = dialog.findViewById(R.id.win_time);
        TextView winMove = dialog.findViewById(R.id.win_move);

        winMove.setText(String.valueOf(moveCount));
        winTime.setText(binding.time.getText().toString());


        boolean isTime = false;
        boolean isMove = false;


        if (formatTimeLong(time) < highTimeSave || highTimeSave == 0) {

            sharedRepository.saveHighTime(time);
            binding.highTimeScore.setText(time);
            isTime = true;
        }
        if (highMoveSave > moveCount || highMoveSave == 0) {
            sharedRepository.saveHighMove(moveCount);
            binding.highMoveSave.setText(String.valueOf(moveCount));
            isMove = true;
        }


        if (isTime && isMove) {
            if (soundStatus) mySoundPool.playSoundWin();
            winConditionText.setText("High Score");

        } else if (isTime) {
            winConditionText.setText("High Score Time");
            if (soundStatus) mySoundPool.playSoundWin();
        } else if (isMove) {
            winConditionText.setText("High Score Move");
            if (soundStatus) mySoundPool.playSoundWin();
        } else {
            winConditionText.setText("Good Job");
            if (soundStatus) mySoundPool.playGameOver();
        }

        refreshBtn.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            refreshGame();
            dialog.dismiss();
        });
        exitBtn.setOnClickListener(v -> {
            if (soundStatus) mySoundPool.playClickButton();
            dialog.dismiss();
            exitByWinDialog = true;
            getParentFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, new MenuFragment()).commit();
        });


    }

    private void controlTimer() {
        binding.time.setBase(SystemClock.elapsedRealtime() - saveTime);
        binding.time.start();
    }

    @Override
    public void onClick(View v) {
        Button clicked = (Button) v;
        String tag = v.getTag().toString();
        int clickedX = tag.charAt(0) - '0';
        int clickedY = tag.charAt(1) - '0';

        boolean isHorizontal = (Math.abs(x - clickedX) == 0 && Math.abs(y - clickedY) == 1), isVertical = (Math.abs(x - clickedX) == 1 && Math.abs(y - clickedY) == 0);
        if (isHorizontal || isVertical) {
            if (soundStatus) mySoundPool.playClickStone();

            animateMoveButton(clickedX, clickedY, isHorizontal, isVertical, clicked);
            moveCount++;
            binding.moveCount.setText(String.valueOf(moveCount));
            String text = clicked.getText().toString();
            clicked.setText("");
            clicked.setBackgroundResource(R.drawable.default_stone);

            determineDrawablePosition(Integer.parseInt(text));
            emptyButton.setBackgroundResource(backDrawable[drawablePosition]);

            emptyButton.setText(text);
            if (Integer.parseInt(text) > 8)
                emptyButton.setTextColor(getResources().getColor(R.color.white));
            else emptyButton.setTextColor(getResources().getColor(R.color.text_color));

            emptyButton = clicked;
            x = clickedX;
            y = clickedY;

        }

        checkWin();
//        gameButtonMedia.stop();
    }

    private void animateMoveButton(int clickX, int clickY, boolean isHorizontal, boolean isVertical, Button clicked) {
        Animation leftToRight = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.lefttoright);
        Animation rightToLeft = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.righttoleft);
        Animation upToDown = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.uptodown);
        Animation downToUp = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.downtoup);
        if (isHorizontal) {
            if (y > clickY) {
                // empty button chapga , clikced button o'ngga
                clicked.startAnimation(rightToLeft);
                emptyButton.startAnimation(leftToRight);
            } else if (y < clickY) {
                // empty button o'ngga , clikced button chapga
                clicked.startAnimation(leftToRight);
                emptyButton.startAnimation(rightToLeft);
            }
        } else if (isVertical) {
            if (x > clickX) {
                // empty button yuqoriga , clikced button pastga
                clicked.startAnimation(downToUp);
                emptyButton.startAnimation(upToDown);

            } else if (x < clickX) {
                // empty button pastga , clikced button yuqoriga
                clicked.startAnimation(upToDown);
                emptyButton.startAnimation(downToUp);

            }
        }
    }

    private Long formatTimeLong(String time) {
        String[] arr = time.split(":");
        int minut = 0;
        int second = 0;
        if (arr[0].charAt(0) == '0') {
            if (arr[0].charAt(1) != '0') {
                minut = Integer.parseInt(arr[0].charAt(1) + "");
            }
        } else {
            minut = Integer.parseInt(arr[0]);
        }
        if (arr[1].charAt(0) == '0') {
            if (arr[1].charAt(1) != '0') {
                second = Integer.parseInt(arr[1].charAt(1) + "");
            }
        } else {
            second = Integer.parseInt(arr[1]);
        }
        return (minut * 60L + second) * 1000L;

    }

    private void saveItems() {

        sharedRepository.saveTime(formatTimeLong(binding.time.getText().toString()));
        sharedRepository.saveMove(moveCount);
        sharedRepository.saveNumberPositions(savePuzzleNumberPosition());
        sharedRepository.saveContinue(!exitByWinDialog);
    }

    private String savePuzzleNumberPosition() {
        StringBuilder numberPosition = new StringBuilder();
        for (int i = 0; i < binding.gridLayout.getChildCount(); i++) {
            if (((Button) binding.gridLayout.getChildAt(i)).getText() != "") {
                numberPosition.append(((Button) binding.gridLayout.getChildAt(i)).getText().toString());
            } else numberPosition.append("16");
            numberPosition.append(":");
        }
        return numberPosition.toString();
    }

    private String[] getNumberPosition() {

        String numPosition = sharedRepository.getNumberPositions();
        if (numPosition == null) return new String[]{};
        return numPosition.split(":");
    }

    @Override
    public void onStop() {
        saveItems();
        super.onStop();
    }

    @Override
    public void onDestroy() {
         super.onDestroy();
        binding = null;

    }
}