/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

public class Tile {
    
    private int tileX, tileY;
    private char type;
    private Vector3f coord;
          
    public Tile (int tileX, int tileY, char type) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.type = type;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public Vector3f getCoord() {
        return coord;
    }

    public void setCoord(Vector3f coord) {
        this.coord = coord;
    }
    
    public void tileType () {

        //wall
        if (type == '1') {

        } else {
            //empty
            if (type == '0') {

            }
        }
    }
}
