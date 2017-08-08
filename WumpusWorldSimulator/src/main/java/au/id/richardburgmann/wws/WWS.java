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

package au.id.richardburgmann.wws;

public enum WWS { YES, NO,
    FIXED, RANDOM,
    // Entities of the world.
    ADVENTURER, WUMPUS, PIT, BREEZE, STENCH, GOLD,
    // Property keys
    SET_ADVENTURERS_BRAIN, SET_ADVENTURERS_HEALTH,
    LOAD_TRAINED_BRAIN,
    DIR_LOCATION_OF_BRAIN_Q_VALUES,
    LOAD_BRAIN_Q_VALUES_FROM,
    PERSIST_TRAINED_BRAIN_AFTER_RUN,
    SAVE_BRAIN_Q_VALUES_TO

}
