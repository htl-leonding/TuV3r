package at.htl.rest;

import at.htl.entity.Match;
import at.htl.entity.Tournament;
import at.htl.logic.MatchFacade;
import io.swagger.annotations.*;
//import com.wordnik.swagger.annotations.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by Laurenz on 12.11.2015.
 */
@Path("match")
@Api(value = "/match",description = "Matches")
public class MatchResource {
    @Inject
    MatchFacade matchFacade;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get match by id")
    public Match findById(
            @ApiParam(value = "id of the wanted match", required = true)
            @PathParam("id") long id){
        return matchFacade.findById(id);
    }
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get all matches")
    public List<Match> findAll(){
        return matchFacade.findAll();
    }
    @GET
    @Path("t{id}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get matches by tournament id")
    public List<Match> findByTournamentId(
            @ApiParam(value = "id of the tournament the wanted matches are in", required = true)
            @PathParam("id") long tournamentId){
        return matchFacade.findMatchesByTournamentId(tournamentId);
    }
    @PUT
    @Path("{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Accepted"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    @ApiOperation(value = "Update a match")
    public Response update(
            @ApiParam(value = "Updated match object in json", required = true)
                    Match m,
            @ApiParam(value = "id of the match that needs to be updated", required = true)
            @PathParam(value = "id") long id){
        matchFacade.save(id,m);
        return Response.accepted().build();
    }
    @POST
    @ApiOperation(value = "Save a match")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Created"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response save(
            @ApiParam(value = "new match in json",required = true)
                    Match m,
            @Context UriInfo info){
        Match saved = matchFacade.save(0,m);
        URI uri = info.getAbsolutePathBuilder().path("/"+saved.getId()).build();
        return Response.created(uri).build();
    }
}
