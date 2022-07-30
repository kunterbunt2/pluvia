/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

/**
 *
 * 9 rows of 4 columns, 6 different stones, with up to 3 stones that can fall at the same time
 *
 *
 */
public class OneGameBird extends Game {
	public OneGameBird() {
		super("Bird", 4, 9, 3, 6, 0, 0, 7.2f, false);
		description = "9 rows of 4 columns\r\n"//
				+ "6 different stones\r\n"//
				+ "with up to 3 stones that can fall at the same time\r\n";
	}

}