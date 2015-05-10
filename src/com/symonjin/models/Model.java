package com.symonjin.models;

public class Model {
    //Contains a model's meta data?

    private int vaoID;
    private int vertexCount;

    public Model(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount(){
        return vertexCount;
    }

}
