package pl.piomin.services.aws.order.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

import pl.piomin.services.aws.order.util.EnviarEmail;

public class ProcessOrder implements RequestHandler<DynamodbEvent, String> {

	private AmazonSNSClient client;
	private ObjectMapper jsonMapper;
	
	public ProcessOrder() {
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

	                
	                
	                EnviarEmail.enviaEmail("danbislojavirtual@gmail.com", "luciano.souzaviana@gmail.com", accountIdStr, nameStr, dataStr, statusPagseguroStr, referStr, itemsStr, amountStr, address01, city, state, zip, empresa, enderecoEmpresa, cidadeEmpresa, estadoEmpresa, cepEmpresa, telEmpresa, ctx); 	                
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
  