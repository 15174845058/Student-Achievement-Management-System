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
        // ���ֽܷ�������
        int result = Integer.compare(other.totalScore, this.totalScore);
        if (result == 0) {
            // �ܷ���ͬ��ѧ����������
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
                sb.deleteCharAt(sb.length() - 1); // ɾ�����һ������
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
            System.out.println("======= ѧ����Ϣ����ϵͳ =======");
            System.out.println("1. ¼��ѧ���ɼ�");
            System.out.println("2. �������ѧ���ɼ�");
            System.out.println("3. ɾ��ѧ���ɼ�");
            System.out.println("4. �ɼ���ѯ");
            System.out.println("5. ͳ��");
            System.out.println("6. �˳�");
            System.out.print("������ѡ�");
            int option = scanner.nextInt();
            scanner.nextLine(); // ��ȡ���з�

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
                    System.out.println("��Чѡ����������롣");
                    break;
            }
        }
        saveDataToFile();
        scanner.close();
    }

    private static void enterStudentScore(Scanner scanner) {
        System.out.print("������ѧ�ţ�");
        String studentId = scanner.nextLine();
        System.out.print("������������");
        String name = scanner.nextLine();
        System.out.print("�������Ա�");
        String gender = scanner.nextLine();
        System.out.print("������༶��");
        String className = scanner.nextLine();
        int[] scores = new int[3];
        System.out.print("���������ݽṹ�γ̳ɼ���");
        scores[0] = scanner.nextInt();
        System.out.print("������Linux��̿γ̳ɼ���");
        scores[1] = scanner.nextInt();
        System.out.print("�������㷨���������γ̳ɼ���");
        scores[2] = scanner.nextInt();    
        scanner.nextLine(); // ��ȡ���з�

        Student student = new Student(studentId, name, gender, className, scores);
        students.add(student);
        System.out.println("ѧ����Ϣ¼��ɹ���");
    }

    private static void outputAllScores() {
        if (students.isEmpty()) {
            System.out.println("����ѧ���ɼ���");
            return;
        }
        System.out.println("��ĳ�ſγ̳ɼ����������");
        System.out.print("��1�����ݽṹ��2��Linux��̣�3:�㷨����������\n������Ҫ����Ŀγ̣�1-3����");

        Scanner scanner = new Scanner(System.in);
        int courseIndex = scanner.nextInt();
        scanner.nextLine(); // ��ȡ���з�

        if (courseIndex < 1 || courseIndex > 3) {
            System.out.println("��Ч�Ŀγ�������");
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
            System.out.println(student.getName() + "��" + student.getScores()[courseIndex - 1]);
        }
    }

    private static void deleteStudentScore(Scanner scanner) {
        System.out.print("������Ҫɾ��ѧ����ѧ�ţ�");
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
            System.out.println("ѧ����Ϣɾ���ɹ���");
        } else {
            System.out.println("δ�ҵ���ѧ������Ϣ��");
        }
    }

    private static void searchScore(Scanner scanner) {
        System.out.println("��ѡ���ѯ��ʽ��");
        System.out.println("1. ����������ѯѧ���ɼ�");
        System.out.println("2. ���ݰ༶��ѯѧ���ɼ�");
        System.out.print("������ѡ�");
        int option = scanner.nextInt();
        scanner.nextLine(); // ��ȡ���з�

        switch (option) {
            case 1:
                searchByStudentName(scanner);
                break;
            case 2:
                searchByClassName(scanner);
                break;
            default:
                System.out.println("��Чѡ����������롣");
                break;
        }
    }

    private static void searchByStudentName(Scanner scanner) {
        System.out.print("������ѧ��������");
        String name = scanner.nextLine();
        boolean found = false;

        for (Student student : students) {
            if (student.getName().equals(name)) {
                outputStudentScore(student);
                found = true;
            }
        }

        if (!found) {
            System.out.println("δ�ҵ���ѧ������Ϣ��");
        }
    }

    private static void searchByClassName(Scanner scanner) {
        System.out.print("������༶���ƣ�");
        String className = scanner.nextLine();
        ArrayList<Student> matchedStudents = new ArrayList<>();

        for (Student student : students) {
            if (student.getClassName().equals(className)) {
                matchedStudents.add(student);
            }
        }

        if (matchedStudents.isEmpty()) {
            System.out.println("δ�ҵ��ð༶��ѧ���ɼ���");
        } else {
            Collections.sort(matchedStudents);
            System.out.println("���ֽܷ�������ð༶��ѧ���ɼ���");
            for (Student student : matchedStudents) {
                outputStudentScore(student);
            }
        }
    }
    private static void showStatisticsSubMenu(Scanner scanner) {
        boolean validOption = false;
        while (!validOption) {
            System.out.println("��ѡ��ͳ�Ʒ�ʽ��");
            System.out.println("a. ͳ��ÿ��ѧ����ƽ���֣���ƽ���ֽ������");
            System.out.println("b. ͳ��ÿ���༶�ֵܷ�ƽ���֣���ƽ���ֽ������");
            System.out.println("c. ͳ��ÿ�������ܷ���ߵ�ǰ�������ܷ���͵ĺ������������");
            System.out.println("d. ͳ�ƻ�ý�ѧ���ѧ����Ϣ");
            System.out.print("������ѡ�a/b/c/d����");
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
                    System.out.println("��Чѡ����������롣");
                    break;
            }
        }
    }

        /*if (students.isEmpty()) {
            System.out.println("����ѧ���ɼ���");
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
        System.out.println("ÿ��ѧ����ƽ���֣����򣩣�");
        for (Student student : sortedStudentsByAverage) {
            System.out.println(student.getName() + "��" + getAverageScore(student));
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
        System.out.println("ÿ���༶��ƽ���֣����򣩣�");
        for (Map.Entry<String, Integer> entry : sortedClassAverageScores) {
            String className = entry.getKey();
            int totalScore = entry.getValue();
            int studentCount = classStudentCounts.get(className);
            int averageScore = totalScore / studentCount;
            System.out.println(className + "��" + averageScore);
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
            System.out.println("�༶��" + className);
            System.out.println("�ܷ���ߵ�ǰ������");
            for (int i = 0; i < Math.min(3, classStudents.size()); i++) {
                outputStudentScore(classStudents.get(i));
            }
            System.out.println("�ܷ���͵ĺ�������");
            for (int i = Math.max(0, classStudents.size() - 3); i < classStudents.size(); i++) {
                outputStudentScore(classStudents.get(i));
            }
        }
    }

   
    private static void statisticsScholarshipStudents() {
        System.out.println("��ý�ѧ���ѧ����Ϣ��");
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
        System.out.println("ѧ�ţ�" + student.getStudentId());
        System.out.println("������" + student.getName());
        System.out.println("�Ա�" + student.getGender());
        System.out.println("�༶��" + student.getClassName());
        System.out.println("�ɼ���");
        
        int[] scores = student.getScores();
        String[] subjects = {"���ݽṹ", "Linux���", "�㷨��������"};
        
        for (int i = 0; i < scores.length; i++) {
            System.out.println(subjects[i] + "�γ̣�" + scores[i]);
        }
        
        System.out.println("�ܷ֣�" + student.getTotalScore());
        System.out.println("--------------------");
    }

 }
