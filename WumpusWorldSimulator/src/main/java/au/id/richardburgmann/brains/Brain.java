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

package au.id.richardburgmann.brains;

import au.id.richardburgmann.CoOrdinate;
import au.id.richardburgmann.TheWorld;

public interface Brain {

    default CoOrdinate whereAmI(TheWorld state) {
        CoOrdinate xy = new CoOrdinate();

        for (int r = 0; r < state.GRID_SIZE; r++) {
            for (int c = 0; c < state.GRID_SIZE; c++) {
                if (state.worldState[state.ADVENTURER][r][c] == state.OCCUPIED_LOCATION) {
                    xy.row = r;
                    xy.col = c;
                    break;
                }
            }
        }
        return xy;
    }

    int think(TheWorld state);

    TheWorld learn(TheWorld state, int action, int reward);

    void brainDump();
}
