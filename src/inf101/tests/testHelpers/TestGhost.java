package inf101.tests.testHelpers;

import inf101.simulator.Direction;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.pacman.IGhost;
import inf101.simulator.objects.pacman.Pacman;

/**
 * Simple ghost used for testing purposes
 * @author Einar Snorrason
 *
 */
public class TestGhost extends AbstractSimObject implements IGhost{
	
	public void setScared(boolean scared) {
		this.scared = scared;
	}

	private boolean scared = false;

	public TestGhost(Position pos) {
		super(new Direction(0), pos);
	}

	@Override
	public double eat(double howMuch) {
		return 0;
	}

	@Override
	public double getNutritionalValue() {
		return scared ? 200 : 0;
	}

	@Override
	public double getHeight() {
		return 0;
	}

	@Override
	public double getWidth() {
		return 0;
	}

	@Override
	public void step() {
		
		
	}

	@Override
	public boolean isScared() {
		return scared;
	}

	@Override
	public boolean canSee(ISimObject obj) {
		return false;
	}

	@Override
	public boolean canSense(ISimObject obj) {
		return false;
	}

	@Override
	public Pacman findPacman() {
		return null;
	}
	

}
