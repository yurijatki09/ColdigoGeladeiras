package br.com.coldigogeladeiras.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;

public class UtilRest {
	/*Abaixo o método responsável por enviar a resposta ao cliente sobre a transição realizada (inclusão, consulta, edição ou exlusão)
	 *caso ela seja realizada com sucesso. Repare que o método em questão aguarda que seja repassado um conteúdo que será referenciado
	 *por um objeto chamado result.	 
	 */
	public Response buildResponse (Object result) {
		
		try {
			/*Retorna o objeto de resposta com status 200 (ok), tendo em seu corpo o objeto valorResposta (que consiste no objeto result
			 * convertido para JSON).
			 * 
			 */
			String valorResposta = new Gson().toJson(result);
			return Response.ok(valorResposta).build();
		}catch (Exception ex){
			ex.printStackTrace();
			//Se algo der errado acima, cria o Response de erro.
			return this.buildErrorResponse(ex.getMessage());
		}
	}
	/*Abaixo o método responsável por enviar a resposta ao cliente sobre a transição realizada, inclusão, consulta, edição ou exclusão ao
	 *Cliente, não são realizadas com sucesso, ou seja, que contenha algum erro. Repare que o método em questão aguarda que seja
	 *repassado algum conteúdo que será referenciado por um objeto chamado rb.
	 */
	public Response buildErrorResponse(String str) {
		/*
		 *Abaixo o objeto rb recebe o status de erro
		 */
		ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		/*
		 * Define a entidade (objeto), que nesse caso é uma mensagem que será retornada ao cliente.
		 */
		rb = rb.entity(str);
		/*
		 * Define o tipo de retorno desta entidade (objeto), no caso é definida como texto simples.
		 */
		rb = rb.type("text/plain");
		/*
		 * Retorna o objeto de resposta com o status 500 (erro), junto com a String contendo a mensagem do erro.
		 */
		return rb.build();
	}
}
