package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 14/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;

public class CoOrdinate implements Comparator<CoOrdinate>, Comparable<CoOrdinate> {
    private static final Logger logger = LoggerFactory.getLogger(CoOrdinate.class);
    public int row = 0;
    public int col = 0;


    public String toCSV() {
        String s = new String(row + "," + col);
        return s;
    }

    public boolean collision(CoOrdinate otherXY) {
        if ((this.row == otherXY.row) && (this.col == otherXY.col)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean collision(ArrayList<CoOrdinate> otherXY) {

        for (int i = 0; i < otherXY.size(); i++) {
            if ((this.row == otherXY.get(i).row) && (this.col == otherXY.get(i).col)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object o) {

        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;

        CoOrdinate oXY = (CoOrdinate) o;

        if ((this.row == oXY.row) && (this.col == oXY.col)) {
            return true;
        } else {
            return false;
        }
    }
    public int compare(CoOrdinate o1, CoOrdinate o2) {
        System.out.println(o1.row + " " + o2.row);
        return ((o1.row - o2.row) * TheWorld.GRID_SIZE) + (o1.col - o2.col);
    }
    public int compareTo(CoOrdinate o) {
        return ((this.row - o.row) * TheWorld.GRID_SIZE) + (this.col - o.col);
    }
}
