package Animals;

import Field.*;

import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling.  Modified by David Dobervich 2007-2022
 */
public class Rabbit extends Animal {
    // ----------------------------------------------------
    // Characteristics shared by all rabbits (static fields).
    // ----------------------------------------------------
    private static int RABBIT_BREEDING_AGE = 5;

    // The age to which all rabbits can live.
    private static int RABBIT_MAX_AGE = 30;

    // The likelihood of a rabbit breeding.
    private static double RABBIT_BREEDING_PROBABILITY = 0.06;

    // The maximum number of births.
    private static int RABBIT_MAX_LITTER_SIZE = 5;

    // -----------------------------------------------------
    // Individual characteristics (attributes).
    // -----------------------------------------------------
    // The rabbit's age.
    private int RABBIT_AGE = 0;

    // Whether the rabbit is alive or not.
    private boolean RABBIT_ALIVE = true;

    // The rabbit's position
    private Location location;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     *
     * @param startWithRandomAge If true, the rabbit will have a random age.
     */
    public Rabbit(boolean startWithRandomAge) {
        super(5, 30, 0.6, 5, 0, true);
        RABBIT_AGE = 0;
        if (startWithRandomAge) {
            RABBIT_AGE = (int) (Math.random() * RABBIT_MAX_AGE);
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     *
     * @param updatedField      The field to transfer to.
     * @param babyRabbitStorage A list to add newly born rabbits to.
     */
    public void run(Field updatedField, List<Rabbit> babyRabbitStorage) {
        incrementAge();
        if (RABBIT_ALIVE) {
            int births = breed();
            for (int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                babyRabbitStorage.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(location);
                newRabbit.setLocation(loc);
                updatedField.put(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(location);
            // Only transfer to the updated field if there was a free location
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.put(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                RABBIT_ALIVE = false;
            }
        }
    }
}

