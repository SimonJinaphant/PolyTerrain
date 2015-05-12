package com.symonjin;

import com.symonjin.vector.Matrix;
import com.symonjin.vector.Matrix4f;
import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
import com.symonjin.entities.Entity;

import com.symonjin.shaders.StaticShader;
import com.symonjin.util.MathUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderDaemon {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    public RenderDaemon(StaticShader shader){
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Entity entity, StaticShader shader) {
        TexturedModel texturedmodel = entity.getTmodel();
        Model model = texturedmodel.getModel();

        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f transformMatrix = MathUtil.createTransformation(
                entity.getPosition(), entity.getRotx(),entity.getRoty(), entity.getRotz(),
                entity.getScale());
        shader.loadTransformMatrix(transformMatrix);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedmodel.getModelTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void createProjectionMatrix(){
        float aspectRatio = Main.getWidth()/Main.getHeight();
        float y_scale = (float) ((1f/Math.tan(Math.toRadians(FOV/2f)))* aspectRatio);
        float x_scale = y_scale/aspectRatio;
        float frustum = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -( (FAR_PLANE + NEAR_PLANE) / frustum);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -( (2 * NEAR_PLANE * FAR_PLANE) / frustum);
        projectionMatrix.m33 = 0;
    }

}
