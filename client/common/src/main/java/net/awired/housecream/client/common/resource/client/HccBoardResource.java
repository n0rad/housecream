package net.awired.housecream.client.common.resource.client;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.board.HccBoard;
import net.awired.housecream.client.common.resource.HccUpdateException;

@Path("/")
public interface HccBoardResource {

    @GET
    public HccBoard getBoard();

    @PUT
    public HccBoard setBoard(HccBoard board) throws HccUpdateException;

}
