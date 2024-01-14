package uz.sher.puzzle15.data;

public interface SharedService {
    String getHighTime();

    int getHighMove();

    void saveHighTime(String time);

    void saveHighMove(int moveCount);

    long getTime();

    int getMove();

    void saveTime(long time);

    void saveMove(int moveCount);

    void saveContinue(boolean isContinue);

    boolean getContinue();

    void saveNumberPositions(String positions);

    String getNumberPositions();

    void saveSoundStatus(boolean status);

    boolean getSoundStatus();
}
