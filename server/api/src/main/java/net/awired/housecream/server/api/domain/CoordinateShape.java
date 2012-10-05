package net.awired.housecream.server.api.domain;

public enum CoordinateShape {
    rect("rectangle"), //
    circle("circle"), //
    poly("polygon"), //
    bezier1("bezier"), //

    ;

    private final String name;

    private CoordinateShape(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

}
