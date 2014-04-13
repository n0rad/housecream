package org.housecream.server.api.domain.point;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.housecream.server.api.domain.CoordinateShape;
import lombok.Data;


// zone
// area
// field
// room
// floor
// building
// land
@Data
public class PointBunch {

    @NotNull
    @Size(min = 1, max = 30)
    private String name;

    private String parentName;

    private String imageMime;

    private byte[] image;

    private CoordinateShape parentZoneCoordinatesShape;

    private String parentZoneCoordinates;


    //    private Coordinate position;
    //    private List<Coordinate> coverage;

}
