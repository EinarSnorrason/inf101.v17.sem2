package inf101.simulator;

import inf101.simulator.objects.examples.Blob;
import inf101.simulator.objects.examples.SimAnimal;
import inf101.simulator.objects.examples.SimFeed;
import inf101.simulator.objects.examples.SimRepellant;
import inf101.simulator.objects.pacman.BlueGhost;
import inf101.simulator.objects.pacman.Pacman;
import inf101.simulator.objects.pacman.Pellet;
import inf101.simulator.objects.pacman.PinkGhost;
import inf101.simulator.objects.pacman.RedGhost;
import inf101.simulator.objects.pacman.Scoreboard;
import inf101.simulator.objects.pacman.SuperPellet;
import inf101.simulator.objects.pacman.YellowGhost;

public class Setup {
	
	/** This method is called when the simulation starts */
	public static void setup(SimMain main, Habitat habitat) {
		pacmanStart(main,habitat);

		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new Pellet(pos), "Pellet", Pellet.PAINTER);
		SimMain.registerSimObjectFactory((Position pos,Habitat hab)->new SuperPellet(pos), "Super Pellet", SuperPellet.PAINTER);
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new Pacman(pos , hab)    , "Pacman", "pacman2.png");
		//SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimFeed(pos, main.getRandom().nextDouble()*2+0.5), "SimFeed™", SimFeed.PAINTER);
		//SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimRepellant(pos), "SimRepellant™",
		//		SimRepellant.PAINTER);
		//SimMain.registerSimObjectFactory((Position pos,  Habitat hab) -> new SimAnimal(pos,hab),"SimAnimal", "pipp.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new RedGhost(pos,hab), "Red ghost", "redGhost4.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new PinkGhost(pos,hab), "Pink ghost", "pinkGhost4.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new BlueGhost(pos,hab), "Blue ghost", "blueGhost4.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new YellowGhost(pos,hab), "Yellow ghost", "yellowGhost4.png");
		
	}
	
	/** This method is called when the simulation restarts */
	public static void restart(SimMain main, Habitat habitat){
		habitat.removeAll();
		pacmanStart(main,habitat);
	}

	/**
	 * This method is called for each step, you can use it to add objects at
	 * random intervals
	 */
	public static void step(SimMain main, Habitat habitat) {
		if (habitat.allObjects().size() < 50){
			if (main.getRandom().nextInt(20) == 0){
				habitat.addObject(new SuperPellet(main.randomPos())) ;
			} else {
				habitat.addObject(new Pellet(main.randomPos()));
			}
		}
		
	}
	
	/**
	 * Adds all objects needed for pacman game
	 * @param main
	 * @param habitat
	 */
	private static void pacmanStart(SimMain main, Habitat habitat){
		habitat.addObject(new Pacman(new Position(400, 400), habitat));
		habitat.addObject(new Scoreboard(new Position(0,habitat.getHeight()-habitat.getHeight()/20), habitat));
		habitat.addObject(new RedGhost(main.randomPos(), habitat));
		habitat.addObject(new BlueGhost(main.randomPos(), habitat));
		habitat.addObject(new PinkGhost(main.randomPos(), habitat));
		habitat.addObject(new YellowGhost(main.randomPos(), habitat));
		//habitat.addObject(new Blob(new Direction(0), new Position(400, 400), 1));
		
		habitat.addObject(new SuperPellet(main.randomPos()));
		for (int i = 0; i < 10; i++)
			habitat.addObject(new Pellet(main.randomPos()));
	}
}
