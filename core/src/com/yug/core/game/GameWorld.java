package com.yug.core.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.yug.core.Constants;
import com.yug.core.game.model.Player;
import com.yug.pf.NavigationMap;
import com.yug.pf.NavigationPoint;

import java.util.Iterator;

/**
 * The root class of game model.
 * Created by yugine on 16.1.15.
 */
public class GameWorld
{
    private final static String TM_WITH = "width";
    private final static String TM_HEIGHT = "height";
    private final static String TM_WALLS_LAYER_NAME = "walls";
    private final static String TM_TILES_LAYER_NAME = "tiles";

    private TmxMapLoader tmLoader;
    private TiledMap tiledMap;
    private TiledMapTileLayer wallsLayer;
    private TiledMapTileLayer tilesLayer;
    private int collsAmt;
    private int rowsAmt;
    private float tileWidth;
    private float tileHeight;
    private NavigationMap<NavigationPoint> navigationMap;

    private Player player;

    public GameWorld()
    {
        tmLoader = new TmxMapLoader();
        player = new Player(this);

    }

    public void loadLevel(final LevelName levelName)
    {
        tiledMap = tmLoader.load(levelName.getTmxFilePath());
        collsAmt = (Integer) tiledMap.getProperties().get(TM_WITH);
        rowsAmt = (Integer) tiledMap.getProperties().get(TM_HEIGHT);
        wallsLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TM_WALLS_LAYER_NAME);
        tilesLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TM_TILES_LAYER_NAME);
        tileHeight = wallsLayer.getTileHeight();
        tileWidth = wallsLayer.getTileWidth();

        final NavigationPoint[][] navPoints = new NavigationPoint[collsAmt][rowsAmt];
        navigationMap = new NavigationMap<NavigationPoint>(navPoints);
        loadNavigationMap();
        player.setCol(1);
        player.setRow(1);
        player.setX(player.getCol() * tileWidth);
        player.setY(player.getRow() * tileHeight);
    }

    public void loadNavigationMap()
    {
        for (int x = 0; x<collsAmt; x++)
        {
            for (int y = 0; y<rowsAmt; y++)
            {
                NavigationPoint point = navigationMap.getPoint(x, y);
                if (point == null)
                {
                    point = new NavigationPoint(x, y);
                    navigationMap.setPoint(point, x, y);
                }
                point.setWalkable(tilesLayer.getCell(x, y) != null);
            }
        }
    }

    /**
     * Returns the tiled map of the level.
     */
    public TiledMap getTiledMap()
    {
        return tiledMap;
    }

    /**
     * Returns the tile that player can walk through.
     */
    public TiledMapTile getFloorTile(final int col, final int row)
    {
        return getTile(tilesLayer, col, row);
    }

    /**
     * Returns the tile that palyer can not walk through.
     */
    public TiledMapTile getWallTile(final int col, final int row)
    {
        return getTile(wallsLayer, col, row);
    }

    private TiledMapTile getTile(final TiledMapTileLayer layer, final int col, final int row)
    {
        TiledMapTile result = null;
        final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
        if (cell != null)
        {
            result = cell.getTile();
        }
        return result;
    }

    /**
     * Checks if the tile is wall.
     */
    public boolean isWall(final TiledMapTile tile)
    {
        return Boolean.TRUE.toString().equalsIgnoreCase((String) tile.getProperties().get("isWall"));
    }

    /**
     * Returns the game world with. Amount of tiles.
     */
    public int getCollsAmt()
    {
        return collsAmt;
    }

    /**
     * Returns the game world height. Amount of tiles.
     */
    public int getRowsAmt()
    {
        return rowsAmt;
    }

    /**
     * Returns tile width in pixels.
     */
    public float getTileWidth()
    {
        return tileWidth;
    }

    /**
     * Returns tile height in pixels.
     */
    public float getTileHeight()
    {
        return tileHeight;
    }

    public Player getPlayer()
    {
        return player;
    }

    public NavigationMap<NavigationPoint> getNavigationMap()
    {
        return navigationMap;
    }
}
