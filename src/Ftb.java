import java.util.ArrayList;
public class Ftb extends Question {
    private ArrayList<String> correctAnswers = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();
    private int userGotCorrect = 0;
    private int answeredBlanks = 0;

    public Ftb(String questionTitle, String questionText) {
        super(questionTitle, questionText);
    }

    public ArrayList<String> getUserAnswers() {
        return userAnswers;
    }

    public ArrayList<String> getCorrectAnswers() { return correctAnswers; }

    public int getAnsweredBlanks() {
        return answeredBlanks;
    }

    public int getUserGotCorrect() { return userGotCorrect; }

    public void setUserGotCorrect(int userGotCorrect) {
        this.userGotCorrect = userGotCorrect;
    }

    public void setAnsweredBlanks(int answeredBlanks) {
        this.answeredBlanks = answeredBlanks;
    }

    public void resetArrays() {
        for (int nextCorrectAns = 0; nextCorrectAns < correctAnswers.size(); nextCorrectAns++) {
            correctAnswers.set(nextCorrectAns, "");
        }

        for (int nextUserAns = 0; nextUserAns < userAnswers.size(); nextUserAns++) {
            userAnswers.set(nextUserAns, "");
        }
    }

}
