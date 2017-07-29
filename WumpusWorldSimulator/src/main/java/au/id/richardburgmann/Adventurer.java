/*
 *
 *    Copyright 2017 Richard Burgmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *
 */

package au.id.richardburgmann;

import au.id.richardburgmann.brains.Brain;
import au.id.richardburgmann.brains.RandomChoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Adventurer {
    private static final Logger logger = LoggerFactory.getLogger(Adventurer.class);
    public Brain brain = new RandomChoice();
    private int health = 1;
    private int maxHealth = 1;

    public static void main(String[] args) {

    }

    public Adventurer() {
        super();
    }

    public Adventurer(Properties applicationProperties) {
        Brain brain = extractBrainFromProperties(applicationProperties);
        setBrain(brain);

    }

    private Brain extractBrainFromProperties(Properties properties) {
        String classToUseForBrain;
        classToUseForBrain = properties.getProperty("SET_ADVENTURERS_BRAIN");
        Brain brain = new RandomChoice();

        try {
            Class theClassToUse = Class.forName(classToUseForBrain);
            brain = (Brain) theClassToUse.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (brain == null) brain = new RandomChoice();
        return brain;
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }

    private void setHealth(Properties properties) {
        Integer health = (Integer) properties.get("SET_ADVENTURERS_HEALTH");
        setHealth(health.intValue());
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health > maxHealth) {
            maxHealth = health;
        }
    }

    public int getMaxHealth() {return this.maxHealth;}

    public int think(TheWorld currState) {
        return brain.think(currState);
    }

    public TheWorld act(TheWorld currState, int action) {
        logger.debug("Adv location is ("+currState.getEntityLocation(TheWorld.ADVENTURER).toCSV()+
                ") action is " + TheWorld.ACTION_CONSTANTS[action]);
        // Is the action legal ?
        TheWorld newState = currState.cloneStateArray(currState);

        CoOrdinate myXY = newState.getEntityLocation(TheWorld.ADVENTURER);
        if (newState.isThisMoveLegal(action, myXY)) {


            newState.worldState[TheWorld.ADVENTURER][myXY.row][myXY.col] = TheWorld.EMPTY_LOCATION;
            newState.worldState[TheWorld.VISITED][myXY.row][myXY.col] = TheWorld.OCCUPIED_LOCATION;

            switch (action) {
                case 0:
                    myXY.col = moveLeft(myXY.col);
                    break;
                case 1:
                    myXY.col = moveRight(myXY.col);
                    break;
                case 2:
                    myXY.row = moveUp(myXY.row);
                    break;
                case 3:
                    myXY.row = moveDown(myXY.row);
                    break;
            }

            newState.worldState[TheWorld.ADVENTURER][myXY.row][myXY.col] = TheWorld.OCCUPIED_LOCATION;
            newState.worldState[TheWorld.VISITED][myXY.row][myXY.col] = TheWorld.OCCUPIED_LOCATION;
        }
        return newState;


    }

    public TheWorld learn(TheWorld state, int action, int reward) {
        logger.debug("learning...");
        TheWorld newState = state.cloneStateArray(state);
        int      newAction = action;
        int      newReward = reward;

        return this.brain.learn(newState, newAction, newReward);

    }

    private int moveLeft(int X_Loc) {
        if (X_Loc - 1 >= 0) {
            logger.info("Just a step to the left.");
            return (X_Loc - 1);

        } else {
            logger.info("Left grid wall. I think I'll just stand here a moment.");
            return X_Loc;
        }
    }

    private int moveRight(int X_Loc) {
        if (X_Loc + 1 < TheWorld.GRID_SIZE) {
            logger.info("Just a step to the right.");
            return (X_Loc + 1);
        } else {
            logger.info("Right grid wall. I think I'll just stand here a moment.");
            return X_Loc;

        }
    }

    private int moveUp(int Y_Loc) {
        if (Y_Loc - 1 >= 0) {
            logger.info("Moving up.");
            return (Y_Loc - 1);
        } else {
            logger.info("North grid limit. I think I'll just stand here a moment.");
            return Y_Loc;
        }
    }

    private int moveDown(int Y_Loc) {
        if (Y_Loc + 1 < TheWorld.GRID_SIZE) {
            logger.info("Moving down.");
            return (Y_Loc + 1);
        } else {
            logger.info("South grid limit. I think I'll just stand here a moment.");
            return Y_Loc;
        }

    }

}
