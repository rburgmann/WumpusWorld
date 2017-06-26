package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 14/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoOrdinate {
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
}
