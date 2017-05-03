package inf101.simulator;

import inf101.simulator.objects.examples.Blob;
import inf101.simulator.objects.examples.SimAnimal;
import inf101.simulator.objects.examples.SimFeed;
import inf101.simulator.objects.examples.SimRepellant;
import inf101.simulator.objects.pacman.Pacman;
import inf101.simulator.objects.pacman.Pellet;
import inf101.simulator.objects.pacman.RedGhost;
import inf101.simulator.objects.pacman.SuperPellet;

public class Setup {
	
	/** This method is called when the simulation starts */
	public static void setup(SimMain main, Habitat habitat) {
		habitat.addObject(new Pacman(new Position(400, 400), habitat));
		habitat.addObject(new RedGhost(main.randomPos(), habitat));
		//habitat.addObject(new Blob(new Direction(0), new Position(400, 400), 1));
		

		for (int i = 0; i < 3; i++)
			habitat.addObject(new SimRepellant(main.randomPos()));

		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new Pellet(pos), "Pellet", Pellet.PAINTER);
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new Pacman(pos , hab)    , "Pacman", "pipp.png");
		//SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimFeed(pos, main.getRandom().nextDouble()*2+0.5), "SimFeed™", SimFeed.PAINTER);
		//SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimRepellant(pos), "SimRepellant™",
		//		SimRepellant.PAINTER);
		SimMain.registerSimObjectFactory((Position pos,Habitat hab)->new SuperPellet(pos), "Super Pellet", SuperPellet.PAINTER);
		//SimMain.registerSimObjectFactory((Position pos,  Habitat hab) -> new SimAnimal(pos,hab),"SimAnimal", "pipp.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new RedGhost(pos,hab), "Red ghost", SimRepellant.PAINTER);
	}

	/**
	 * This method is called for each step, you can use it to add objects at
	 * random intervals
	 */
	public static void step(SimMain main, Habitat habitat) {
		if (main.getRandom().nextInt(300) == 0)
			//habitat.addObject(new SimFeed(main.randomPos(), main.getRandom().nextDouble()*2+0.5));
			habitat.addObject(new Pellet(main.randomPos()));
	}
}
