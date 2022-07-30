/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

/**
 *
 * 13 rows of 5 columns, 8 different stones, with up to 3 stones that can fall at the same time
 *
 * @author kunterbunt
 *
 */
public class TreeGameTurtle extends Game {
	public TreeGameTurtle() {
		super(GameName.Turtle.name(), 5, 13, 3, 8, 0, 0, 11, false);
		description = " * 13 rows of 5 columns\r\n"//
				+ " * 8 different stones\r\n"//
				+ " * with up to 2 stones that can fall at the same time\r\n";
	}

}