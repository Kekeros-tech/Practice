package com.company;

import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class OperationWithPrioritiesByHeirsTest {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    @Test
    public void testOfPriorities() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 15:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 15:00");

        Recourse firstRecourse = new Recourse("15-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("18-08-2021 09:00", "18-08-2021 16:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("19-08-2021 09:00", "19-08-2021 16:00");

        Recourse secondRecourse = new Recourse("16-08-2021 09:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);

        IOperation first0 = new OperationWithPriorityNew();
        first0.setNameOfOperation("Первая операция, первой серии");
        first0.setResourceGroup(allRecourses);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        IOperation second0 = new OperationWithPriorityNew();
        second0.setNameOfOperation("Вторая операция, первой серии");
        second0.setResourceGroup(allRecourses);
        second0.setDurationOfExecution(Duration.ofHours(4));
        second0.setOperatingMode(0);


        ArrayList<IOperation> operationsOfFirstSeries = new ArrayList<>();
        operationsOfFirstSeries.add(first0);
        operationsOfFirstSeries.add(second0);

        Series firstSeries = new Series(operationsOfFirstSeries, "01-09-2021 00:00", "15-08-2021 09:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);


        IOperation first1 = new OperationWithPriorityNew();
        first1.setNameOfOperation("Первая операция, второй серии");
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(4));
        first1.setOperatingMode(0);

        IOperation second1 = new OperationWithPriorityNew();
        second1.setNameOfOperation("Вторая операция, второй серии");
        second1.setResourceGroup(justFirstRecourse);
        second1.setDurationOfExecution(Duration.ofHours(6));
        second1.setOperatingMode(0);

        IOperation third1 = new OperationWithPriorityNew();
        third1.setNameOfOperation("Третья операция, второй серии");
        third1.setResourceGroup(justFirstRecourse);
        third1.setDurationOfExecution(Duration.ofHours(6));
        third1.setOperatingMode(0);

        IOperation fourth1 = new OperationWithPriorityNew();
        fourth1.setNameOfOperation("Четвертая операция, второй серии");
        fourth1.setResourceGroup(justFirstRecourse);
        fourth1.setDurationOfExecution(Duration.ofHours(6));
        fourth1.setOperatingMode(0);

        IOperation fifth1 = new OperationWithPriorityNew();
        fifth1.setNameOfOperation("Пятая операция, второй серии");
        fifth1.setResourceGroup(justFirstRecourse);
        fifth1.setDurationOfExecution(Duration.ofHours(6));
        fifth1.setOperatingMode(0);

        IOperation sixth1 = new OperationWithPriorityNew();
        sixth1.setNameOfOperation("Шестая операция, второй серии");
        sixth1.setResourceGroup(justFirstRecourse);
        sixth1.setDurationOfExecution(Duration.ofHours(6));
        sixth1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        first1.addFollowingOperation(third1);
        first1.addFollowingOperation(fourth1);
        first1.addFollowingOperation(fifth1);
        first1.addFollowingOperation(sixth1);


        ArrayList<IOperation> operationsOfSecondSeries = new ArrayList<>();
        operationsOfSecondSeries.add(first1);
        operationsOfSecondSeries.add(second1);
        operationsOfSecondSeries.add(third1);
        operationsOfSecondSeries.add(fourth1);
        operationsOfSecondSeries.add(fifth1);
        operationsOfSecondSeries.add(sixth1);

        Series secondSeries = new Series(operationsOfSecondSeries, "25-09-2021 00:00", "15-08-2021 11:00");
        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);
        third1.setSerialAffiliation(secondSeries);
        fourth1.setSerialAffiliation(secondSeries);
        fifth1.setSerialAffiliation(secondSeries);
        sixth1.setSerialAffiliation(secondSeries);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(secondSeries.getDeadlineForCompletion());

        OperationsArrangementAlgorithmWithCPAndFutureFrontNew algo =
                new OperationsArrangementAlgorithmWithCPAndFutureFrontNew(seriesToWork,
                        new ControlParameters(1,1,1),
                        Duration.ofHours(2));

        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 500); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                reader = new BufferedReader(new InputStreamReader(System.in));
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String serverWord = in.readLine();
                String[] nextParam = serverWord.split(" ");
                ArrayList<Integer> param = new ArrayList<>();
                for(int i = 0; i < nextParam.length; i++) {
                    param.add(Integer.parseInt(nextParam[0]));
                }
                //System.out.println("Вы что-то хотели сказать? Введите это здесь:");
                // если соединение произошло и потоки успешно созданы - мы можем
                //  работать дальше и предложить клиенту что то ввести
                // если нет - вылетит исключение
                //String word = reader.readLine(); // ждём пока клиент что-нибудь
                // не напишет в консоль
                System.out.println(nextParam.length);
                System.out.println(param.get(0));
                System.out.println(param.get(1));

                OperationsArrangementAlgorithmWithCPAndFutureFrontNew algoNew =
                        new OperationsArrangementAlgorithmWithCPAndFutureFrontNew(seriesToWork,
                                new ControlParameters(param.get(1),1,1),
                                Duration.ofSeconds(param.get(0)));
                Duration resDuration = Duration.between(OA_byEGO.findEarliestStartTime(seriesToWork),
                        OA_byEGO.findTimeOfLatestOperation(seriesToWork));
                out.write(resDuration.getSeconds() + "\n"); // отправляем сообщение на сервер
                out.flush();
                //String serverWord = in.readLine(); // ждём, что скажет сервер
                //System.out.println(serverWord); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

