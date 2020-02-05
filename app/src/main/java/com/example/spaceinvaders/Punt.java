package com.example.spaceinvaders;

public class Punt {
    private float x;
    private float y;
    private int id;

    public Punt(float x, float y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }
}
