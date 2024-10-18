package Animals;

import Animals.*;
import Field.*;
import Graph.*;

import java.io.Serializable;
import java.util.List;

/**
 * A simple model of a fox. Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kolling.  Modified by David Dobervich 2007-2022
 */
public class Fox extends Animal{

	private int foodLevel;
	private int RABBIT_FOOD_VALUE;
	private int FOX_FOOD_VALUE = 8;

	/**
	 * Create a new rabbit. A rabbit may be created with age
	 * zero (a new born) or with a random age.
	 *
	 * @param startWithRandomAge If true, the rabbit will have a random age.
	 */



	public Fox(boolean startWithRandomAge) {

		super(5,15, 0.02, 8, 0, true);
		age = 0;
		if (startWithRandomAge) {
			age = (int)(Math.random()*MAX_AGE);
			foodLevel = (int)(Math.random()* FOX_FOOD_VALUE);
		} else {
			// leave age at 0
			foodLevel = FOX_FOOD_VALUE;
		}

	}

	/**
	 * This is what the fox does most of the time: it hunts for rabbits. In the
	 * process, it might breed, die of hunger, or die of old age.
	 * 
	 * @param currentField
	 *            The field currently occupied.
	 * @param updatedField
	 *            The field to transfer to.
	 * @param babyFoxStorage
	 *            A list to add newly born foxes to.
	 */


	public void act(Field currentField, Field updatedField, List<Animal> babyFoxStorage) {
		incrementAge();
		incrementHunger();
		if (alive) {
			// New foxes are born into adjacent locations.
			int births = breed();
			for (int b = 0; b < births; b++) {
				Fox newFox = new Fox(false);
				newFox.setFoodLevel(this.foodLevel);
				babyFoxStorage.add(newFox);
				Location loc = updatedField.randomAdjacentLocation(location);
				newFox.setLocation(loc);
				updatedField.put(newFox, loc);
			}
			// Move towards the source of food if found.
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
	 * Increase the age. This could result in the fox's death.
	 */
	/**
	 * Make this fox more hungry. This could result in the fox's death.
	 */


	private void incrementHunger() {
		foodLevel--;
		if (foodLevel <= 0) {
			alive = false;
		}
	}

	/**
	 * Tell the fox to look for rabbits adjacent to its current location. Only
	 * the first live rabbit is eaten.
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
			if (animal instanceof Rabbit) {
				Rabbit rabbit = (Rabbit) animal;
				if (rabbit.isAlive()) {
					rabbit.setEaten();
					foodLevel = RABBIT_FOOD_VALUE;
					return where;
				}
			}
		}

		return null;
	}

	/**
	 * Generate a number representing the number of births, if it can breed.
	 * 
	 * @return The number of births (may be zero).
	 */


	public void setFoodLevel(int fl) {
		this.foodLevel = fl;
	}

	public void setEaten() {
		alive = false;
	}
}
