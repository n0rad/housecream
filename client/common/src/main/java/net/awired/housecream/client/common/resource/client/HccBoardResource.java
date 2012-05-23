package net.awired.housecream.client.common.resource.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import net.awired.housecream.client.common.domain.board.HccBoard;
import net.awired.housecream.client.common.resource.HccUpdateException;

@Path("/")
@Produces("application/json")
@Consumes("application/json")
public interface HccBoardResource {

    @GET
    public HccBoard getBoard();

    @PUT
    public void setBoard(HccBoard board) throws HccUpdateException;

    @PUT
    @Path("/reset")
    public void resetBoard();

    @PUT
    @Path("init")
    public void init();

    @PUT
    @Path("notify")
    public void runNotify();

}
