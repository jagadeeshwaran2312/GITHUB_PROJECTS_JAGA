import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.util.InputMismatchException;

class Student{
    private String name,dept,year,dob,college;
    private long reg_no;
    private int no_sem_completed;
    private float CGPA = 0.0f;
    private float[] Grades;
    private float[] Credits;

    // public parameterized constructor to get the student details.
    public Student(String name,long reg_no,String dept,String year,String dob,int no_sem_completed,String college,Scanner input){
        this.name = name;
        this.reg_no = reg_no;
        this.dept = dept;
        this.year = year;
        this.dob = dob.replace("/","-");
        this.no_sem_completed = no_sem_completed;
        this.college = college;
        Grades = new float[no_sem_completed];
        Credits = new float[no_sem_completed];
        this.calculateCGPA(input);
        this.displayreport();
    }

    //Display method to diplay the student CGPA report.
    public void displayreport(){
        System.out.println("\n======= CGPA report for student  ========== \n");
        System.out.println("NAME : "+name);
        System.out.println("REGISTER NUMBER : "+reg_no);
        System.out.println("DATE OF BIRTH : "+dob);
        System.out.println("DEPARTMENT : "+dept);
        System.out.println("YEAR : "+year);
        System.out.println("COLLEGE : "+college);
        System.out.println("\n======= CGPA report ========== \n");
        
        float gradeSum = 0.0f, creditSum = 0.0f;
        float running_CGPA = 0.0f;
        for(int i = 1; i <= no_sem_completed; i++){
            gradeSum += Grades[i-1];
            creditSum += Credits[i-1];
            if(creditSum == 0){
                running_CGPA = 0.0f;
            }
            else{
                running_CGPA = gradeSum / creditSum;
            }
            float semGPA = (Credits[i-1] == 0) ? 0.0f : Grades[i-1] / Credits[i-1];
            System.out.printf("\tGPA for semester %d is : %.2f\t\t|",i,semGPA);
            System.out.printf("\tCGPA after semester %d is %.2f\n",i,running_CGPA);
        }
        System.out.printf("\n======= Final CGPA : %.2f =========\n",CGPA);
    }

    //Grade convertion Grades into Grade points
    private int GradeConvertion(String gradeinstring){

        switch(gradeinstring.toUpperCase()){
            case "O":
                return 10;
            case "A+":
                return 9;
            case "A":
                return 8;
            case "B+":
                return 7;
            case "B":
                return 6;
            case "U":
                return 0;
            default :
                return -1;
        }
    }

    //Method to calculate CGPA of a student.
    public void calculateCGPA(Scanner input){
        float gradeSum = 0.0f, creditSum = 0.0f;

        //GPA calculation for each semester.
        for(int i = 1; i <= no_sem_completed; i++){
            System.out.println("===================== GPA calculation for semester "+ i + " =========================");
            int no_subjects;
            System.out.print("Enter no of subjects for semester " + i +" : ");
            no_subjects = input.nextInt();
            input.nextLine();
            Grades[i-1] = 0.0f;
            Credits[i-1] = 0.0f;
            float tempGrade = 0, tempCredits = 0, GradeMulCredits = 0;
            for(int j = 1; j <= no_subjects; j++){

                //Grade input for a subject.
                while(true){
                    try{
                        System.out.print("Enter Grade for subject " + j + " : ");
                        String gradeinsString = input.nextLine();
                        tempGrade = GradeConvertion(gradeinsString);
                        if(tempGrade == -1.0f){
                            System.out.println("Enter valid grades! ");
                            continue;
                        }
                        else if((tempGrade >= 6.0f && tempGrade <= 10.0f) || tempGrade == 0.0f){
                            break;
                        }
                    }catch(Exception e){
                            System.out.println("Enter grades in like '0', 'A+', 'A'...etc., ");
                            input.nextLine();
                    }
                }
                
                //Credit input for a subject.
                while (true) {
                    try {
                        System.out.print("Enter credits for subject " + j + " : ");
                        tempCredits = input.nextFloat();

                        if ((tempCredits >= 1.0f && tempCredits <= 4.0f) || tempCredits == 10.0f) {
                            input.nextLine();
                            break;
                        } else {
                            System.out.println("Enter valid credits between 1 to 4 (or 10 for final project)");
                        }
                    } catch (Exception e) {
                        System.out.println("Enter credits in numbers only");
                        input.nextLine();
                    }
                }

                GradeMulCredits = tempGrade * tempCredits; 
                //Calculating total Grade points for each semester.
                Grades[i-1] += GradeMulCredits; 
                //Calculating total Credit points for each semester.
                Credits[i-1] += tempCredits;
            }

            //Sum of the Grade poionts and Credits for CGPA calculation.
            gradeSum += Grades[i-1];
            creditSum += Credits[i-1];
        }
        if(creditSum == 0){
            CGPA = 0.0f;
        }else{
            //CGPA
            CGPA = gradeSum / creditSum;
        }
    }

