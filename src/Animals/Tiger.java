package Animals;

import Animals.*;
import Field.*;
import Graph.*;

import java.io.Serializable;
import java.util.List;

/**
 * A simple model of a tiger. Tigers age, move, eat foxs, and die.
 *
 * @author David J. Barnes and Michael Kolling.  Modified by David Dobervich 2007-2022
 */
public class Tiger extends Animal {
    // ----------------------------------------------------
    // Characteristics shared by all tigeres (static fields).
    // ----------------------------------------------------
    private static int BREEDING_AGE = 3;
    // The age to which a tiger can live.
    private static int MAX_AGE = 50;
    // The likelihood of a tiger breeding.
    private static double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE = 6;
    // The food value of a single fox. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static int TIGER_FOOD_VALUE = 8;
    // A shared random number generator to control breeding.

    // The tiger's food level, which is increased by eating foxes.
    private int foodLevel;

    /**
     * Create a tiger. A tiger can be created as a new born (age zero and not
     * hungry) or with random age.
     *
     * @param startWithRandomAge
     *            If true, the tiger will have random age and hunger level.
     */
    public Tiger(boolean startWithRandomAge) {
        super(3,50, 0.15, 6, 0, true);
        age = 0;
        if (startWithRandomAge) {
            age = (int)(Math.random()*MAX_AGE);
            foodLevel = (int)(Math.random()*TIGER_FOOD_VALUE);
        } else {
            // leave age at 0
            foodLevel = TIGER_FOOD_VALUE;
        }
    }

    /**
     * This is what the tiger does most of the time: it hunts for foxs. In the
     * process, it might breed, die of hunger, or die of old age.
     *
     * @param currentField
     *            The field currently occupied.
     * @param updatedField
     *            The field to transfer to.
     * @param babyTigerStorage
     *            A list to add newly born tigeres to.
     */
    public void hunt(Field currentField, Field updatedField, List<Tiger> babyTigerStorage) {
        incrementAge();
        incrementHunger();
        if (alive) {
            // New tigers are born into adjacent locations.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Tiger newTiger = new Tiger(false);
                newTiger.setFoodLevel(this.foodLevel);
                babyTigerStorage.add(newTiger);
                Location loc = updatedField.randomAdjacentLocation(location);
                newTiger.setLocation(loc);
                updatedField.put(newTiger, loc);
            }

            Location newLocation = findFood(currentField, location);
            if (newLocation == null) { // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.put(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations
                // taken
                alive = false;
            }
        }
    }

    /**
     * Make this tiger more hungry. This could result in the tiger's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            alive = false;
        }
    }

    /**
     * Tell the tiger to look for foxs adjacent to its current location. Only
     * the first live fox is eaten.
     *
     * @param field
     *            The field in which it must look.
     * @param location
     *            Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */

    private Location findFood(Field field, Location location) {
        List<Location> adjacentLocations = field.adjacentLocations(location);

        for (Location where : adjacentLocations) {
            Object animal = field.getObjectAt(where);
            if (animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if (fox.isAlive()) {
                    fox.setEaten();
                    foodLevel = TIGER_FOOD_VALUE;
                    return where;
                }
            }
        }

        return null;
    }


    public void setFoodLevel(int fl) {
        this.foodLevel = fl;
    }
}
