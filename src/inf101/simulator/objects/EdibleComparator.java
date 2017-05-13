package inf101.simulator.objects;

import java.util.Comparator;

/**
 * Compares the nutritional value of two edible objects
 * @author Einar Snorrason
 *
 */
public class EdibleComparator implements Comparator<IEdibleObject> {

	@Override
	public int compare(IEdibleObject o1, IEdibleObject o2) {
		return Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue());
	}

}
