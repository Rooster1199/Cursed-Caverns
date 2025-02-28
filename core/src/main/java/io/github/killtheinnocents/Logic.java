package io.github.killtheinnocents;

public class Logic {

    public static void Logic(String[] args) {
        Entity player = new Entity(100, 5);
        Entity enemy1 = new Entity(15, 2);
        enemy1.ouchies(player.inflictWound());
        System.out.println(enemy1.getCHealth());
    }

}

class Entity {

    int maxHealth;
    int str;
    int cHealth;

    Entity(int health, int strength) {
        str = strength;
        cHealth = health;
        maxHealth = health;
    }

    public int ouchies(int damage) {
        cHealth -= damage;
        if (cHealth > maxHealth) {
            cHealth = maxHealth;
        }
        return cHealth;
    }

    public int inflictWound() {
        return 5 + str; //add real math
    }

    public int getCHealth() {
        return cHealth;
    }

    public int getMaxHealth() { return maxHealth;}

}
