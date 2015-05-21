package com.symonjin;

import com.symonjin.entities.Entity;
import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
import com.symonjin.shaders.StaticShader;
import com.symonjin.texture.ModelTexture;
import com.symonjin.util.MathUtil;
import com.symonjin.vector.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


import java.util.List;
import java.util.Map;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public Renderer(StaticShader shader) {
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0f, 0f, 0f, 1f);
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel model:entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity:batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel tmodel){
        Model model = tmodel.getModel();

        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture t = tmodel.getModelTexture();
        shader.loadShineVariables(t.getShineDamper(), t.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmodel.getModelTexture().getTextureID());


    }

    private void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformMatrix = MathUtil.createTransformation(
                entity.getPosition(), entity.getRotx(), entity.getRoty(), entity.getRotz(),
                entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
    }


    private void createProjectionMatrix() {
        float aspectRatio = Main.getWidth() / Main.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum);
        projectionMatrix.m33 = 0;
    }

}
