package com.ifood.quarkus.marketplace.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.ifood.quarkus.marketplace.model.dto.PedidoRealizadoDTO;
import com.ifood.quarkus.marketplace.model.dto.PratoDTO;
import com.ifood.quarkus.marketplace.model.dto.PratoPedidoDTO;
import com.ifood.quarkus.marketplace.model.dto.RestauranteDTO;
import com.ifood.quarkus.marketplace.model.entity.Prato;
import com.ifood.quarkus.marketplace.model.entity.PratoCarrinho;

import io.smallrye.mutiny.Uni;

@Path("carrinhos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarrinhoAPI {
	
	 private static final String CLIENTE = "a";

	    @Inject
	    io.vertx.mutiny.pgclient.PgPool client;

	    @Inject
	    @Channel("pedidos")
	    private Emitter<PedidoRealizadoDTO> emitterPedido;

	    @GET
	    public Uni<List<PratoCarrinho>> buscarcarrinho() {
	        return PratoCarrinho.findCarrinho(client, CLIENTE);
	    }

	    @POST
	    @Path("/prato/{idPrato}")
	    public Uni<Long> adicionarPrato(@PathParam("idPrato") Long idPrato) {
	        //poderia retornar o pedido atual
	        PratoCarrinho pc = new PratoCarrinho();
	        pc.cliente = CLIENTE;
	        pc.prato = idPrato;
	        return PratoCarrinho.save(client, CLIENTE, idPrato);

	    }

	    @POST
	    @Path("/realizar-pedido")
	    public Uni<Boolean> finalizar() {
	        PedidoRealizadoDTO pedido = new PedidoRealizadoDTO();
	        String cliente = CLIENTE;
	        pedido.cliente = cliente;
	        List<PratoCarrinho> pratoCarrinho = PratoCarrinho.findCarrinho(client, cliente).await().indefinitely();
	        //Utilizar mapstruts
	        List<PratoPedidoDTO> pratos = pratoCarrinho.stream().map(pc -> from(pc)).collect(Collectors.toList());
	        pedido.pratos = pratos;

	        RestauranteDTO restaurante = new RestauranteDTO();
	        restaurante.nome = "nome restaurante";
	        pedido.restaurante = restaurante;
	        emitterPedido.send(pedido);
	        return PratoCarrinho.delete(client, cliente);
	    }

	    private PratoPedidoDTO from(PratoCarrinho pratoCarrinho) {
	        PratoDTO dto = Prato.findById(client, pratoCarrinho.prato).await().indefinitely();
	        return new PratoPedidoDTO(dto.nome, dto.descricao, dto.preco);
	    }


}