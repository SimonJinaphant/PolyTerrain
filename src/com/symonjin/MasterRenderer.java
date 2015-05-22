package com.symonjin;

import com.symonjin.entities.Entity;
import com.symonjin.models.TexturedModel;
import com.symonjin.shaders.StaticShader;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(Light sunlight, Camera camera){
        renderer.prepare();
        shader.start();
        shader.loadLight(sunlight);
        shader.loadViewMatrix(camera);

        renderer.render(entities);

        shader.stop();
        entities.clear();
    }

    public void unload(){
        shader.unloadShaders();
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getTmodel();
        List<Entity> batch = entities.get(entityModel);
        if(batch != null){
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

}
