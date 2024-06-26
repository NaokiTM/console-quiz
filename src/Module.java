import java.util.ArrayList;
import java.util.Scanner;
public class Module {
    private ArrayList<QuestionBank> questionBanks = new ArrayList<>();
    private String moduleIdentifier;

    private Scanner scan = new Scanner(System.in);

    public Module(String moduleIdentifier) {
        this.moduleIdentifier = moduleIdentifier;
    }

    public ArrayList<QuestionBank> getQuestionBanks() {
        return questionBanks;
    }

    public void addQuestionBank() {
        boolean dupeIdFound = false;
        boolean exitFlag = false;


        while (!exitFlag) {
            System.out.println("enter the identifier for this question bank: ");
            String idInput = scan.nextLine();

            if (idInput.isBlank()) {
                System.out.println("you need to give the question bank a name, try again");
            }

            for (QuestionBank questionBank : questionBanks) {
                if (questionBank.getBankIdentifier().equals(idInput)) {
                    dupeIdFound = true;
                    break;
                }
            }

            if (dupeIdFound) {
                System.out.println("a question bank with this name already exists in this module, try again.");
            } else {
                if (idInput.length() < 7) {
                    System.out.println(idInput.length());
                    System.out.println("bank ID too short");
                } else if (idInput.length() > 15) {
                    System.out.println("bank ID too long");
                } else {
                    questionBanks.add(new QuestionBank(idInput, moduleIdentifier));
                    System.out.println("added question bank " + questionBanks.getLast().getQbUniqueIdentifier());
                    exitFlag = true;
                }
            }
        }
    }

    public void deleteQuestionBank() {
        System.out.println("select questionBank to delete");
        for (int idIndex = 0; idIndex < questionBanks.size(); idIndex++) {
            System.out.println(questionBanks.get(idIndex).getQbUniqueIdentifier() + "(" + idIndex + ")");
        }
        int indexToDelete = scan.nextInt();
        if (questionBanks.get(indexToDelete).getQuestions().isEmpty()) {
            System.out.println("deleted " + questionBanks.get(indexToDelete).getQbUniqueIdentifier() + " from module " + this.moduleIdentifier);
            questionBanks.remove(questionBanks.get(indexToDelete));
        } else {
            System.out.println("question bank isn't empty!");
        }
    }

    public QuestionBank selectQuestionBank() {

        QuestionBank selectedBank = null;

        if (questionBanks.isEmpty()) {
            System.out.println("this module has no question banks! returning...");
        } else {
            System.out.println("select question bank: ");
            for (int qbIndex = 0; qbIndex < questionBanks.size(); qbIndex++) {
                QuestionBank allQuestionBanks = questionBanks.get(qbIndex);
                System.out.println(allQuestionBanks.getQbUniqueIdentifier() + "(" + qbIndex + ")");
            }
            int qbChoice = scan.nextInt();
            if (qbChoice > questionBanks.size()) {
                System.out.println("invalid choice, try again.");
                selectQuestionBank();
            }
            selectedBank = questionBanks.get(qbChoice);
            if (selectedBank.getQuestions().isEmpty()) {
                System.out.println("this question bank doesn't contain any questions");
            }
        }
        return selectedBank;
    }

    public String getModuleIdentifier() {
        return moduleIdentifier;
    }
}
