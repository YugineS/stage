package com.yug.core.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.yug.core.game.helpers.KeyTilesFactory;
import com.yug.core.game.helpers.LockedTilesFactory;
import com.yug.core.game.helpers.PlatformFactory;
import com.yug.core.game.helpers.TeleportFactory;
import com.yug.core.game.helpers.TiledMapUtils;
import com.yug.core.game.helpers.VanishingTilesFactory;
import com.yug.core.game.model.KeyTile;
import com.yug.core.game.model.LockedTile;
import com.yug.core.game.model.NavigationMapWrapper;
import com.yug.core.game.model.Platform;
import com.yug.core.game.model.Player;
import com.yug.core.game.model.Teleport;
import com.yug.core.game.model.Tile;
import com.yug.core.game.model.VanishingTile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    public final static String TM_WALKABLE = "walkable";
    public final static String TM_OBJECT_X = "x";
    public final static String TM_OBJECT_Y = "y";
    public final static String TM_OBJECT_TYPE = "type";
    public static final String TM_PLATFORM_TYPE = "platformType";

    public static final String TM_OBJECT_TYPE_PLATFORM = "platform";
    public static final String TM_OBJECT_TYPE_VANISHING_TILE = "vanishingTile";
    public static final String TM_OBJECT_TYPE_PLAYER = "player";
    public static final String TM_OBJECT_TYPE_TELEPORT = "teleport";
    public static final String TM_OBJECT_TYPE_LOCK = "lock";
    public static final String TM_OBJECT_TYPE_KEY = "key";

    private TmxMapLoader tmLoader;
    private TiledMap tiledMap;
    private TiledMapTileLayer wallsLayer;
    private TiledMapTileLayer tilesLayer;
    private int width;
    private int height;
    private float tileWidth;
    private float tileHeight;
    private NavigationMapWrapper navigationMap;
    private List<Platform> platforms = new ArrayList<Platform>();
    private List<VanishingTile> vanishingTiles = new LinkedList<VanishingTile>();
    private List<Teleport> teleports = new ArrayList<Teleport>();
    private List<LockedTile> locks = new ArrayList<LockedTile>();
    private List<KeyTile> keys = new ArrayList<KeyTile>();
    private final PlatformFactory platformFactory;
    private final VanishingTilesFactory vanishingTilesFactory;
    private final TeleportFactory teleportFactory = new TeleportFactory();
    private final LockedTilesFactory lockedTilesFactory = new LockedTilesFactory();
    private final KeyTilesFactory keyTilesFactory = new KeyTilesFactory();

    private final Player player;

    private static GameWorld instance = new GameWorld();

    private GameWorld()
    {
        tmLoader = new TmxMapLoader();
        platformFactory = new PlatformFactory();
        vanishingTilesFactory = new VanishingTilesFactory();
        player = new Player();
    }

    public static GameWorld getInstance()
    {
        return instance;
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

        platforms.clear();
        vanishingTiles.clear();
        teleports.clear();
        locks.clear();
        keys.clear();

        //TODO:clear map on level load
        navigationMap = new NavigationMapWrapper(width, height);
        loadNavigationMap();

        navigationMap.addObserver(player);
    }

    private void loadNavigationMap()
    {
        for (final MapLayer layer : tiledMap.getLayers())
        {
            if (layer instanceof TiledMapTileLayer)
            {
                loadTiledLayer(navigationMap, (TiledMapTileLayer) layer);
            }
            else
            {
                loadObjectLayer(navigationMap, layer);
            }
        }
    }

    private void loadObjectLayer(final NavigationMapWrapper navigationMap, final MapLayer layer)
    {
        for (final MapObject object : layer.getObjects())
        {
            final MapProperties objectProperties = object.getProperties();
            final String objectType = TiledMapUtils.getStringPropery(TM_OBJECT_TYPE, objectProperties);
            if (TM_OBJECT_TYPE_PLATFORM.equals(objectType))
            {
                final Platform platform = platformFactory.create(tileWidth, tileHeight, objectProperties);
                navigationMap.setPoint(platform, platform.getX(), platform.getY());
                platforms.add(platform);
            }
            else if (TM_OBJECT_TYPE_VANISHING_TILE.equals(objectType))
            {
                final VanishingTile vanishingTile = vanishingTilesFactory.create(tileWidth, tileHeight, objectProperties);
                navigationMap.setPoint(vanishingTile, vanishingTile.getX(), vanishingTile.getY());
                vanishingTiles.add(vanishingTile);
            }
            else if (TM_OBJECT_TYPE_TELEPORT.equals(objectType))
            {
                final Teleport teleport = teleportFactory.create(tileWidth, tileHeight, objectProperties);
                navigationMap.setPoint(teleport, teleport.getX(), teleport.getY());
                teleports.add(teleport);
            }
            else if (TM_OBJECT_TYPE_PLAYER.equals(objectType))
            {
                initPlayer(TiledMapUtils.getIntProperty(TM_OBJECT_X, objectProperties),
                        TiledMapUtils.getIntProperty(TM_OBJECT_Y, objectProperties));
            }
            else if (TM_OBJECT_TYPE_LOCK.equals(objectType))
            {
                final LockedTile lockedTile = lockedTilesFactory.create(tileWidth, tileHeight, objectProperties);
                navigationMap.setPoint(lockedTile, lockedTile.getX(), lockedTile.getY());
                locks.add(lockedTile);
            }
            else if (TM_OBJECT_TYPE_KEY.equals(objectType))
            {
                final KeyTile keyTile = keyTilesFactory.create(tileWidth, tileHeight, objectProperties);
                navigationMap.setPoint(keyTile, keyTile.getX(), keyTile.getY());
                keys.add(keyTile);
            }
        }
        initTeleports(teleports);
    }

    private void initTeleports(final List<Teleport> teleports)
    {
        for (final Teleport teleport : teleports)
        {
            final Teleport targetTeleport = findTeleportById(teleport.getTargetId(), teleports);
            teleport.setTargetTeleport(targetTeleport);
        }
    }

    private Teleport findTeleportById(final int id, final List<Teleport> teleports)
    {
        for (final Teleport teleport : teleports)
        {
            if (id == teleport.getId())
            {
                return teleport;
            }
        }
        return null;
    }

    private void initPlayer(final int x, final int y)
    {
        player.setX(x);
        player.setY(y);
        player.setScreenX(player.getX() * tileWidth);
        player.setScreenY(player.getY() * tileHeight);
        player.setWidth(tileWidth);
        player.setHeight(tileHeight);
        player.setState(Player.State.STANDING);
    }

    private void loadTiledLayer(final NavigationMapWrapper navigationMap, final TiledMapTileLayer layer)
    {
        final MapProperties layerProperties = layer.getProperties();
        final boolean walkable = TiledMapUtils.getBooleanProperty(TM_WALKABLE, layerProperties);
        for (int x = 0; x < layer.getWidth(); x++)
        {
            for (int y = 0; y < layer.getHeight(); y++)
            {
                final TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null)
                {
                    continue;
                }
                //get point from the Tiles Pool
                if (navigationMap.getPoint(x, y) == null)
                {
                    Tile point = new Tile(x, y);
                    navigationMap.setPoint(point, x, y);
                    point.setWidth(tileWidth);
                    point.setHeight(tileHeight);
                    point.setWalkable(walkable);
                    //TODO:create animated tiles here
                }
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

    public NavigationMapWrapper getNavigationMap()
    {
        return navigationMap;
    }

    public List<Platform> getPlatforms()
    {
        return platforms;
    }

    public List<VanishingTile> getVanishingTiles()
    {
        return vanishingTiles;
    }

    public List<Teleport> getTeleports()
    {
        return teleports;
    }

    public List<KeyTile> getKeys()
    {
        return keys;
    }

    public List<LockedTile> getLocks()
    {
        return locks;
    }
}
