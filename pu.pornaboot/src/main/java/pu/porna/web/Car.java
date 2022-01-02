package pu.porna.web;

public class Car
{
private String make;
private String model;

public Car() {

}

public Car(String aMake, String aModel) {
    this.make = aMake;
    this.model = aModel;
}

public String getMake() {
    return make;
}

public void setMake(String aMake) {
    this.make = aMake;
}

public String getModel() {
    return model;
}

public void setModel(String aModel) {
    this.model = aModel;
}

}

