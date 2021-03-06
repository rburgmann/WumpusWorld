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

package au.id.richardburgmann.wws.brains;

import au.id.richardburgmann.wws.TheWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomChoice implements Brain {
    private static final Logger logger = LoggerFactory.getLogger(RandomChoice.class);

    @Override
    public int think(TheWorld state) {
        logger.debug("think() Getting a legal random move for now.");
        return state.getALegalRandomMove(state.getEntityLocation(TheWorld.ADVENTURER));
    }

    @Override
    public TheWorld learn(TheWorld state, int action, int reward) {
        return state;
    }

    @Override
    public void persistBrain() {
        // Do nothing as this brain has no persistance.
    }

    @Override
    public Brain loadBrain() {
        return new RandomChoice();
    }

    @Override
    public void brainDump() {
    }
}
