import java.util.ArrayList;
import java.util.Scanner;


public class MenuClass {
    private Scanner scan = new Scanner(System.in);

    private ArrayList<Module> modules = new ArrayList<>();  //stores all created modules

    public void mainMenu() {

            System.out.println("// MAIN MENU //");
            System.out.println("Are you a teacher(t) or student(s)?");
            System.out.println("press q to quit");
            String input = scan.nextLine().toLowerCase();


            switch(input) {
                case "t":
                    this.teacherMenu();
                    break;
                case "s":
                    this.studentMenu();
                    break;
                case "q":
                    //uses system.exit to break out of infinite loop in Application.run() method
                    System.out.println("Thank you for using the program!");
                    System.exit(0);
                default:
                    System.out.println("invalid input, try again");
            }

    }






    public void teacherMenu() {
        System.out.println("// TEACHER MENU //");
        System.out.println("Add question bank (a)");
        System.out.println("Add questions to existing bank (b)");
        System.out.println("list and remove questions from existing bank (c)");
        System.out.println("delete empty question bank (d)");
        System.out.println("create module(e)");
        System.out.println("delete a module(f)");
        System.out.println("go back(x)");

        String teacherInput = scan.nextLine();


        switch(teacherInput) {

                case "a":
                    Module accessedModule1 = selectModule();
                    if (accessedModule1 == null) {              //if a module doesn't exist
                        System.out.println("There aren't any modules available, create a module first!");
                    } else {
                        accessedModule1.addQuestionBank();
                    }
                    break;


                case "b":
                    if (modules.isEmpty()) {
                        System.out.println("no modules to choose a question bank from!");
                    } else {
                        Module accessedModule2 = selectModule();
                        QuestionBank selectedQB = accessedModule2.selectQuestionBank();
                        selectedQB.addQuestion();
                    }
                    break;


                case "c":
                    if (modules.isEmpty()) {
                        System.out.println("no modules to select from");
                    } else {
                        Module accessedModule3 = selectModule();
                        if (accessedModule3.getQuestionBanks().isEmpty()) {
                            System.out.println("no question banks to delete questions from");
                        } else {
                            QuestionBank selectedQB2 = accessedModule3.selectQuestionBank();
                            selectedQB2.deleteQuestion();
                        }
                    }
                    break;

                case "d":
                    Module accessedModule4 = selectModule();
                    accessedModule4.deleteQuestionBank();
                    break;


                case "e":
                    createModule();
                    break;


                case "f":
                    deleteModule();
                    break;

                case "x":
                    mainMenu();
                    break;


                default:
                    System.out.println("invalid input, try again");
                    teacherMenu();
                    break;
            }
    }





    public void studentMenu() {
        System.out.println("// STUDENT MENU //");
        System.out.println("take quiz(q)");
        System.out.println("go back(x)");
        String studentInput = scan.nextLine().toLowerCase();
            if (studentInput.equals("q")) {
                Module selectedModule = selectModule();
                QuestionBank selectedQuestionBank = selectedModule.selectQuestionBank();
                Quiz newQuiz = new Quiz();
                newQuiz.runQuiz(selectedQuestionBank);
            } else if (studentInput.equals("x")) {
                mainMenu();
            } else {
                System.out.println("invalid input, try again");
            }
    }


    public Module selectModule() {
        String moduleNumber;
        int moduleNumberAsInt;
        Module accessedModule = null;

            if (modules.isEmpty()) {
                return null;
            } else {
                System.out.println("select module: ");
                int length = modules.size();
                for (int i = 0; i < length; i++) {
                    Module currentModule = modules.get(i);
                    System.out.println(currentModule.getModuleIdentifier() + "(" + i + ")");
                }
                System.out.println("cancel (x)");
                moduleNumber = scan.nextLine();

                //try parsing input as integer
                try {
                    moduleNumberAsInt = Integer.parseInt(moduleNumber);
                    accessedModule = modules.get(moduleNumberAsInt);
                } catch (NumberFormatException e) {
                    if (moduleNumber.equals("x")) {
                        //return null can be used, however recursion can be used since select module is only called from the teachermenu method.
                        teacherMenu();
                    } else {
                        System.out.println("invalid input");
                        selectModule();
                    }
                }
            }

        return accessedModule;
    }






    public void createModule() {
        System.out.println("enter an identifier for the module");
        System.out.println("go back (x)");
        String input = scan.nextLine();
        boolean idFound = false;
        boolean exitFlag = false;

        while (!exitFlag) {

            ArrayList<String> modIdsToCompare = new ArrayList<>();

            for (Module module : modules) {
                modIdsToCompare.add(module.getModuleIdentifier());
            }


            for (String userModId : modIdsToCompare) {
                if (userModId.equals(input)) {
                    idFound = true;
                    break;
                }
            }

            if (input.equals("x")) {
                exitFlag = true;
                teacherMenu();
            } else if (idFound) {
                System.out.println("A module with this name already exists, try again");
            } else {
                modules.add(new Module(input));
                Module lastAddedModule = modules.getLast();
                System.out.println("added new module with identifier: " + lastAddedModule.getModuleIdentifier());
                exitFlag = true;
            }
        }
    }





    public void deleteModule() {
        if (modules.isEmpty()) {
            System.out.println("no modules available to delete ");
            System.out.println("returning to main menu...");
            mainMenu();
        } else {
            System.out.println("select a module to delete");
            for (int i = 0; i < modules.size(); i++) {
                System.out.println(modules.get(i).getModuleIdentifier() + "(" + i + ")");
            }
            System.out.println("go back (x)");
            String userInput = scan.nextLine();

            try {
                int userInputInt = Integer.parseInt(userInput);

                if (userInputInt > modules.size()) {
                    System.out.println("not a valid integer, try again");
                    deleteModule();
                } else {
                    Module toBeDeleted = modules.get(userInputInt);
                    if (!toBeDeleted.getQuestionBanks().isEmpty()) {
                        System.out.println("module is not empty - proceed? (y/n)");
                        String doubleCheck = scan.nextLine().toLowerCase();
                        if (doubleCheck.equals("n")) {
                            System.out.println("skipping deletion, returning to main menu...");
                            mainMenu();
                        }  //no option for "y" because the module is deleted after breaking out of the if statement
                    }

                    //prints before actually deleting the module so identifier can be accessed before it's deletion
                    System.out.println("deleted module " + modules.get(userInputInt).getModuleIdentifier());
                    modules.remove(modules.get(userInputInt));
                }
            } catch (NumberFormatException e) {
                if (userInput.equals("x")) {
                    teacherMenu();
                } else {
                    System.out.println("invalid input");
                    selectModule();
                }
            }
        }
    }




}
