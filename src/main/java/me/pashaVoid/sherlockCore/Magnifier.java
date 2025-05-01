package me.pashaVoid.sherlockCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Magnifier {

    public static HashMap<String, Magnifier> patterns = new HashMap<>();
    public static List<String> keys = new ArrayList<>();

    private String id;
    private String name;
    private int nicks;
    private int durability;
    private int add_chances;
    private boolean show_thief;

    public Magnifier(String id, String name, int nicks, int durability, int add_chances, boolean show_thief) {
        this.id = id;
        this.name = name;
        this.nicks = nicks;
        this.durability = durability;
        this.add_chances = add_chances;
        this.show_thief = show_thief;

        patterns.put(id, this);
        keys.add(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNicks() {
        return nicks;
    }

    public void setNicks(int nicks) {
        this.nicks = nicks;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getAdd_chances() {
        return add_chances;
    }

    public void setAdd_chances(int add_chances) {
        this.add_chances = add_chances;
    }

    public boolean isShow_thief() {
        return show_thief;
    }

    public void setShow_thief(boolean show_thief) {
        this.show_thief = show_thief;
    }
}