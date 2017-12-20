package xyz.sky731.programming.lab3;

public interface Ownable {
    public boolean buyBuilding(Building building);
    public void sellBuilding(Building building) throws NoExistException ;
}
