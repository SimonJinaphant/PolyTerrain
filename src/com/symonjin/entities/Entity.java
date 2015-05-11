package com.symonjin.entities;

import com.symonjin.math.Vector3f;
import com.symonjin.models.TexturedModel;

/**
 * Created by Simon on 2015-05-10.
 */
public class Entity {
    private TexturedModel tmodel;
    private Vector3f position;
    private float rotx, roty, rotz;
    private float scale;

    public Entity(TexturedModel tmodel, Vector3f position, float rotx, float roty, float rotz, float scale) {
        this.tmodel = tmodel;
        this.position = position;
        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz){
        this.rotx += dx;
        this.roty += dy;
        this.rotz += dz;
    }
    public void increaseScale(float scale){
        this.scale += scale;
    }

    public TexturedModel getTmodel() {
        return tmodel;
    }

    public void setTmodel(TexturedModel tmodel) {
        this.tmodel = tmodel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotx() {
        return rotx;
    }

    public void setRotx(float rotx) {
        this.rotx = rotx;
    }

    public float getRoty() {
        return roty;
    }

    public void setRoty(float roty) {
        this.roty = roty;
    }

    public float getRotz() {
        return rotz;
    }

    public void setRotz(float rotz) {
        this.rotz = rotz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
