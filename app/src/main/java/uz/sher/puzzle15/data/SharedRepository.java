package uz.sher.puzzle15.data;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedRepository implements SharedService {
    private static final String HIGH_TIME = "high_time";
    private static final String HIGH_MOVE = "high_move";
    private static final String TIME = "time";
    private static final String MOVE = "move";
    private static final String CONTINUE = "continue";
    private static final String POSITION_STATUS = "positions";
    private static final String SOUND_STATUS = "sound";
    private static final String SHARED_NAME = "Puzzle15";


    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedPrefEditor;

    public SharedRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }


    @Override
    public String getHighTime() {
        return sharedPreferences.getString(HIGH_TIME, "00:00");
    }

    @Override
    public int getHighMove() {
        return sharedPreferences.getInt(HIGH_MOVE, 0);
    }

    @Override
    public void saveHighTime(String time) {
        sharedPrefEditor.putString(HIGH_TIME, time).apply();
    }

    @Override
    public void saveHighMove(int moveCount) {
        sharedPrefEditor.putInt(HIGH_MOVE, moveCount).apply();
    }

    @Override
    public long getTime() {
        return sharedPreferences.getLong(TIME, 0L);
    }

    @Override
    public int getMove() {
        return sharedPreferences.getInt(MOVE, 0);
    }

    @Override
    public void saveTime(long time) {
        sharedPrefEditor.putLong(TIME, time).apply();
    }

    @Override
    public void saveMove(int moveCount) {
        sharedPrefEditor.putInt(MOVE, moveCount).apply();
    }

    @Override
    public void saveContinue(boolean isContinue) {
        sharedPrefEditor.putBoolean(CONTINUE, isContinue).apply();
    }

    @Override
    public boolean getContinue() {
        return sharedPreferences.getBoolean(CONTINUE, false);
    }

    @Override
    public void saveNumberPositions(String positions) {
        sharedPrefEditor.putString(POSITION_STATUS, positions).apply();
    }

    @Override
    public String getNumberPositions() {
        return sharedPreferences.getString(POSITION_STATUS, null);
    }

    @Override
    public void saveSoundStatus(boolean status) {
        sharedPrefEditor.putBoolean(SOUND_STATUS, status).apply();
    }

    @Override
    public boolean getSoundStatus() {
        return sharedPreferences.getBoolean(SOUND_STATUS, true);

    }


}
