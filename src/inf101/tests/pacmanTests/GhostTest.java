package inf101.tests.pacmanTests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.SimEvent;
import inf101.simulator.objects.pacman.IGhost;
import inf101.simulator.objects.pacman.Pacman;
import inf101.simulator.objects.pacman.Pellet;
import inf101.simulator.objects.pacman.RedGhost;
import inf101.simulator.objects.pacman.SuperPellet;
import inf101.tests.testHelpers.TestGhost;

public class GhostTest {
	private SimMain main;
	private Habitat hab;

	@Before
	public void setUp() {
		this.main= new SimMain();
			
	}
	
	/**
	 * Tests whether ghost kills pacman
	 */
	@Test
	public void killPacmanTest(){
		hab = new Habitat(main,100,100);
		IGhost ghost = new RedGhost(new Position(50,50),hab);
		Pacman pacman = new Pacman(new Position(51,50),hab);
		hab.addObject(pacman);
		hab.addObject(ghost);
		hab.step();
		assertTrue(pacman.isDead());
	}
	
	
	/**
	 * Tests whether ghost becomes scared when pacman eats super pellet
	 */
	@Test
	public void becomesScaredTest(){
		hab = new Habitat(main,1000,1000);
		IGhost ghost = new RedGhost(new Position(50,50),hab);
		Pacman pacman = new Pacman(new Position(500,500),hab);
		SuperPellet pellet = new SuperPellet(new Position(501,500));
		hab.addObject(pacman);
		hab.addObject(ghost);
		hab.addObject(pellet);
		hab.step();
		assertTrue(ghost.isScared());
	}
	
	/**
	 * Tests that ghost can see pacman
	 */
	@Test
	public void canSeePacmanTest(){
		hab = new Habitat(main,1000,1000);
		RedGhost ghost = new RedGhost(new Position(50,50),hab);
		Pacman pacman = new Pacman(new Position(300,50),hab);
		hab.addObject(pacman);
		hab.addObject(ghost);
		ghost.turnTowards(pacman.getDirection(), 180);
		assertEquals(pacman.getDirection(),ghost.getDirection());

		assertEquals(pacman,ghost.findPacman());
	}
	
	/**
	 * Tests that ghost can sense pacman behind it
	 */
	@Test
	public void canSensePacmanTest(){
		hab = new Habitat(main,1000,1000);
		RedGhost ghost = new RedGhost(new Position(100,50),hab);
		Pacman pacman = new Pacman(new Position(20,50),hab);
		hab.addObject(pacman);
		hab.addObject(ghost);
		hab.step();
		assertEquals(pacman,ghost.findPacman());
	}
	
	/**
	 * Test that ghost cannot see pacman far behind it
	 */
	@Test
	public void cannotSeePacmanBehindTest(){
		hab = new Habitat(main,1000,1000);
		RedGhost ghost = new RedGhost(new Position(300,50),hab);
		Pacman pacman = new Pacman(new Position(20,50),hab);
		hab.addObject(pacman);
		hab.addObject(ghost);
		ghost.turnTowards(pacman.getDirection(), 180);
		hab.step();
		assertEquals(null,ghost.findPacman());
	}
	
	/**
	 * Pacman kills ghosts that are scared
	 */
	@Test
	public void pacmanKillsScaredGhost(){
		hab = new Habitat(main,100,100);
		RedGhost ghost = new RedGhost(new Position(50,50),hab);
		Pacman pacman = new Pacman(new Position(51,50),hab);
		hab.addObject(pacman);
		hab.addObject(ghost);
		hab.triggerEvent(new SimEvent(pacman,"PowerUp",null,null));
		hab.step();
		assertTrue(ghost.isDead());
	}
}
