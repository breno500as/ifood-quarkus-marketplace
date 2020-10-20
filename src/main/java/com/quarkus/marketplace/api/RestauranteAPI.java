package com.quarkus.marketplace.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.quarkus.marketplace.model.db.Prato;
import com.quarkus.marketplace.model.dto.PratoDTO;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteAPI {
	
	 @Inject
	 private PgPool client;

	  @GET
	  @Path("{idRestaurante}/pratos")
	  public Multi<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
	        return Prato.findAll(client, idRestaurante);
	  }

}
