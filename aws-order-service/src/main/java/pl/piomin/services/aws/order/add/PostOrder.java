package pl.piomin.services.aws.order.add;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.piomin.services.aws.order.model.Order;
import pl.piomin.services.aws.order.model.RetornoBling;
import pl.piomin.services.aws.order.util.JSONUtil;

public class PostOrder implements RequestHandler<Order, Order> {

	private DynamoDBMapper mapper;
	
	public PostOrder() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		mapper = new DynamoDBMapper(client);
	}
/*	   "numero": "74",
	    "volumes": [
	        {
	            "codigoRastreamento": "",
	            "servico": "SEDEX - CONTRATO"
	        },
	        {
	            "codigoRastreamento": "",
	            "servico": "PAC - CONTRATO"
	        }
	    ],
	    "idPedido": 6428103586,
	    "codigos_rastreamento": {
	        "codigo_rastreamento": null
	    }*/	
	@Override
	public Order handleRequest(Order o, Context ctx) {
		Order r = null;		
		try {
			LambdaLogger logger = ctx.getLogger();		
			
			String transacacaoParam = o.getTransactionPagseguro();//"AAF6BAFF-E9A7-4FAC-8AA3-28E6BD112492";
			String statusParam = o.getStatusPagseguro();//"AAF6BAFF-E9A7-4FAC-8AA3-28E6BD112492";		
	        String idResult = "";
	        boolean transactionPagseguroResult = false;
	
	        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
	        eav.put(":val1", new AttributeValue().withS(transacacaoParam));
	//        eav.put(":val2", new AttributeValue().withS(statusParam));        
	
	//        DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
	//                .withFilterExpression("transactionPagseguro= :val1 and statusPagseguro = :val2").withExpressionAttributeValues(eav);        
	
	        DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
	                .withFilterExpression("transactionPagseguro= :val1").withExpressionAttributeValues(eav);        
	        
	        List<Order> latestReplies = mapper.scan(Order.class, queryExpression);
	        String idPedido = o.getTransactionPagseguro();
	        String nomeCompradorPedido = o.getName();
	        logger.log("NOME=" + nomeCompradorPedido);	        
	        String emailCompradorPedido = o.getEmail();	        
	        //String dataPedido = o.getData();
	        String statusPedido = o.getStatusPagseguro();
	        String totalPedido = o.getAmount();	        
	        for (Order order : latestReplies) {
	        	logger.log("TRANSACAO QUERY=" + order.getTransactionPagseguro());
	        	logger.log("STATUS QUERY=" + order.getStatusPagseguro());
	        	idResult = order.getId();
	        	logger.log("ID QUERY=" + order.getId());        	
	        	transactionPagseguroResult = true;
	        }		
			if (transactionPagseguroResult) {
		        o.setId(idResult);			
			} else
			{
				String pedido = enviarBling(o);
				o.setPedido(pedido);
				logger.log("NUMERO DO PEDIDO =" + pedido);				
			}
			logger.log("CONTEUDO DO ORDER=" + o.toString());
			logger.log("STATE=" + o.getState());
			mapper.save(o);		
			//Order r = o;
			r = o;
/*			String itemsStr = r.getBins().get(0);
			//String s = "lorem,ipsum,dolor,sit,amet";
			logger.log("CONTEUDO DO ITEMSTR=" + itemsStr);
			List<String> itemsList = new ArrayList<String>(Arrays.asList(itemsStr.split("}")));
			String itemsContent = "";
			for (int i = 0; i < itemsList.size(); i++) {
				String itemStr = "{" + itemsList.get(i).toString().replace(",{", "").replace("{", "") + "}";
				JSONObject jsonObject = new JSONObject(itemStr);
				String idStr = jsonObject.getString("id");
				String descriptionStr = jsonObject.getString("description");
				String quantityStr = jsonObject.getString("quantity");
				String amountStr = jsonObject.getString("amount");
				itemsContent = itemsContent + "|" + "item:" + i +  idStr + "-" + descriptionStr + " Qtd:" + quantityStr + " Total item:" + amountStr + " "; 
			}	
						
			String[] primeiroNome = nomeCompradorPedido.trim().split(" "); 
			String contentStr =  primeiroNome[0] + ", obrigado pela sua compra, esses são os dados da sua compra: " ;			
			contentStr = contentStr + " Status do pedido  :" + getStatus(statusPedido) + " ";
			contentStr = contentStr + " Produtos:" + itemsContent + " " ;
			contentStr = contentStr + " Total pedido:" + totalPedido + " " ;
			contentStr = contentStr + " localizador:"  + idPedido;
			logger.log("CONTEUDO CORPO EMAIL=" + contentStr);
			//logger.log("CONTEUDO DO myList idstr=" + idStr);
			//logger.log("CONTEUDO DO myList2=" + myList.get(1).toString());			
			//System.out.println(myList);  // prints [lorem, ipsum, dolor, sit, amet]			
			
			
            EnviarEmail.enviaEmail("luciano.souzaviana@gmail.com", "danbislojavirtual@gmail.com", "Compra realizada", contentStr, itemsContent);*/			
			logger.log("Order saved7: " + r.getId());	                
        }catch (Exception e){
            e.printStackTrace();
        }			
		return r;
		//}	
//		return o;
/*        return o;*/

	}
	public String enviarBling(Order order) throws JsonMappingException, JsonProcessingException {
		    ObjectMapper mapperBling = new ObjectMapper();	
		    Random random = new Random();
		    int numero = random.nextInt(100);
	        String xml = "<?xml version='1.0' encoding='iso8859-1'?>" 
		    		+ "<pedido>" 
	                + "<cliente>" 
	                + "<nome>" + order.getName() + numero + "</nome>"
	                + "<tipoPessoa>F</tipoPessoa>"
	                + "<endereco>" + order.getAddress01() + "</endereco>"
	                + "<cpf_cnpj>00000000000000</cpf_cnpj>"
	                + "<ie_rg>000000</ie_rg>"
	                + "<numero>0</numero>"
	                + "<complemento></complemento>"
	                + "<bairro></bairro>"
	                + "<cep>" + order.getZip() + "</cep>"
	                + "<cidade>" + order.getCity() + "</cidade>"
	                + "<uf>" + order.getState() + "</uf>"
	                + "<fone>5481153376</fone>"
	                + "<email>teste@teste.com.br</email>"
	                + "</cliente>"
	                + "<transporte>"
	                + "<transportadora>Transportadora XYZ</transportadora>"
	                + "<tipo_frete>R</tipo_frete>"
	                + "<servico_correios>SEDEX - CONTRATO</servico_correios>"
	                + "<dados_etiqueta>"
	                + "<nome>Endereço de entrega</nome>"
	                + "<endereco>Rua Visconde de São Gabriel</endereco>"
	                + "<numero>3924</numero>"
	                + "<complemento>Sala 59</complemento>"
	                + "<municipio>Bento Gonçalves</municipio>"
	                + "<uf>RS</uf>"
	                + "<cep>95.700-000</cep>"
	                + "<bairro>Cidade Alta</bairro>"
	                + "</dados_etiqueta>"
	                + "<volumes>"
	                + "<volume>"
	                + "<servico>SEDEX - CONTRATO</servico>"
	                + "<codigoRastreamento></codigoRastreamento>"
	                + "</volume>"
	                + "<volume>"
	                + "<servico>PAC - CONTRATO</servico>"
	                + "<codigoRastreamento></codigoRastreamento>"
	                + "</volume>"
	                + "</volumes>"
	                + "</transporte>"
	                + "<itens>"
	                + "<item>"
	                + "<codigo>001</codigo>"
	                + "<descricao>Caneta 001</descricao>"
	                + "<un>Pç</un>"
	                + "<qtde>10</qtde>"
	                + "<vlr_unit>1.68</vlr_unit>"
	                + "</item>"
	                + "<item>"
	                + "<codigo>002</codigo>"
	                + "<descricao>Caderno 002</descricao>"
	                + "<un>Un</un>"
	                + "<qtde>3</qtde>"
	                + "<vlr_unit>3.75</vlr_unit>"
	                + "</item>"
	                + "<item>"
	                + "<codigo>005</codigo>"
	                + "<descricao>Teclado 003</descricao>"
	                + "<un>Cx</un>"
	                + "<qtde>7</qtde>"
	                + "<vlr_unit>18.65</vlr_unit>"
	                + "</item>"
	                + "</itens>"
	                + "<parcelas>"
	                + "<parcela>"
	                + "<data>02/09/2009</data>"
	                + "<vlr>100</vlr>"
	                + "<obs>Teste obs 1</obs>"
	                + "</parcela>"
	                + "<parcela>"
	                + "<data>07/09/2009</data>"
	                + "<vlr>50</vlr>"
	                + "<obs></obs>"
	                + "</parcela>"
	                + "<parcela>"
	                + "<data>12/09/2009</data>"
	                + "<vlr>50</vlr>"
	                + "<obs>Teste obs 3</obs>"
	                + "</parcela>"
	                + "</parcelas>"
	                + "<vlr_frete>15</vlr_frete>"
	                + "<vlr_desconto>10</vlr_desconto>"
	                + "<obs>Testando o campo observações do pedido</obs>"
	                + "<obs_internas>Testando o campo observações internas do pedido</obs_internas>"
	                + "</pedido>";
				        	    
	    Client client = ClientBuilder.newClient();
	    WebTarget target = client.target("https://bling.com.br/Api/v2/pedido/json");
	    Form form = new Form();
	    form.param("apikey", "bd7e67a5cc06e91702f7309cc696184e521b0c6a607e1129f3750ceeb8f17ae135c3166c");
	    form.param("xml", xml);
	    String responseResult = target.request(MediaType.APPLICATION_XML_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);	    
	  	System.out.println("postOrderServiceBling =" + responseResult);  //idSessaoStr);
		List<Object> listaRetorno = new ArrayList();
	  	String numeroCurrStr = "";	  		  			
	  	JSONObject obj = new JSONObject(responseResult);
		System.out.println("RETORNO OBJ =" + obj.toString());			  	
		//JSONArray returnResult = obj.getJSONArray("retorno");
//  	    JSONObject retorno = obj.getJSONObject("retorno");		
//		listaRetorno = (ArrayList<Object>) JSONUtil.toList(retorno);
//		System.out.println("LISTARETORNO =" + listaRetorno.toString());		
//		if (!listaRetorno.isEmpty()) {	  	
	  	//JSONArray usersarray=question.getJSONArray("users");	  	
	  	 //   JSONObject retorno = obj.getJSONObject("retorno");
//	  	if (retorno != null) {	 
  	    JSONObject retorno = obj.getJSONObject("retorno");		
//  		ObjectMapper mapperJSON = new ObjectMapper();
//  		mapperJSON.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);	  		
	  	JSONArray pedidosArray = retorno.getJSONArray("pedidos");
		listaRetorno = (ArrayList<Object>) JSONUtil.toList(pedidosArray);
		System.out.println("LISTARETORNO =" + listaRetorno.toString());		
		if (!listaRetorno.isEmpty()) {	  		  	
	  	//String numero = pedidosArray.getJSONObject(0).get("numero").toString();	  	
		  	JSONObject pedido = pedidosArray.getJSONObject(0);
			System.out.println("PEDIDO3 =" + pedido.toString());
	
			JSONObject objPedido = new JSONObject(pedido.toString());
		  	JSONObject pedidoCurr = objPedido.getJSONObject("pedido");
			System.out.println("PEDIDOCurr234 =" + pedidoCurr);
	
			RetornoBling retornoBling = mapperBling.readValue(pedidoCurr.toString(), RetornoBling.class);			
			System.out.println("numero : " + retornoBling.getNumero());						
	//			JsonParser jp = new JsonParser();
	//	        JsonElement root = jp.parse(pedidoCurr.toString());
			
	//			JsonNode node = mapperJSON.readValues(jp, valueType)
	//			System.out.println("id : "+node.findValues("id").get(0).asText());			
	//			JsonParser jp = new JsonParser();
	//	        JsonElement root = jp.parse(pedidoCurr.toString());
	//	        JsonObject jsonobj = root.getAsJsonObject();
	//	        System.out.println("PEDIDO numeroCurrStr =" + jsonobj.toString());	  	    			
	        //JsonObject numero = (JsonObject) jsonobj.get("numero");        
			numeroCurrStr = retornoBling.getNumero().toString();	
			System.out.println("PEDIDO numeroCurrStr =" + numeroCurrStr);	  	    			
				/*		  	JSONArray pedidosArray = retorno.getJSONArray("pedidos");
			  	for(int i = 0 ; i < pedidosArray.length() ; i++){
			  	    String pedido = pedidosArray.getJSONObject(i).getString("pedidos");
					System.out.println("PEDIDO =" + pedido);	  	    
			  	}*/	  				
				//JSONObject numeroObj = new JSONObject(pedidoCurr.toString());
				
			 	//JSONObject numeroCurr = obj.getJSONObject("numero");
				//System.out.println("NUMEROCurr =" + numeroCurr);
				//numeroCurrStr = numeroCurr.toString();
				
			  	//String numeroCurrObj = objPedido.get("numero").toString();
	//			System.out.println("PEDIDOCurrStr =" + numeroCurrObj);
	//			numeroCurr = numeroCurrObj;
	/*			JsonParser jp = new JsonParser();
		        JsonElement root = jp.parse(pedidoCurr.toString());
		        JsonObject jsonobj = root.getAsJsonObject();
		        JsonObject retorno = (JsonObject) jsonobj.get("retorno");	  	
		        JsonObject pedidos = (JsonObject) jsonobj.get("pedidos");
		        JsonObject pedido = (JsonObject) jsonobj.get("pedido");
		        JsonObject numero = (JsonObject) jsonobj.get("numero");        
				numeroCurr = numero.toString();
				System.out.println("NUMEROCurr =" + numeroCurr);			
	*//*			JSONObject objNumero = new JSONObject(pedido.toString());
			  	JSONObject numeroObj = obj.getJSONObject("numero");
				System.out.println("NUMEROCurr =" + numeroObj);*/		  	
				
	/*		  	JSONArray pedidosArray = retorno.getJSONArray("pedidos");
			  	for(int i = 0 ; i < pedidosArray.length() ; i++){
			  	    String pedido = pedidosArray.getJSONObject(i).getString("pedidos");
					System.out.println("PEDIDO =" + pedido);	  	    
			  	}*/	  	
				
	/*			JSONObject pedidoObj = new JSONObject(pedido.toString());
			  	//String pedidoStr = pedido.getString("pedido");
			  	JSONArray pedidoArray = pedidoObj.getJSONArray("pedido");		
				System.out.println("PEDIDOARRAY =" + pedidoArray.toString());	
			  	//JSONObject objNumero = new JSONObject(pedidoStr);		
				//JSONObject numeroObj = new JSONObject(pedido);	
			  	//JSONObject numeroObj = numeroArray.getJSONObject(0);	  	
				JSONObject pedidoStr = pedidoArray.getJSONObject(0);
				System.out.println("PEDIDOStr =" + pedidoStr.toString());*/		
	  	} else
	  	{
	  		//TODO :ESTUDAR COMO AVISAR ESSE ERRO E PROCEGUIR E DEPOIS ATUALIZAR NOVAMENTE PORQUE AO INCLUIR UM PEDIDO E OCORRER O ERRO EH MELHOR SALVAR EM ORDER SEM O NUMERO DO PEDIDO QUE NAO INCLUIR, DEPOIS TENTA INCLUIR NOVAMENTE E ATUALIZA SE FOR O CASO
	  		System.out.println("NAO ENCONTROU O PEDIDO DO RETORNO DO BLING");		
	  	}
				
/*	  	JSONObject numero = pedido.getJSONObject(0);		
	  	//String pedidoStr = pedido.getString("pedido");
		System.out.println("PEDIDOSTR =" + pedidoStr);	  	
	  	//String numero = pedido.getString("numero");
	  	JSONObject pedidoObj = new JSONObject(pedidoStr);
	  	String numero = pedidoObj.getString("numero");
		System.out.println("NUMERO =" + numero);*/	 
		
		
/*		JSONObject jsonObject = new JSONObject(responseResult);	  	
		String retorno = jsonObject.getString("retorno");
		System.out.println("RETORNO =" + retorno); 		
		JSONObject jsonObjectPedidos = new JSONObject(retorno);		
		String pedidos = jsonObjectPedidos.getString("pedidos");
		System.out.println("PEDIDOS =" + pedidos);		
		JSONObject jsonObjectPedido = new JSONObject(pedidos);		
		String numero = jsonObjectPedido.getString("numero");*/
//		String descriptionStr = jsonObject.getString("description");
//		String quantityStr = jsonObject.getString("quantity");
//		String amountItemStr = jsonObject.getString("amount");
	  	
/*	  	JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(responseResult);
        JsonObject jsonobj = root.getAsJsonObject();
        JsonObject retorno = (JsonObject) jsonobj.get("retorno");	  	
        JsonObject pedidos = (JsonObject) retorno.get("pedidos");
        JsonObject pedido = (JsonObject) pedidos.get("pedido");
        JsonObject numero = (JsonObject) pedido.get("numero");*/        
	  	
	  	
		//JSONObject jsonObject = new JSONObject(responseResult);
  	
        return numeroCurrStr;		
	}
}