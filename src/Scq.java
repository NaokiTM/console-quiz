import java.util.ArrayList;
public class Scq extends Question {
    private ArrayList<String> answers = new ArrayList<>();
    private int correctAnsIndex;

    public Scq(String name, String prompt, ArrayList<String> answers, int index) {
        super(name, prompt);
        this.answers = answers;
        this.correctAnsIndex = index;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnsIndex() {
        return correctAnsIndex;
    }

}
