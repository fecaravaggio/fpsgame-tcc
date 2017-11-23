/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;


public class Player {
    
    private Vector3f coord;
    private Vector3f angle;
    private int life;
    
    private Player () {

    }
    
    public Player (int life, Vector3f coord, Vector3f angle) {
        this();
        this.life = life;
        this.coord = coord;
        this.angle = angle;      
    }

    public Vector3f getCoord() {
        return coord;
    }

    public void setCoord(Vector3f coord) {
        this.coord = coord;
    }

    public Vector3f getAngle() {
        return angle;
    }

    public void setAngle(Vector3f angle) {
        this.angle = angle;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
 
}
