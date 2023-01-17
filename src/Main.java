import exception.IncorrectTaskParameterException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern DATE_TIME_PATTERN=Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}\\d{2}\\.\\d{2}");
    private static final Pattern DATE_PATTERN=Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
    /*private static final DateTimeFormatter DATE_TIME_FORMATTER=DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm");*/
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
          scanner.useDelimiter("\n");
            label:
            while (true){
                printMenu();
                System.out.println("Выберите пункт меню: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            addTask(scanner);
                            break;
                        case 2:
                            removeTask(scanner);
                            break;
                        case 3:
                            printTaskByDay(scanner);
                            break;
                        case 0:
                            break label;
                    }
                }else{
                        scanner.next();
                        System.out.println("Выберите пункт меню из списка!");
                    }
                }
            }

    private static void printTaskByDay(Scanner scanner) {

       do {

           System.out.println("Введите дату в формате: DD.MM.YYYY");
           if (scanner.hasNext(DATE_PATTERN)) {
               LocalDate day = parseDate(scanner.next(DATE_PATTERN));
               if (day==null){
                   System.out.println("НЕкорректный формат даты ");
                   continue;
               }

               Collection<Task>tasksByDay =TaskService.getTasksByDay(day);
               System.out.println("Задачи на "+day.format(Constant.DATE_FORMATTER));
               for (Task task: tasksByDay){
                   System.out.println(task);
               }
               break;
           } else {
               scanner.next();
           }
       } while (true);


    }
    private static void removeTask(Scanner scanner) {
        try {
            do {

                System.out.println("Введите id задачи: ");
                if (scanner.hasNextInt()) {
                    int id = scanner.nextInt();
                    TaskService.removeById(id);
                    System.out.println("задача с id= "+id+" удалена");
                    break;
                } else {
                    scanner.next();
                }
            } while (true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void addTask(Scanner scanner) {
        try {
            System.out.println("Введите название задачи: ");
            String title = scanner.next();
            System.out.println("Введите описание задачи: ");
            String description = scanner.next();
            Type type = inputType(scanner);
            LocalDateTime dateTime = inputDateTime(scanner);
            Repeatability repeatability = inputRepeatability(scanner);
            Task task = new Task(title, description,type, dateTime, repeatability);
            TaskService.add(task);
            System.out.println("Задача добавлена!");
            System.out.println(task);
        } catch (IncorrectTaskParameterException e) {
            System.out.println(e.getMessage());;
        }
    }


    private static Type inputType(Scanner scanner){
        Type type;
        do {

            System.out.println("Введите тип задачи: 1.Личная\n2.Рабочая\nТип задачи: ");
            if (scanner.hasNextInt()){
                int number=scanner.nextInt();
                if (number!=1&&number!=2){
                    System.out.println("Введите 1 или 2");
                    continue;
                }
                if (number==1){
                    type= Type.PERSONAL;
                }else {
                    type=Type.WORK;
                }
                break;
            }else {
                scanner.next();
            }
        }while (true);
        return type;
    }
    private static LocalDateTime inputDateTime(Scanner scanner){
        LocalDateTime dateTime;
        do {

            System.out.println("Введите дату и время задачи: " +
                    "dd.mm.yyyy hh:mm ");
            if (scanner.hasNext(DATE_TIME_PATTERN)){
                 dateTime=parseDateTime(scanner.next(DATE_TIME_PATTERN));
                if (dateTime==null){
                    System.out.println("НЕкорректный формат даты и времени");
                    continue;
                }
                break;
            }else {
                scanner.next();
            }
        }while (true);
        return dateTime;
    }

    private static LocalDateTime parseDateTime(String dateTime){
        try {
            return LocalDateTime.parse(dateTime, Constant.DATE_TIME_FORMATTER);
        }catch (DateTimeParseException e){
            return null;
        }
    }

    private static LocalDate parseDate(String date){
        try {
            return LocalDate.parse(date, Constant.DATE_FORMATTER);
        }catch (DateTimeParseException e){
            return null;
        }
    }

    private static Repeatability inputRepeatability(Scanner scanner){
        Repeatability repeatability;
        do {
            System.out.println("Введите тип повторяемости задачи: 1.Однократная\n2.Ежедневная\n3.Еженедельная\n4.Ежемесячная\n5.Ежегодная Тип повторяемости задачи: ");
            if (scanner.hasNextInt()){
                int number=scanner.nextInt();
                if (number<1||number>5){
                    System.out.println("Введите цифру от 1 до 5");
                    continue;
                }
                switch (number){
                    default:
                    case 1:
                        repeatability=new OneTime();
                        break;
                    case 2:
                        repeatability=new Daily();
                        break;
                    case 3:
                        repeatability=new Weekly();
                        break;
                    case 4:
                        repeatability=new Montly();
                        break;
                    case 5:
                        repeatability=new Yearly();
                        break;
                }
                break;
            }else {
                scanner.next();
            }
        }while (true);
        return repeatability;
    }
    private static void printMenu(){
    System.out.println("" +
            "1.Добавить задачу\n" +
            "2.Удалить задачу\n" +
            "3.Получить задачу из указанный день\n" +
            "0.Выход");

}

}
