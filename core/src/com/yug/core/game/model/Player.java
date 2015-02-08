package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.yug.core.game.GameWorld;
import com.yug.pf.NavigationPoint;
import com.yug.pf.PathFinder;

import java.util.LinkedList;

/**
 * Created by yugine on 22.1.15.
 */
public class Player extends MovableTile
{
    private enum Direction
    {
        LEFT, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    private final float DEFAULT_SPEED = 96;
    private final GameWorld gameWorld;
    private Texture texture;
    private PathFinder pathFinder;
    private LinkedList<NavigationPoint> path;
    private NavigationPoint nextNavigationPoint;
    private boolean moving;
    private Direction direction;

    public Player(final GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
        setSpeed(DEFAULT_SPEED);
        pathFinder = new PathFinder();
        texture = createTestTexture();

    }

    @Override
    public void update(float deltaT)
    {
        if (moving)
        {
            if (nextNavigationPoint == null)
            {
                if (path != null && path.size() > 0)
                {
                    nextNavigationPoint = path.pop();
                    updateDirection();
                    updatePosition(deltaT);
                }
                else
                {
                    moving = false;
                }
            }
            else
            {
                updatePosition(deltaT);
            }
        }
    }

    private void updateDirection()
    {
        final int nextCol = nextNavigationPoint.getX();
        final int nextRow = nextNavigationPoint.getY();
        final int colDelta = nextCol - getCol();
        final int rowDelta = nextRow - getRow();
        if (colDelta < 0 && rowDelta == 0)
        {
            direction = Direction.LEFT;
        }
        else if(colDelta>0 && rowDelta ==0)
        {
            direction = Direction.RIGHT;
        }
        else if(colDelta == 0 && rowDelta <0)
        {
            direction = Direction.DOWN;
        }
        else if(colDelta == 0 && rowDelta>0)
        {
            direction = Direction.UP;
        }
        else if(colDelta<0 && rowDelta<0)
        {
            direction = Direction.DOWN_LEFT;
        }
        else if(colDelta<0 && rowDelta>0)
        {
            direction = Direction.UP_LEFT;
        }
        else if(colDelta>0 && rowDelta<0)
        {
            direction = Direction.DOWN_RIGHT;
        }
        else if(colDelta>0 && rowDelta>0)
        {
            direction = Direction.UP_RIGHT;
        }
    }

    private void updatePosition(float deltaT)
    {
        if (nextNavigationPoint == null)
        {
            return;
        }
        final int nextCol = nextNavigationPoint.getX();
        final int nextRow = nextNavigationPoint.getY();

        final float nextPointX = nextCol * gameWorld.getTileWidth();
        final float nextPointY = nextRow * gameWorld.getTileHeight();
        setCol(nextCol);
        setRow(nextRow);
        float newX = getX();
        float newY = getY();
        if (Direction.LEFT.equals(direction))
        {
            //moving left
            newX = getX() - getSpeed() * deltaT;
            if (newX <= nextPointX)
            {
                newX = nextPointX;
            }
        }
        else if(Direction.RIGHT.equals(direction))
        {
            //moving right
            newX = getX() + getSpeed() * deltaT;
            if (newX >= nextPointX)
            {
                newX = nextPointX;
            }
        }
        else if(Direction.DOWN.equals(direction))
        {
            //moving down
            newY = getY() - getSpeed()*deltaT;
            if(newY <=nextPointY)
            {
                newY = nextPointY;
            }
        }
        else if(Direction.UP.equals(direction))
        {
            //moving up
            newY = getY() + getSpeed()*deltaT;
            if(newY >=nextPointY)
            {
                newY = nextPointY;
            }
        }
        else if(Direction.DOWN_LEFT.equals(direction))
        {
            //moving down left
            newX = getX() - getSpeed() * deltaT;
            newY = getY() - getSpeed()*deltaT;
            if (newX <= nextPointX)
            {
                newX = nextPointX;
            }
            if(newY <=nextPointY)
            {
                newY = nextPointY;
            }
        }
        else if(Direction.UP_LEFT.equals(direction))
        {
            //moving up left
            newX = getX() - getSpeed() * deltaT;
            newY = getY() + getSpeed()*deltaT;
            if (newX <= nextPointX)
            {
                newX = nextPointX;
            }
            if(newY >=nextPointY)
            {
                newY = nextPointY;
            }
        }
        else if(Direction.DOWN_RIGHT.equals(direction))
        {
            //moving down right
            newX = getX() + getSpeed() * deltaT;
            newY = getY() - getSpeed()*deltaT;
            if (newX >= nextPointX)
            {
                newX = nextPointX;
            }
            if(newY <=nextPointY)
            {
                newY = nextPointY;
            }
        }
        else if(Direction.UP_RIGHT.equals(direction))
        {
            //moving up right
            newX = getX() + getSpeed() * deltaT;
            newY = getY() + getSpeed()*deltaT;
            if (newX >= nextPointX)
            {
                newX = nextPointX;
            }
            if(newY >=nextPointY)
            {
                newY = nextPointY;
            }
        }
        if (newX == nextPointX && newY == nextPointY)
        {
            nextNavigationPoint = null;
        }
        setX(newX);
        setY(newY);
    }

    public void goTo(final int destCol, final int destRow)
    {
        if(getCol()!=destCol || getRow()!=destRow)
        {
            path = pathFinder.calculatePath(getCol(), getRow(), destCol, destRow, gameWorld.getNavigationMap());

            if (moving && path != null)
            {
                //force updating the nextNavigationPoint
                //nextNavigationPoint = path.pop();
                //updateDirection();
            }
            else if (!moving && path != null)
            {
                moving = true;
            }
        }
    }

    public boolean isMoving()
    {
        return moving;
    }

    public void setMoving(final boolean moving)
    {
        this.moving = moving;
    }

    public Texture getTexture()
    {
        return texture;
    }

    //creating test texture
    private Texture createTestTexture()
    {
        final Pixmap pixmap = createTestPixmap(32, 32);
        return new Texture(pixmap);
    }

    //creating test texture
    private Pixmap createTestPixmap(final int width, final int height)
    {
        final Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        // Fill square with red color at 50% opacity
        pixmap.setColor(1, 0, 0, .5f);
        pixmap.fill();
        // Draw a yellow-colored X shape on square
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        // Draw a cyan-colored border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

}
