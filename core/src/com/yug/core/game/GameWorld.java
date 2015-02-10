package com.yug.core.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.yug.core.game.model.NavigationMapWrapper;
import com.yug.core.game.model.Player;
import com.yug.core.game.model.Tile;
import com.yug.pf.NavigationMap;

/**
 * The root class of game model.
 * Created by yugine on 16.1.15.
 */
public class GameWorld
{
    private final static String TM_WITH = "width";
    private final static String TM_HEIGHT = "height";
    private final static String TM_TILE_WIDTH = "tilewidth";
    private final static String TM_TILE_HEIGHT = "tileheight";
    private final static String TM_WALLS_LAYER_NAME = "walls";
    private final static String TM_TILES_LAYER_NAME = "tiles";

    private TmxMapLoader tmLoader;
    private TiledMap tiledMap;
    private TiledMapTileLayer wallsLayer;
    private TiledMapTileLayer tilesLayer;
    private int width;
    private int height;
    private float tileWidth;
    private float tileHeight;
    private NavigationMapWrapper navigationMap;

    private Player player;

    public GameWorld()
    {
        tmLoader = new TmxMapLoader();
        player = new Player(this);

    }

    public void loadLevel(final LevelName levelName)
    {
        tiledMap = tmLoader.load(levelName.getTmxFilePath());
        width = (Integer) tiledMap.getProperties().get(TM_WITH);
        height = (Integer) tiledMap.getProperties().get(TM_HEIGHT);
        tileHeight = (Integer) tiledMap.getProperties().get(TM_TILE_HEIGHT);
        tileWidth = (Integer) tiledMap.getProperties().get(TM_TILE_WIDTH);

        wallsLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TM_WALLS_LAYER_NAME);
        tilesLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TM_TILES_LAYER_NAME);

        final Tile[][] navPoints = new Tile[width][height];
        navigationMap = new NavigationMapWrapper(navPoints);
        loadNavigationMap();
        player.setX(1);
        player.setY(1);
        player.setScreenX(player.getX() * tileWidth);
        player.setScreenY(player.getY() * tileHeight);
        player.setWidth(tileWidth);
        player.setHeight(tileHeight);
    }

    public void loadNavigationMap()
    {
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                Tile point = navigationMap.getPoint(x, y);
                if (point == null)
                {
                    point = new Tile(x, y);
                    point.setWidth(tileWidth);
                    point.setHeight(tileHeight);
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
    public int getWidth()
    {
        return width;
    }

    /**
     * Returns the game world height. Amount of tiles.
     */
    public int getHeight()
    {
        return height;
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

    public NavigationMap<Tile> getNavigationMap()
    {
        return navigationMap;
    }
}
