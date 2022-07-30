/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game.model.stone;

import de.bushnaq.abdalla.engine.Renderable;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.util.RcBoolean;

/**
 * @author bushnaq TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class Stone extends Renderable implements Comparable<Stone> {
	private RcBoolean	canDrop			= new RcBoolean(false);
	private RcBoolean	canMoveLeft		= new RcBoolean(false);
	private RcBoolean	canMoveRight	= new RcBoolean(false);
	private RcBoolean	cannotDrop		= new RcBoolean(false);
	private RcBoolean	cannotMoveLeft	= new RcBoolean(false);
	private RcBoolean	cannotMoveRight	= new RcBoolean(false);
	private RcBoolean	dropping		= new RcBoolean(false);
	private RcBoolean	leftAttached	= new RcBoolean(false);
	private RcBoolean	movingLeft		= new RcBoolean(false);
	private RcBoolean	movingRight		= new RcBoolean(false);
	private RcBoolean	pushingLeft		= new RcBoolean(false);
	private RcBoolean	pushingRight	= new RcBoolean(false);
	private RcBoolean	rightAttached	= new RcBoolean(false);
	public int			score			= 0;
	public float		tx				= 0;
	public float		ty				= 0;
	public int			type			= 0;
	private RcBoolean	vanishing		= new RcBoolean(false);
	public int			x				= 0;
	public int			y				= 0;
	public int			z				= 0;

	public Stone(GameEngine gameEngine, int x, int y, int aType) {
		set3DRenderer(new Stone3DRenderer(this));
		type = aType;
		this.x = x;
		this.y = y;
		get3DRenderer().create(gameEngine);
	}

	public boolean clearCommandAttributes() {
		boolean		somethingHasChanged	= false;
		RcBoolean	attributeList[]		= { dropping, movingLeft, movingRight };
		for (RcBoolean element : attributeList) {
			if (element.getBooleanValue()) {
				element.setBooleanValue(false);
				somethingHasChanged = true;
			}
		}
		return somethingHasChanged;
	}

	public boolean clearPushingAttributes() {
		boolean		somethingHasChanged	= false;
		RcBoolean	attributeList[]		= { pushingLeft, pushingRight };
		for (RcBoolean element : attributeList) {
			if (element.getBooleanValue()) {
				element.setBooleanValue(false);
				somethingHasChanged = true;
			}
		}
		return somethingHasChanged;
	}

	public void clearTemporaryAttributes() {
		RcBoolean attributeList[] = { cannotDrop };
		for (RcBoolean element : attributeList) {
			element.setBooleanValue(false);
		}
	}

	@Override
	public int compareTo(Stone o) {
		int	v1	= x + y * 100;
		int	v2	= o.x + o.y * 100;
		if (v1 < v2) {
			return -1;
		} else if (v1 == v2) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (this.getClass() != o.getClass()))
			return false;
		Stone stone = (Stone) o;
		return x == stone.x && y == stone.y;
	}

	public String getCanAttributesAsString() {
		String attribute = "CAN:";
		if (isCanMoveLeft())
			attribute += "L";
		else
			attribute += "-";
		if (isCanDrop())
			attribute += "D";
		else
			attribute += "-";
		if (isCanMoveRight())
			attribute += "R";
		else
			attribute += "-";
		if (isVanishing())
			attribute += "V";
		else
			attribute += "-";
		return attribute;
	}

	public String getCannotAttributesAsString() {
		String attribute = "CANNOT:";
		if (isCannotMoveLeft())
			attribute += "L";
		else
			attribute += "-";
		if (isCannotDrop())
			attribute += "D";
		else
			attribute += "-";
		if (isCannotMoveRight())
			attribute += "R";
		else
			attribute += "-";
		return attribute;
	}

	public String getDoingStatusAsString() {
		String attribute = "DOING :";
		if (isPushingLeft())
			attribute += "P";
		else
			attribute += "-";
		if (isPushingRight())
			attribute += "P";
		else
			attribute += "-";
		if (isMovingLeft())
			attribute += "M";
		else
			attribute += "-";
		if (isDropping())
			attribute += "D";
		else
			attribute += "-";
		if (isMovingRight())
			attribute += "M";
		else
			attribute += "-";
		return attribute;
	}

	public String getGlueStatusAsString() {
		String attribute = "GLUE:";
		if (isLeftAttached())
			attribute += "L";
		else
			attribute += "-";
		if (isRightAttached())
			attribute += "R";
		else
			attribute += "-";
		return attribute;
	}

	public int getType() {
		return type;
	}

	public boolean isCanDrop() {
		return canDrop.getBooleanValue();
	}

	public boolean isCanMoveLeft() {
		return canMoveLeft.getBooleanValue();
	}

	public boolean isCanMoveRight() {
		return canMoveRight.getBooleanValue();
	}

	public boolean isCannotDrop() {
		return cannotDrop.getBooleanValue();
	}

	public boolean isCannotMoveLeft() {
		return cannotMoveLeft.getBooleanValue();
	}

	public boolean isCannotMoveRight() {
		return cannotMoveRight.getBooleanValue();
	}

	public boolean isDropping() {
		return dropping.getBooleanValue();
	}

	public boolean isLeftAttached() {
		return leftAttached.getBooleanValue();
	}

	public boolean isMoving() {
		return isDropping() || isMovingLeft() || isMovingRight();
	}

	public boolean isMovingLeft() {
		return movingLeft.getBooleanValue();
	}

	public boolean isMovingRight() {
		return movingRight.getBooleanValue();
	}

	public boolean isPushingLeft() {
		return pushingLeft.getBooleanValue();
	}

	public boolean isPushingRight() {
		return pushingRight.getBooleanValue();
	}

	public boolean isRightAttached() {
		return rightAttached.getBooleanValue();
	}

	public boolean isVanishing() {
		return vanishing.getBooleanValue();
	}

	public void setCanDrop(boolean aCanDrop) {
		canDrop.setBooleanValue(aCanDrop);
	}

	public void setCanMoveLeft(boolean aCanMoveLeft) {
		canMoveLeft.setBooleanValue(aCanMoveLeft);
	}

	public void setCanMoveRight(boolean aCanMoveRight) {
		canMoveRight.setBooleanValue(aCanMoveRight);
	}

	public void setCannotDrop(boolean aCannotDrop) {
		cannotDrop.setBooleanValue(aCannotDrop);
	}

	public void setCannotMoveLeft(boolean aCannotMoveLeft) {
		cannotMoveLeft.setBooleanValue(aCannotMoveLeft);
	}

	public void setCannotMoveRight(boolean aCannotMoveRight) {
		cannotMoveRight.setBooleanValue(aCannotMoveRight);
	}

	public void setDropping(boolean aDropping) {
		dropping.setBooleanValue(aDropping);
	}

	public void setisVanishing(boolean aCanVanish) {
		vanishing.setBooleanValue(aCanVanish);
	}

	public void setLeftAttached(boolean aLeftAttached) {
		leftAttached.setBooleanValue(aLeftAttached);
	}

	public void setMovingLeft(boolean aMovingLeft) {
		movingLeft.setBooleanValue(aMovingLeft);
	}

	public void setMovingRight(boolean aMovingRight) {
		movingRight.setBooleanValue(aMovingRight);
	}

	public void setPushingLeft(boolean aPushingLeft) {
		pushingLeft.setBooleanValue(aPushingLeft);
	}

	public void setPushingRight(boolean aPushingRight) {
		pushingRight.setBooleanValue(aPushingRight);
	}

	public void setRightAttached(boolean aRightAttached) {
		rightAttached.setBooleanValue(aRightAttached);
	}

	public void setTx(float tx) {
		this.tx = tx;
	}

	public void setTy(float ty) {
		this.ty = ty;
	}

}