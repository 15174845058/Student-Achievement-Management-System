import java.util.*;
import java.io.*;

class Student implements Comparable<Student> {
    private String studentId;
    private String name;
    private String gender;
    private String className;
    private int[] scores;
    private int totalScore;

    public Student(String studentId, String name, String gender, String className, int[] scores) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.className = className;
        this.scores = scores;
        this.totalScore = calculateTotalScore();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getClassName() {
        return className;
    }

    public int[] getScores() {
        return scores;
    }

    public int getTotalScore() {
        return totalScore;
    }

    private int calculateTotalScore() {
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return sum;
    }

    @Override
    public int compareTo(Student other) {
        // 按总分降序排序
        int result = Integer.compare(other.totalScore, this.totalScore);
        if (result == 0) {
            // 总分相同则按学号升序排序
            result = this.studentId.compareTo(other.studentId);
        }
        return result;
    }
}

public class StudentManagementSystem {
    private static final String FILE_PATH = "students.txt";
    private static ArrayList<Student> students;

    public static void main(String[] args) {
        students = new ArrayList<>();
        loadDataFromFile();
        showMenu();
    }

    private static void loadDataFromFile() {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String studentId = data[0];
                String name = data[1];
                String gender = data[2];
                String className = data[3];
                int[] scores = new int[3];
                for (int i = 0; i < 3; i++) {
                    scores[i] = Integer.parseInt(data[4 + i]);
                }
                Student student = new Student(studentId, name, gender, className, scores);
                students.add(student);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new file.");
            saveDataToFile();
        }
    }

    private static void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new File(FILE_PATH))) {
            for (Student student : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(student.getStudentId()).append(",");
                sb.append(student.getName()).append(",");
                sb.append(student.getGender()).append(",");
                sb.append(student.getClassName()).append(",");
                for (int score : student.getScores()) {
                    sb.append(score).append(",");
                }
                sb.deleteCharAt(sb.length() - 1); // 删除最后一个逗号
                writer.println(sb.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to save data to file.");
        }
    }

    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("======= 学生信息管理系统 =======");
            System.out.println("1. 录入学生成绩");
            System.out.println("2. 输出所有学生成绩");
            System.out.println("3. 删除学生成绩");
            System.out.println("4. 成绩查询");
            System.out.println("5. 统计");
            System.out.println("6. 退出");
            System.out.print("请输入选项：");
            int option = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (option) {
                case 1:
                    enterStudentScore(scanner);
                    break;
                case 2:
                    outputAllScores();
                    break;
                case 3:
                    deleteStudentScore(scanner);
                    break;
                case 4:
                    searchScore(scanner);
                    break;
                case 5:
                	showStatisticsSubMenu(scanner);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("无效选项，请重新输入。");
                    break;
            }
        }
        saveDataToFile();
        scanner.close();
    }

    private static void enterStudentScore(Scanner scanner) {
        System.out.print("请输入学号：");
        String studentId = scanner.nextLine();
        System.out.print("请输入姓名：");
        String name = scanner.nextLine();
        System.out.print("请输入性别：");
        String gender = scanner.nextLine();
        System.out.print("请输入班级：");
        String className = scanner.nextLine();
        int[] scores = new int[3];
        System.out.print("请输入数据结构课程成绩：");
        scores[0] = scanner.nextInt();
        System.out.print("请输入Linux编程课程成绩：");
        scores[1] = scanner.nextInt();
        System.out.print("请输入算法设计与分析课程成绩：");
        scores[2] = scanner.nextInt();    
        scanner.nextLine(); // 读取换行符

        Student student = new Student(studentId, name, gender, className, scores);
        students.add(student);
        System.out.println("学生信息录入成功！");
    }

    private static void outputAllScores() {
        if (students.isEmpty()) {
            System.out.println("暂无学生成绩。");
            return;
        }
        System.out.println("按某门课程成绩降序输出：");
        System.out.print("【1：数据结构，2：Linux编程，3:算法设计与分析】\n请输入要排序的课程（1-3）：");

        Scanner scanner = new Scanner(System.in);
        int courseIndex = scanner.nextInt();
        scanner.nextLine(); // 读取换行符

        if (courseIndex < 1 || courseIndex > 3) {
            System.out.println("无效的课程索引。");
            return;
        }

        ArrayList<Student> sortedStudents = new ArrayList<>(students);
        Collections.sort(sortedStudents, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Integer.compare(s2.getScores()[courseIndex - 1], s1.getScores()[courseIndex - 1]);
            }
        });

        for (Student student : sortedStudents) {
            System.out.println(student.getName() + "：" + student.getScores()[courseIndex - 1]);
        }
    }

    private static void deleteStudentScore(Scanner scanner) {
        System.out.print("请输入要删除学生的学号：");
        String studentId = scanner.nextLine();
        boolean found = false;
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (student.getStudentId().equals(studentId)) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (found) {
            System.out.println("学生信息删除成功！");
        } else {
            System.out.println("未找到该学生的信息。");
        }
    }

    private static void searchScore(Scanner scanner) {
        System.out.println("请选择查询方式：");
        System.out.println("1. 根据姓名查询学生成绩");
        System.out.println("2. 根据班级查询学生成绩");
        System.out.print("请输入选项：");
        int option = scanner.nextInt();
        scanner.nextLine(); // 读取换行符

        switch (option) {
            case 1:
                searchByStudentName(scanner);
                break;
            case 2:
                searchByClassName(scanner);
                break;
            default:
                System.out.println("无效选项，请重新输入。");
                break;
        }
    }

    private static void searchByStudentName(Scanner scanner) {
        System.out.print("请输入学生姓名：");
        String name = scanner.nextLine();
        boolean found = false;

        for (Student student : students) {
            if (student.getName().equals(name)) {
                outputStudentScore(student);
                found = true;
            }
        }

        if (!found) {
            System.out.println("未找到该学生的信息。");
        }
    }

    private static void searchByClassName(Scanner scanner) {
        System.out.print("请输入班级名称：");
        String className = scanner.nextLine();
        ArrayList<Student> matchedStudents = new ArrayList<>();

        for (Student student : students) {
            if (student.getClassName().equals(className)) {
                matchedStudents.add(student);
            }
        }

        if (matchedStudents.isEmpty()) {
            System.out.println("未找到该班级的学生成绩。");
        } else {
            Collections.sort(matchedStudents);
            System.out.println("按总分降序输出该班级的学生成绩：");
            for (Student student : matchedStudents) {
                outputStudentScore(student);
            }
        }
    }
    private static void showStatisticsSubMenu(Scanner scanner) {
        boolean validOption = false;
        while (!validOption) {
            System.out.println("请选择统计方式：");
            System.out.println("a. 统计每个学生的平均分，按平均分降序输出");
            System.out.println("b. 统计每个班级总分的平均分，按平均分降序输出");
            System.out.println("c. 统计每个班中总分最高的前三名和总分最低的后三名，并输出");
            System.out.println("d. 统计获得奖学金的学生信息");
            System.out.print("请输入选项（a/b/c/d）：");
            char subOption = scanner.nextLine().charAt(0);

            switch (subOption) {
                case 'a':
                    statisticsByAverageScore();
                    validOption = true;
                    break;
                case 'b':
                    statisticsByClassAverageScore();
                    validOption = true;
                    break;
                case 'c':
                    statisticsTopAndBottomThree();
                    validOption = true;
                    break;
                case 'd':
                    statisticsScholarshipStudents();
                    validOption = true;
                    break;
                default:
                    System.out.println("无效选项，请重新输入。");
                    break;
            }
        }
    }

        /*if (students.isEmpty()) {
            System.out.println("暂无学生成绩。");
        }*/
    private static int getAverageScore(Student student) {
        return student.getTotalScore() / 3;
    }  
    
    private static void statisticsByAverageScore() {
    	ArrayList<Student> sortedStudentsByAverage = new ArrayList<>(students);
        Collections.sort(sortedStudentsByAverage, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Integer.compare(getAverageScore(s2), getAverageScore(s1));
            }
        });
        System.out.println("每个学生的平均分（降序）：");
        for (Student student : sortedStudentsByAverage) {
            System.out.println(student.getName() + "：" + getAverageScore(student));
        }
    }


    private static void statisticsByClassAverageScore() {
        HashMap<String, Integer> classTotalScores = new HashMap<>();
        HashMap<String, Integer> classStudentCounts = new HashMap<>();
        for (Student student : students) {
            String className = student.getClassName();
            int totalScore = classTotalScores.getOrDefault(className, 0) + student.getTotalScore();
            int studentCount = classStudentCounts.getOrDefault(className, 0) + 1;
            classTotalScores.put(className, totalScore);
            classStudentCounts.put(className, studentCount);
        }
        ArrayList<Map.Entry<String, Integer>> sortedClassAverageScores = new ArrayList<>(classTotalScores.entrySet());
        Collections.sort(sortedClassAverageScores, (e1, e2) -> {
            int averageScore1 = e1.getValue() / classStudentCounts.get(e1.getKey());
            int averageScore2 = e2.getValue() / classStudentCounts.get(e2.getKey());
            return Integer.compare(averageScore2, averageScore1);
        });
        System.out.println("每个班级的平均分（降序）：");
        for (Map.Entry<String, Integer> entry : sortedClassAverageScores) {
            String className = entry.getKey();
            int totalScore = entry.getValue();
            int studentCount = classStudentCounts.get(className);
            int averageScore = totalScore / studentCount;
            System.out.println(className + "：" + averageScore);
        }
    }
    
    private static void statisticsTopAndBottomThree() {
        HashMap<String, List<Student>> classMap = new HashMap<>();
        for (Student student : students) {
            String className = student.getClassName();
            if (!classMap.containsKey(className)) {
                classMap.put(className, new ArrayList<>());
            }
            classMap.get(className).add(student);
        }
        for (String className : classMap.keySet()) {
            List<Student> classStudents = classMap.get(className);
            Collections.sort(classStudents);
            System.out.println("班级：" + className);
            System.out.println("总分最高的前三名：");
            for (int i = 0; i < Math.min(3, classStudents.size()); i++) {
                outputStudentScore(classStudents.get(i));
            }
            System.out.println("总分最低的后三名：");
            for (int i = Math.max(0, classStudents.size() - 3); i < classStudents.size(); i++) {
                outputStudentScore(classStudents.get(i));
            }
        }
    }

   
    private static void statisticsScholarshipStudents() {
        System.out.println("获得奖学金的学生信息：");
        for (Student student : students) {
            if (isEligibleForScholarship(student)) {
                outputStudentScore(student);
            }
        }
    }

    private static boolean isEligibleForScholarship(Student student) {
        if (getAverageScore(student) >= 90) {
            for (int score : student.getScores()) {
                if (score < 85) {
                    return false;
                }
            }
            ArrayList<Student> classStudents = new ArrayList<>();
            for (Student s : students) {
                if (s.getClassName().equals(student.getClassName())) {
                    classStudents.add(s);
                }
            }
            Collections.sort(classStudents);
            int rank = classStudents.indexOf(student) + 1;
            return rank <= 3;
        }
        return false;
    }

    private static void outputStudentScore(Student student) {
        System.out.println("学号：" + student.getStudentId());
        System.out.println("姓名：" + student.getName());
        System.out.println("性别：" + student.getGender());
        System.out.println("班级：" + student.getClassName());
        System.out.println("成绩：");
        
        int[] scores = student.getScores();
        String[] subjects = {"数据结构", "Linux编程", "算法设计与分析"};
        
        for (int i = 0; i < scores.length; i++) {
            System.out.println(subjects[i] + "课程：" + scores[i]);
        }
        
        System.out.println("总分：" + student.getTotalScore());
        System.out.println("--------------------");
    }

 }
