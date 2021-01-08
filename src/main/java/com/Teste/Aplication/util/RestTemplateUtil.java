package com.Teste.Aplication.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateUtil {
	@Value("${base.host}")
	private String baseHost;
	/* Class util para realizar o consumo utilizando o RestTemplate */
	// Metodo usando para consumir api que contenha os paramenters
	// Esse metodo pode ser usando POST, GET, PUT...
	// O metodo recebe o objeto HttpMethod do spring
	// A url da api que deseja realizar o consumo
	// O Objeto MultValueMap do spring, responsável para setar a key (tipo String) e
	// a value (tipo String) do paramentro da api
	// Class<?> é o tipo da classe modelada por este Class objeto. Por exemplo, o
	// tipo de String a class é Class<String>. Usa-se Class<?> se a classe que está
	// sendo modelada for desconhecida.
	public ResponseEntity<?> sendByParams(HttpMethod method, String url, MultiValueMap<String, String> params,
			Class<?> class1) {
		RestTemplate restTemplate = new RestTemplate(); // instancia do RestTemplate
		HttpHeaders headers = new HttpHeaders(); // instancia do HttpHeaders, no qual é responsavel por informar qual o
													// conteudo a requisição vai enviar para a api de consumo.
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Esse exemplo é para informar o conteudo que
																		// vai setar os paramentos da api.

		HttpEntity<MultiValueMap<String, String>> request = // Representa uma solicitação HTTP ou entidade de resposta,
															// consistindo em cabeçalhos e corpo.
				new HttpEntity<MultiValueMap<String, String>>(params, headers);
		// O metodo exchage é responsavel por enviar a solicitação
		// recebe a url, o tipo do metodo, a entidade do envio e a class do retorno da api.
		return restTemplate.exchange(baseHost + url, method, request, class1);
	}

	public ResponseEntity<?> get(String url, Class<?> class1) {
		RestTemplate restTemplate = new RestTemplate();// instancia do RestTemplate
		// O metodo getForEntity é responsavel por enviar a solicitação do tipo get
		// recebe a url e a class do retorno da api.
		return restTemplate.getForEntity(baseHost + url, class1);
	}
	
	public Object getEntity(String url, HttpHeaders httpHeaders, Class<?> class1){
		return get(url, httpHeaders, class1).getBody();
	}
	

	public Object getEntity(String url, Class<?> class1){
		return get(url, class1).getBody();
	}
	
	public ResponseEntity<?> get(String url, HttpHeaders httpHeaders, Class<?> class1){
		RestTemplate restTemplate = new RestTemplate();// instancia do RestTemplate
		HttpEntity<?> request = new HttpEntity<>(httpHeaders);
		// O metodo getForEntity é responsavel por enviar a solicitação do tipo get
		// recebe a url, a class do retorno da api e os headers da requisição.
		return restTemplate.exchange(baseHost + url, HttpMethod.GET, request, class1);
	}
	
	public ResponseEntity<?> post(String url, HttpHeaders headers, Object object, Class<?> class1){
		RestTemplate restTemplate = new RestTemplate();// instancia do RestTemplate
		
		HttpEntity<?> request = new HttpEntity<>(object, headers);
		// O metodo postForEntity é responsavel por enviar a solicitação do tipo post
		// recebe a url, o objeto HttpEntity e a class do retorno da api.
		return restTemplate.postForEntity(baseHost + url, request, class1);
	}
	
}