    //Method to create a new CSV file for a new student.
    public void createNewCsvFile(){
        try{
            FileWriter fwrite = new FileWriter(name.trim().replace(" ","_")+" "+dob+".csv");
            fwrite.write(name+","+reg_no+","+year+","+dept+","+dob+","+no_sem_completed+","+CGPA+","+college+"\n");
            for(int i = 1; i <= no_sem_completed; i++){
                fwrite.write(Grades[i-1]+",");
            }
            fwrite.write("\n");
            
            for(int i = 1; i <= no_sem_completed; i++){
                fwrite.write(Credits[i-1]+",");
            }

            fwrite.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    //Public parameterized constructor to read the Data from a CSV file.
    public Student(String name,String dob) {
        try(BufferedReader bfread = new  BufferedReader(new FileReader(name.replace(" ","_")+" "+dob.replace("/","-")+".csv"));){
            
            String[] data = bfread.readLine().split(",");

            this.name = data[0];
            this.reg_no = Long.parseLong(data[1]);
            this.year = data[2];
            this.dept = data[3];
            this.dob = data[4];
            no_sem_completed  = Integer.parseInt(data[5]);
            CGPA = Float.parseFloat(data[6]);
            this.college = data[7];

            String[] grades = bfread.readLine().split(",");
            String[] credits = bfread.readLine().split(",");

            Grades = new float[no_sem_completed];
            Credits = new float[no_sem_completed];
            for(int j = 0; j < no_sem_completed; j++){
                Grades[j] = Float.parseFloat(grades[j]);
                Credits[j] = Float.parseFloat(credits[j]);
            }
        displayreport();
        }
        catch(Exception e){
            System.out.println("========= File not found! =========");
        }
    }
}

public class CGPAManagementSystem {
    public static void main(String[] args){

        String name,dept,year,dob,college;
        long reg_no = 0;
        int no_sem_completed = 0;
        
        System.out.println("========== Welcome to CGPA management system ==========");
        int choice;
        Scanner sc = new Scanner(System.in);
        while(true){
            
            System.out.println("1. CALCULATE CGPA FOR A NEW STUDENT ");
            System.out.println("2. DISPLAY CGPA REPORT FOR A STUDENT ALREADY CGPA WAS CALCULATED");
            System.out.println("3. EXIT");
            

            System.out.print("Enter your choice : ");
            if(!sc.hasNextInt()){
                System.out.println("Enter valid choice number");
                sc.nextLine();
                continue;
            }
            choice = sc.nextInt();
            sc.nextLine();
            
            switch(choice){
                case 1:
                    System.out.print("Enter name : ");
                    name = sc.nextLine();
                    while(true){
                        try{
                            System.out.print("Enter register number : ");
                            reg_no = sc.nextLong();
                            sc.nextLine();
                            if(reg_no <= 0){
                                System.out.println("Enter valid register number (greater than 0)");
                            } else {
                                break;
                            }
                        }catch(Exception e){
                            System.out.println("Enter valid register number (in numbers only)");
                            sc.nextLine();
                        }
                    }
                    System.out.print("Enter department : ");
                    dept = sc.nextLine();
                    System.out.print("Enter year of study : ");
                    year = sc.nextLine();
                    System.out.print("Enter date of birth [use slash '/' or '-' in dob] (dd-mm-yyyy) : ");
                    dob = sc.nextLine();
                    while(true){
                        try{
                            System.out.print("Enter no of semester completed : ");
                            no_sem_completed = sc.nextInt();
                            if(no_sem_completed <= 0 || no_sem_completed > 8 ){
                                System.out.println("Enter valid number of semesters (between 1 and 8)");
                            } else {
                                break;
                            }
                        }catch(InputMismatchException e){
                            System.out.println("Enter valid number of semesters (in numbers only)");
                            sc.nextLine();
                        }
                    }
                    sc.nextLine();
                    System.out.print("Enter college name : ");
                    college = sc.nextLine();
                    Student std = new Student(name,reg_no,dept,year,dob,no_sem_completed,college,sc);
                    std.createNewCsvFile();
                    break;
                
                case 2:
                    System.out.print("Enter name : ");
                    name = sc.nextLine();
                    System.out.print("Enter date of birth [warning !Don't use slash '/' in dob] (dd-mm-yyyy) : ");
                    dob = sc.nextLine();
                    new Student(name.trim(),dob);
                    break;

                case 3:
                    System.out.println("Exited successfully");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}