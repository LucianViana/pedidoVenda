package pl.piomin.services.aws.order.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class ProcessBling implements RequestHandler<DynamodbEvent, String> {

	private AmazonSNSClient client;
	private ObjectMapper jsonMapper;
	
	public ProcessBling() {
		client = new AmazonSNSClient();
		jsonMapper = new ObjectMapper();
	}
	
	public String handleRequest(DynamodbEvent event, Context ctx) {
		LambdaLogger logger = ctx.getLogger();	
		final List<DynamodbStreamRecord> records = event.getRecords();
		List<AttributeValue> itemsAtrib = null;
		    List<Item> listOfItem = new ArrayList<>();
	        List<Map<String, AttributeValue>> listOfMaps = null;	        
	        for (DynamodbStreamRecord record : event.getRecords()) {
	            //if (INSERT.equals(record.getEventName()) || MODIFY.equals(record.getEventName())) {
                listOfMaps = new ArrayList<Map<String, AttributeValue>>();
                listOfMaps.add(record.getDynamodb().getNewImage());
                listOfItem = InternalUtils.toItemList(listOfMaps);
				Map<String, AttributeValue> m = record.getDynamodb().getNewImage();
				logger.log("CONTEUDO M =" + m.toString());
				itemsAtrib = m.get("bins").getL();
				String amountStr = m.get("amount").getS();
				String accountIdStr = m.get("accountId").getS();				
				String nameStr = m.get("name").getS();				
				String dataStr = m.get("data").getS();				
				String statusPagseguroStr = m.get("statusPagseguro").getS();
				String referStr = m.get("transactionPagseguro").getS();				
				String toStr = m.get("email").getS();				
				
            	String address01 = m.get("address01").getS();
            	String city = m.get("city").getS();
            	String state = m.get("state").getS();
            	String zip = m.get("zip").getS();
            	String empresa = m.get("empresa").getS();
            	String enderecoEmpresa = m.get("enderecoEmpresa").getS();
            	String cidadeEmpresa = m.get("cidadeEmpresa").getS();
            	String estadoEmpresa = m.get("estadoEmpresa").getS();
            	String cepEmpresa = m.get("cepEmpresa").getS();
            	String telEmpresa = m.get("telEmpresa").getS();
            	
            	logger.log("CONTEUDO BINS Atrib=" + itemsAtrib.toString());						
				String itemsStr = itemsAtrib.get(0).getS().toString();
				logger.log("CONTEUDO BINS String=" + itemsStr);
				
				
	            //}

	            //logger.log(listOfItem);
	            try {
	               // String json = new ObjectMapper().writeValueAsString(listOfItem.get(0));
	                Gson gson = new Gson();
	                Item item = listOfItem.get(0);

	                String json = gson.toJson(item.asMap());
	                logger.log("JSON is 2 ");
	                logger.log(json);

		  	        String xml = "<?xml version='1.0' encoding='iso8859-1'?>" 
		  		    		+ "<pedido>" 
		  	                + "<cliente>" 
		  	                + "<nome>" + nameStr + "</nome>"
		  	                + "<tipoPessoa>F</tipoPessoa>"
		  	                + "<endereco>" + address01 + "</endereco>"
		  	                + "<cpf_cnpj>00000000000000</cpf_cnpj>"
		  	                + "<ie_rg>000000</ie_rg>"
		  	                + "<numero>0</numero>"
		  	                + "<complemento></complemento>"
		  	                + "<bairro></bairro>"
		  	                + "<cep>" + zip + "</cep>"
		  	                + "<cidade>" + city + "</cidade>"
		  	                + "<uf>" + state + "</uf>"
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
		  	                + "<numero>392</numero>"
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
		  	                + "<codigo>003</codigo>"
		  	                + "<descricao>Teclado 003</descricao>"
		  	                + "<un>Cx</un>"
		  	                + "<qtde>7</qtde>"
		  	                + "<vlr_unit>18.65</vlr_unit>"
		  	                + "</item>"
		  	                + "</itens>"
		  	                + "<parcelas>"
		  	                + "<parcela>"
		  	                + "<data>01/09/2009</data>"
		  	                + "<vlr>100</vlr>"
		  	                + "<obs>Teste obs 1</obs>"
		  	                + "</parcela>"
		  	                + "<parcela>"
		  	                + "<data>06/09/2009</data>"
		  	                + "<vlr>50</vlr>"
		  	                + "<obs></obs>"
		  	                + "</parcela>"
		  	                + "<parcela>"
		  	                + "<data>11/09/2009</data>"
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
	                //EnviarEmail.enviaEmail("danbislojavirtual@gmail.com", "luciano.souzaviana@gmail.com", accountIdStr, nameStr, dataStr, statusPagseguroStr, referStr, itemsStr, amountStr, address01, city, state, zip, empresa, enderecoEmpresa, cidadeEmpresa, estadoEmpresa, cepEmpresa, telEmpresa, ctx); 	                
	                //EnviarEmail.enviaEmail("luciano.souzaviana@gmail.com", "danbislojavirtual@gmail.com", "dateste titulo", "teste corpo");	                
	            }catch (Exception e){
	                e.printStackTrace();
	            }
	        }
/*		LambdaLogger logger = ctx.getlogger.log();	
		final List<DynamodbStreamRecord> records = event.getRecords();
		List<Map<String, AttributeValue>> listOfMaps = null;
		List<Item> listOfItem = new ArrayList<>();
		for (DynamodbStreamRecord record : records) {
			try {
				listOfMaps = new ArrayList<Map<String, AttributeValue>>();
                listOfMaps.add(record.getDynamodb().getNewImage());
                listOfItem = InternalUtils.toItemList(listOfMaps);
                Gson gson = new Gson();
                Item item = listOfItem.get(0);

                String json = gson.toJson(item.asMap());
                logger.log("JSON is ");
                logger.log(json);      */
	    //List<DynamodbEvent.DynamodbStreamRecord> dynamodbStreamRecordlist = event.getRecords();

        //DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient());
        //logger.log("Whole event - "+event.toString());            		
		
/*		for (DynamodbStreamRecord record : records) {
			try {
				logger.log(String.format("DynamoEvent: %s, %s", record.getEventName(), record.getDynamodb().getNewImage().values().toString()));
				logger.log("CONTEUDO RECORD=" + record.toString());
				Map<String, AttributeValue> m = record.getDynamodb().getNewImage();
				logger.log("CONTEUDO M =" + m.toString());
				Order order = new Order(m.get("id").getS(), m.get("accountId").getS(), Integer.parseInt(m.get("amount").getN()), m.get("transaction").getS(), m.get("data").getS(), m.get("status").getS(), m.get("produtos").getS());
				logger.log("PRODUTOS=" + m.get("produtos").getS());
				//Order order = new Order("10", "1", 1500);
				String msg = jsonMapper.writeValueAsString(order);
				logger.log(String.format("SNS message: %s", msg));
		  	  //PublishRequest req = new PublishRequest("arn:aws:sns:us-east-1:658226682183:order", jsonMapper.writeValueAsString(new OrderMessage(msg)), "Order");
				PublishRequest req = new PublishRequest("arn:aws:sns:us-east-1:505529903636:order", jsonMapper.writeValueAsString(new OrderMessage(msg)), "Order");
				
				req.setMessageStructure("json");
				PublishResult res = client.publish(req);
				logger.log(String.format("SNS message sent: %s", res.getMessageId()));
			} catch (JsonProcessingException e) {
				logger.log(e.getMessage());
			}
		}*/
		return "OK";
	}
    public void enviarOrderBling(String transactionSenderEmail, String transactionCode, String transactionNetAmount, String transactionStatus, String transactionDate, String itemsStr, String name, String email, String address01, String city, String estado, String cep, String empresa, String enderecoEmpresa, String cidadeEmpresa, String estadoEmpresa, String cepEmpresa, String telEmpresa) {
	    Logger logger = Logger.getLogger(ProcessOrder.class.getName()); 										
        try {    	
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Erro envio ordem serviço Bling=" + e); 									
		}		
    }	
}
  