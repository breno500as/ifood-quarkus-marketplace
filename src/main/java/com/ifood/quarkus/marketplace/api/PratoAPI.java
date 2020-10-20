package com.ifood.quarkus.marketplace.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ifood.quarkus.marketplace.model.db.Prato;
import com.ifood.quarkus.marketplace.model.dto.PratoDTO;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

@Path("/pratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PratoAPI {
	
	@Inject
	private PgPool pgPool;

	@GET
	public Multi<PratoDTO> getPratos(){
		return Prato.findAll(this.pgPool);
	}
	
}
