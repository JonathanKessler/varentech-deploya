package jettyjerseytutorial;
//  this will be the file where we are going to write our Rest-methods using jersey

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;



@Path("home")

public class Resource {

    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod (@FormParam("inputfile") String inputfile, @FormParam("desiredLocation") String desiredLocation,@FormParam("execute") String execute,@FormParam("unpack" ) String unpack) {
        Add_to_Table tab = new Add_to_Table();
        tab.alterTable(inputfile, desiredLocation, execute, unpack);
        return "<h2> The file you chose to input was: " +inputfile+ ". You want us to store it here: "+ desiredLocation + ". We will execute this file using this command: "+execute + ". We will unpack the file using this command: " +   unpack + "." +"</h2>";
    }
    }



