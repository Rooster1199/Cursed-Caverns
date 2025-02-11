//package helper;
//
//import com.badlogic.gdx.maps.MapObject;
//import com.badlogic.gdx.maps.MapObjects;
//import com.badlogic.gdx.maps.objects.RectangleMapObject;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TmxMapLoader;
//import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.physics.box2d.Body;
//import io.github.killtheinnocents.View;
//import entities.living.LivingEntity;
//
//public class mapHelper {
//
//    private TiledMap tiledMap;
//    private View gameScreen;
//
//    public mapHelper(View gameScreen) {
//
//        this.gameScreen = gameScreen;
//    }
//
//    /*
//    public OrthogonalTiledMapRenderer setupMap()
//    {
//        tiledMap = new TmxMapLoader().load("map.tmx");
//        parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
//        return new OrthogonalTiledMapRenderer(tiledMap);
//    }
//
//     */
//
//    private void parseMapObjects(MapObjects mapObjects) {
//        for (MapObject mapObject : mapObjects) {
//
//            if (mapObject instanceof RectangleMapObject)
//            {
//                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
//                String rectangleName = mapObject.getName();
//
//                if(rectangleName.equals("player"))
//                {
//                    Body body = LivingEntity(
//                        rectangle.getX() + rectangle.getWidth() / 2,
//                        rectangle.getY() + rectangle.getHeight() / 2,
//                        rectangle.getWidth(),
//                        rectangle.getHeight(),
//                        false,
//                        gameScreen.getWorld()
//                    );
//                }
//            }
//        }
//    }
//
//    private void createStaticBody()
//}
