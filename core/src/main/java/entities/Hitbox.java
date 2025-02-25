package entities;

public class Hitbox {
    public double min;
    public double max;

    public Hitbox(double coord) {
        this.min = coord -50;
        this.max = coord +50;
    }
    public void update(double coord){
        this.min = coord -50;
        this.max = coord +50;
    }
    public Hitbox(double coord, double mod){
        this.min = coord -mod;
        this.max = coord +mod;
    }
    public void updateMod(double coord, double mod){
        this.min = coord -mod;
        this.max = coord +mod;
    }

}
