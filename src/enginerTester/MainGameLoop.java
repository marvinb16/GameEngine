package enginerTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gameEngine.DisplayManager;
import gameEngine.Loader;
import gameEngine.MasterRenderer;
import gameEngine.OBJLoader;
import gameEngine.EntityRenderer;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();		
		
		// TERRAIN TEXTURE STUFF
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		 Light light = new Light(new Vector3f(20000,40000,20000), new Vector3f(1,1,1));

		
		Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap, "heightmap");
		
		
		RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
		TexturedModel fern = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		
		RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
		TexturedModel tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
		
		RawModel bigTreeModel = OBJLoader.loadObjModel("lowPolyTree", loader);
		TexturedModel bigTree = new TexturedModel(bigTreeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));
		
		
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
		for  (int i = 0; i < 500;i++) {
			float x = random.nextFloat() * 800 - 400;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(fern, new Vector3f(x,y,z), 0,random.nextFloat() * 360, 0, 0.9f));
			entities.add(new Entity(grass, new Vector3f(x,y,z), 0,random.nextFloat() * 360, 0, 0.8f));
			
		}
		for(int j = 0; j < 200; j++){
			float x = random.nextFloat() * 800 - 400;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(tree, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,3f));
		}
		for(int j = 0; j < 50; j++){
			float x = random.nextFloat() * 800 - 400;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(bigTree, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,1));
		}
		
		
		
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel bunnyModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("stallTexture")));
		
		
		
		Player player = new Player(stanfordBunny, new Vector3f(100,0,-50),0,0,0,0.5f);
		Camera camera = new Camera(player);
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move(terrain);
			renderer.processEntity(player);
			// game logic, tender
			for (Entity ent : entities) {
				renderer.processEntity(ent);
			}
			
			renderer.processTerrain(terrain);

			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		renderer.cleanUP();
		loader.cleanUP();
		DisplayManager.closeDisplay();

	}

}
