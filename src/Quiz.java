import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;


public class Quiz {

    private long initialTime;
    private Scanner scan = new Scanner(System.in);

    public void runQuiz(QuestionBank bank) {
        Timer timer = new Timer();


        System.out.println("how many questions would you like to answer?");
        System.out.println("cancel(x)");
        String input = scan.nextLine().toLowerCase();

        try {
            int Q = Integer.parseInt(input);
            int totalNumOfQuestions = bank.getQuestions().size();
            if (Q > totalNumOfQuestions) {
                Q = totalNumOfQuestions;
            } else if (Q <= 0) {
                System.out.println("invalid number of questions, try again");
                runQuiz(bank);
            }

            Collections.shuffle(bank.getQuestions());  //shuffles all questions
            ArrayList<Question> testQuestions = new ArrayList<>(bank.getQuestions().subList(0,Q)); //then picks Q questions

            for (Question testQuestion : testQuestions) {
                if (testQuestion instanceof Ftb) {
                    Pattern catchWords = Pattern.compile("<([^_]+)>");
                    Matcher wordsToStore = catchWords.matcher(testQuestion.questionText);

                    while (wordsToStore.find()) {       //adds all default correct answer words into an array.
                        String wordToAdd = wordsToStore.group(1);
                        ((Ftb) testQuestion).getCorrectAnswers().add(wordToAdd);
                    }
                }
            }

            System.out.println("you're about to be quizzed on " + Q + " questions");


            System.out.println("press enter to start the timer!");
            System.out.println("cancel (x)");
            String timerInput = scan.nextLine();


            if (timerInput.equals("x")) {
                System.out.println("returning to the student menu");
                //will automatically return to the student menu
            } else if (timerInput.isBlank()) {
                initialTime = System.currentTimeMillis();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                    }
                };

                timer.schedule(task, 0, 1000);
                System.out.println("started timer");


                //mechanism that keeps the quiz running until it stops.
                String returnedVal = "";
                int questionIndex = 1;

                while (!returnedVal.equals("s")) {
                    if (returnedVal.equals("a")) {
                        if ((questionIndex - 1) < 1) {  //wrapping
                            questionIndex = Q;
                        } else {
                            questionIndex--;
                        }
                        returnedVal = handleQuestion(testQuestions.get(questionIndex - 1), questionIndex);
                        System.out.println(returnedVal);
                    } else if (returnedVal.equals("d")) {
                        if ((questionIndex + 1) > Q) {  //wrapping the other way
                            questionIndex = 1;
                        } else {
                            questionIndex++;
                        }
                        returnedVal = handleQuestion(testQuestions.get(questionIndex - 1), questionIndex);
                    } else {
                        if (testQuestions.get(questionIndex - 1).isAnswered) {
                            System.out.println("you have answered this question");
                        }
                        //will keep showing the same question, answered or not, until the user either submits or changes question
                        returnedVal = handleQuestion(testQuestions.get(questionIndex - 1), questionIndex);  //-1 because arraylist is 0 indexed
                    }


                }


                stopTimer(timer);
                calculateScore(testQuestions);
                resetAnswers(testQuestions);


            } else {
                System.out.println("invalid input, don't type anything before pressing enter if you want to start the timer");
                runQuiz(bank);
            }



        } catch (InputMismatchException e) {
            if (input.equals("x")) {
                System.out.println("returning to student menu...");
            } else {
                System.out.println("invalid input try again");
                runQuiz(bank);
            }
        }
    }


    public String handleQuestion(Question questionToDisplay, int questionNo) {

        String questionPrompt = questionToDisplay.questionText;
        String returnVal = "";

        System.out.println();
        System.out.println("//QUESTION NO. " + questionNo);

        if (questionToDisplay instanceof Scq) {

            System.out.println(questionPrompt);
            System.out.println();

            System.out.println("select an answer for this question: ");
            for (int i = 0; i < ((Scq) questionToDisplay).getAnswers().size(); i++) {
                System.out.println(((Scq) questionToDisplay).getAnswers().get(i) + "(" + i + ")");
            }

            System.out.println("a for previous question, d for next question");
            System.out.println("s to submit the quiz");

            String input = scan.nextLine();
            input = input.trim();

            if (input.equals("a")) {
                returnVal = "a";
            } else if (input.equals("d")) {
                returnVal = "d";
            } else if (input.equals("s")){
                returnVal = "s";
            } else if (!input.isEmpty()) {

                int selectionToInt = Integer.parseInt(input);

                if (selectionToInt == ((Scq) questionToDisplay).getCorrectAnsIndex()) {
                    questionToDisplay.isCorrect = true;
                } else if (selectionToInt != ((Scq) questionToDisplay).getCorrectAnsIndex()) {
                    questionToDisplay.isCorrect = false;
                } else if (selectionToInt > 9 || selectionToInt < 0) {
                    System.out.println("invalid, select an available answer");
                    handleQuestion(questionToDisplay, questionNo);
                }
                questionToDisplay.isAnswered = true;

            }


        } else if (questionToDisplay instanceof Ftb) {
            int answeredBlanks = ((Ftb) questionToDisplay).getAnsweredBlanks();

            String textWithGaps = groupBlankWords(questionPrompt);

            System.out.println(textWithGaps);
            System.out.println("fill the gaps, in order:");
            System.out.println(answeredBlanks + " are already filled");

            ArrayList<String> correctWords = ((Ftb) questionToDisplay).getCorrectAnswers();
            ArrayList<String> userWords = ((Ftb) questionToDisplay).getUserAnswers();

            System.out.println("a for previous question, d for next question");
            System.out.println("s to submit the quiz");

            String input = scan.nextLine();
            input = input.trim();

            if (input.equals("a")) {
                returnVal = "a";
            } else if (input.equals("d")) {
                returnVal = "d";
            } else if (input.equals("s")){
                returnVal = "s";
            } else if (!input.isEmpty()) {

                ((Ftb) questionToDisplay).getUserAnswers().add(input);

                if (correctWords.size() == userWords.size()) {
                    questionToDisplay.isAnswered = true;
                    System.out.println("you answered all the blanks!");

                    int correctNo = 0;

                    for (int i = 0; i < correctWords.size(); i++) {
                        if (correctWords.get(i).equals(userWords.get(i))) {
                            correctNo++;
                        }
                    }

                    ((Ftb) questionToDisplay).setUserGotCorrect(correctNo);
                }

            }


        } else {
            System.out.println("the question type is not of either type?");
        }


        return returnVal;
    }

    public void calculateScore(ArrayList<Question> answeredQuestions) {

        int scqTally = 0;
        int ftbTally = 0;

        int totalScqTally = 0;
        int totalFtbTally = 0;


        for (Question aq : answeredQuestions) {  //runs through all answered questions and tallies the score
            System.out.println(totalFtbTally);
            System.out.println(ftbTally);
            if (aq instanceof Scq) {
                totalScqTally++;
                if (aq.isCorrect) {
                    scqTally++;
                }
            } else if (aq instanceof Ftb) {
                totalFtbTally += ((Ftb) aq).getCorrectAnswers().size();
                ftbTally += ((Ftb) aq).getUserGotCorrect();
            }
        }

        int answeredCount = 0;

        for (Question answeredQuestion : answeredQuestions) {
            if (answeredQuestion.isAnswered) {
                answeredCount++;
            }
        }


        int noUnanswered = answeredQuestions.size() - answeredCount;



        float finalPercentage = (float) (scqTally + ftbTally) / (totalScqTally + totalFtbTally) * 100;

        System.out.println("you got " + finalPercentage + "%");
        System.out.println("you have " + noUnanswered + " unanswered questions");
    }


    public String groupBlankWords(String text) {
        Pattern pattern = Pattern.compile("<([^<>]+)>");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String word = matcher.group();

            StringBuilder blanks = new StringBuilder();

            //the -2 omits the angle brackets at each side of the found word
            for (int noOfLetters = 0; noOfLetters < word.length() - 2; noOfLetters++) {

                //will generate the length of the gap based on the length of the word being matched.
                blanks.append("_");
            }
            text = text.replace(word, blanks.toString());
        }

        return text;
    }

    public void stopTimer(Timer timer) {
        long elapsedTime = System.currentTimeMillis() - initialTime;
        long timeInSeconds = elapsedTime / 1000;

        long elapsedTimeMinutes = timeInSeconds / 60;
        long elapsedTimeSeconds = timeInSeconds % 60;
        System.out.println("your attempt took: " + elapsedTimeMinutes + " minutes and " + elapsedTimeSeconds + " seconds.");
        timer.cancel();
    }

    public void resetAnswers(ArrayList<Question> questionsToReset) {
        for (int resetIndex = 0; resetIndex < questionsToReset.size(); resetIndex++ ) {
            questionsToReset.get(resetIndex).isAnswered = false;
            questionsToReset.get(resetIndex).isCorrect = false;

            if (questionsToReset.get(resetIndex) instanceof Ftb) {

                ((Ftb) questionsToReset.get(resetIndex)).setUserGotCorrect(0);
                ((Ftb) questionsToReset.get(resetIndex)).setAnsweredBlanks(0);

                ((Ftb) questionsToReset.get(resetIndex)).resetArrays();
            }
        }

    }

}
