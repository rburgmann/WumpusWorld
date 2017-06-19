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
    public int x = 0;
    public int y = 0;


    public String toCSV() {
        String s = new String(x + "," + y + ",");
        return s;
    }
}
