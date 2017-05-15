package app.learning.bigdata.khadre.abdou.diop.sensorlearningapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Abdou Khadre DIOP
 * @since  22/02/2017
 * This class represent all vallues get from a specific user
 */

public class AllValues {
    // the name of the user
    private String user;

    // the kind of data it can be sitting, walking running etc
    private String label;

    // the list of values
    private ArrayList<AcceleratorValues> listAcceleratorValues;

    /**
     * this function is used to add a group of values(Accelerator values) il the list of values
     * @param values the values
     */
    public void addValues(AcceleratorValues values) {
        listAcceleratorValues.add(values);
    }

    /**
     * this function return list of accelerators values
     * @return list of values
     */
    public ArrayList<AcceleratorValues> getListAcceleratorValues() {
        return listAcceleratorValues;
    }

    public AllValues(String user,String label) {
        this.user=user;
        this.label=label;
        listAcceleratorValues=new ArrayList<AcceleratorValues>();
    }
}
