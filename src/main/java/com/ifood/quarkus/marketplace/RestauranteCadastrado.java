package com.ifood.quarkus.marketplace;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.ifood.quarkus.marketplace.model.db.Restaurante;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;

@ApplicationScoped
public class RestauranteCadastrado {
	
	
	@Inject
	private PgPool pgPool;
	
	
	@Incoming("restaurantes")
	public void receberRestaurante(JsonObject json) {
	 Restaurante r = json.mapTo(Restaurante.class);
	 r.persist(pgPool);
	}

}
