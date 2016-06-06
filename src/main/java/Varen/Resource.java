package Varen;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("home")

public class Resource {

    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod(@FormParam("inputfile") String inputfile, @FormParam("desiredLocation") String desiredLocation, @FormParam("executeCommand") String executeCommand, @FormParam("unpack") String unpack) {

        System.out.println(inputfile);
        System.out.println(desiredLocation);
        System.out.println(executeCommand);
        System.out.println(unpack);

        InsertToDatabase db = new InsertToDatabase();
        db.addToDatabase(inputfile, desiredLocation, executeCommand, unpack);

        return "<h2> Hello, " + desiredLocation + "</h2>";
    }
}