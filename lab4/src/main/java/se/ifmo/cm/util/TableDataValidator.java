package se.ifmo.cm.util;

import se.ifmo.cm.Exception.NotDistinctPointsException;

import javax.management.InvalidAttributeValueException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TableDataValidator {
    private final Object[][] tableData;

    public TableDataValidator(Object[][] tableData) {
        this.tableData = tableData;
    }

    public Object[][] validate() throws NotDistinctPointsException {
        Set<Double> set = new LinkedHashSet<>();
        int n = 0;
        for (Object[] tableDatum : tableData) {
            if (tableDatum[0] != null && tableDatum[1] != null) {
                double wrongDataFormatThrower = Double.parseDouble(tableDatum[0].toString());
                wrongDataFormatThrower = Double.parseDouble(tableDatum[1].toString());
                n++;
            } else {
                break;
            }
        }

        Object[][] validatedData = new Object[n][2];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tableData[i], 0, validatedData[i], 0, 2);
            set.add(Double.parseDouble(tableData[i][0].toString()) + Double.parseDouble(tableData[i][1].toString()));
        }

        if (set.size() != validatedData.length) {
            throw new NotDistinctPointsException("Some points have same coordinates");
        }
        return validatedData;
    }
}
