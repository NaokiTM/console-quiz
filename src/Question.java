public class Question {
    protected String questionName;
    protected String questionText;
    protected boolean isAnswered;
    protected boolean isCorrect;

    public Question(String questionName, String questionText) {
        this.questionName = questionName;
        this.questionText = questionText;
    }
}
