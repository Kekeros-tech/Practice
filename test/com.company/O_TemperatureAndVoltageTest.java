package com.company;

import com.company.operation.IOperation;
import com.company.operation.O_TemperatureAndVoltage;
import com.company.operation.O_Basic;
import com.company.recourse.electric_furnace.CellElectricFurnace;
import com.company.recourse.electric_furnace.CellWithVoltage;
import com.company.recourse.electric_furnace.R_ElectricFurnace;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;

public class O_TemperatureAndVoltageTest {

    @Test
    public void firstEasyTestByOneOperation() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        CellWithVoltage cellWithVoltage = new CellWithVoltage(10, 2, "14-08-2021 09:00");
        ArrayList<CellWithVoltage> cells = new ArrayList<>();
        cells.add(cellWithVoltage);
        CellElectricFurnace cellElectricFurnace = new CellElectricFurnace(10, cells);

        R_ElectricFurnace firstRecourse = new R_ElectricFurnace("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);
        firstRecourse.addCellElectricFurnace(cellElectricFurnace);

        Group groupOfRes = new Group();
        groupOfRes.addRecourseInTheGroup(firstRecourse);

        O_Basic OBasic = new O_TemperatureAndVoltage(10, 10);
        OBasic.setResourceGroup(groupOfRes);
        OBasic.setDurationOfExecution(Duration.ofHours(10));
        OBasic.setOperatingMode(1);

        O_Basic OBasicForSecondSeries = new O_TemperatureAndVoltage(10, 10);
        OBasicForSecondSeries.setResourceGroup(groupOfRes);
        OBasicForSecondSeries.setDurationOfExecution(Duration.ofHours(4));
        OBasicForSecondSeries.setOperatingMode(0);

        ArrayList<IOperation> operationsForFirstSeries = new ArrayList<>();
        operationsForFirstSeries.add(OBasic);

        ArrayList<IOperation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(OBasicForSecondSeries);

        Series firstSeries = new Series(operationsForFirstSeries, "01-09-2021 00:00", "15-08-2021 09:00");
        OBasic.setSerialAffiliation(firstSeries);

        Series secondSeries = new Series(operationsForSecondSeries, "01-09-2021 00:00", "15-08-2021 10:00");
        OBasicForSecondSeries.setSerialAffiliation(secondSeries);

        ArrayList<Series> series = new ArrayList<>();
        series.add(firstSeries);
        series.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(series);

        operationsForFirstSeries.add(OBasicForSecondSeries);

        for(IOperation current: operationsForFirstSeries) {
            //System.out.println(current.getCNumberOfAssignedRecourse());
            System.out.println(current);
        }


    }
}
