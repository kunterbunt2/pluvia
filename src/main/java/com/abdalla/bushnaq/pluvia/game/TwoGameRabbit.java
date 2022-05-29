/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game;

/**
 * 
 * 9 rows of 4 columns, 8 different stones, with up to 2 stones that can fall at the same time
 * 
 *
 */
public class TwoGameRabbit extends Game {
	public TwoGameRabbit() {
		super(GameName.Rabbit.name(), 4, 9, 2, 8, 0, 0, 7.5f, false);
	}

}