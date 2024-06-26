import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionBank {
     private String qbUniqueIdentifier;
     private String bankIdentifier;
     private Scanner scan = new Scanner(System.in);
     private ArrayList<Question> questions = new ArrayList<>();

     public QuestionBank(String newBankIdentifier, String moduleIdentifier) {
         this.bankIdentifier = newBankIdentifier;
         this.qbUniqueIdentifier = moduleIdentifier + ":" + bankIdentifier;
     }

     public String getQbUniqueIdentifier() {
         return qbUniqueIdentifier;
     }

     public ArrayList<Question> getQuestions() {
         return questions;
     }

     public void addQuestion() {
         boolean exitFlag = false;

         while (!exitFlag) {
             System.out.println("would you like to add a single choice(s) or fill the blanks(f) question?");
             String input = scan.nextLine().toLowerCase();

             if (input.equals("s")) {
                 exitFlag = true;
                 addScq();
             } else if (input.equals("f")) {
                 exitFlag = true;
                 addFtb();
             } else {
                 System.out.println("invalid, try again");
             }
         }

     }

     public void addScq() {
         ArrayList<String> answersToAdd = new ArrayList<>();
         int countAnswersAdded = 0;  //this counts number of answers added to the answersToAdd array, so the program will only say maximum questions reached when the user has entered 10 answers.
         int indexOffset = 1; //this variable is used when selecting the question number, to list the questions starting with 1 instead of 0.
         int correctAnswerPosition = 0;
         boolean correctAnswerAdded = false;
         boolean wrongAnswerAdded = false;
         int maxAnswers = 10;


         System.out.println("enter title of question");
         String scqTitle = scan.nextLine();
         System.out.println("enter question prompt: ");
         String scqPrompt = scan.nextLine();


         for (int answerCount = 0; answerCount < maxAnswers; answerCount++) {
             System.out.println("enter answer no." + (answerCount + indexOffset) + " , press x to create question and return to main menu.");
             String answer = scan.nextLine().toLowerCase();

             if (answer.equals("x")) {       //below code creates a new question passing the array of answers made using the current method as a parameter. and returns to main menu.
                 if (!correctAnswerAdded) {
                     System.out.println("you don't have a correct answer!");
                     addScq();
                 } else if (!wrongAnswerAdded) {
                     System.out.println("you don't have a wrong answer!");
                     addScq();
                 }

                 Scq newScq = new Scq(scqTitle, scqPrompt, answersToAdd, correctAnswerPosition);
                 questions.add(newScq);
                 System.out.println("created question!");
                 return;
             } else {       //if user wants to keep adding answers, the current answer is saved in the array containing the rest of the answers.
                 System.out.println("is this the correct answer (y/n)");
                 String isCorrect = scan.nextLine();
                 answersToAdd.add(answer);

                 if (isCorrect.equals("y") && correctAnswerAdded) {
                     System.out.println("correct answer already added, will be defaulted to distractor answer.");
                     wrongAnswerAdded = true;
                 } else if (isCorrect.equals("y")) {
                     correctAnswerPosition = answersToAdd.size() - 1;   //this works because the new answer is already added
                     correctAnswerAdded = true;
                     System.out.println("added as correct answer");
                 } else if (isCorrect.equals("n")) {
                     System.out.println("added as distractor answer");
                     wrongAnswerAdded = true;
                 }
                 System.out.println("saved answer!");
                 countAnswersAdded++;
             }
         }
         if (countAnswersAdded == maxAnswers) {
             System.out.println("created question, maximum number of answers reached, returning to main menu..."); //user cannot add more than 10 questions, once looped through 10 times, this line is printed and breaks out of method

         } else {
             System.out.println("created question! returning to main menu...");
         }
     }

     public void addFtb() {
         boolean exitFlag = false;
         while (!exitFlag) {
             System.out.println("enter the name of the question");
             System.out.println("exit (x)");
             String nameOfQuestion = scan.nextLine();
             System.out.println("enter question prompt, using < on the left and > on the right of the hidden text.");
             System.out.println("exit (x)");
             String ftbText = scan.nextLine();

             Pattern underscorePattern = Pattern.compile("<([^<>]+)>");
             Matcher findGapWord = underscorePattern.matcher(ftbText);

             if (nameOfQuestion.equals("x") || ftbText.equals("x")) {
                 exitFlag = true;
                 System.out.println("returning to teacher menu...");
             } else if (!findGapWord.find()) {
                 System.out.println("your prompt doesn't contain any blanks to fill! try again");
             } else {
                 questions.add(new Ftb(nameOfQuestion, ftbText));
                 System.out.println("question " + nameOfQuestion + " added to question bank: " + bankIdentifier);
                 exitFlag = true;
             }
         }
     }

     public void deleteQuestion() {
         String inputString;
         boolean exitFlag = false;
         while (!exitFlag) {

             for (int i = 0; i < questions.size(); i++) {                            //this loops as many times as questions the user wants to delete
                 System.out.println("select which question you want to delete");

                 if (questions.isEmpty()) {
                     System.out.println("question bank is empty!");
                 } else {
                     for (int j = 0; j < questions.size(); j++) {                        //this loops through and lists all questions available to delete
                         System.out.println(questions.get(j).questionName + "(" + j + ")");
                     }
                 }

                 System.out.println("press x to stop and exit");
                 inputString = scan.nextLine().toLowerCase();

                 //try parsing input as integer
                 try {
                     int testInputForInt = Integer.parseInt(inputString);
                     questions.remove(testInputForInt);
                     System.out.println("removed question from questionbank - " + this.qbUniqueIdentifier);
                     exitFlag = true;
                 } catch (NumberFormatException e) {   //string input and method exit handling
                     if (inputString.equals("x")) {
                         exitFlag = true;
                         System.out.println("returning to teacher menu...");
                     } else {
                         System.out.println("invalid input, try again");
                     }
                 }
             }
         }
     }


    public String getBankIdentifier() {
         return bankIdentifier;
    }
}
