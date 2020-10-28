package pl.piomin.services.aws.order.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class EnviarEmail {
	 private static class DynamicTemplatePersonalization extends Personalization {

	        @JsonProperty(value = "dynamic_template_data")
	        private Map<String, Object> dynamicTemplateData;

	        @JsonProperty("dynamic_template_data")
	        public Map<String, Object> getDynamicTemplateData() {
	            if (dynamicTemplateData == null) {
	                return Collections.<String, Object>emptyMap();
	            }
	            return dynamicTemplateData;
	        }

	        public void addDynamicTemplateData(String key, Object value) {
	            if (dynamicTemplateData == null) {
	                dynamicTemplateData = new HashMap<String, Object>();
	                dynamicTemplateData.put(key, value);
	            } else {
	                dynamicTemplateData.put(key, value);
	            }
	        }

	    }
	 
  public static boolean enviaEmail(String fromStr, String toStr, String accountIdStr, String nameStr, String dataStr, String statusPagseguroStr, String referStr, String itemsStr, String amountStr, String address01, String city, String estado, String cep, String empresa, String enderecoEmpresa, String cidadeEmpresa, String estadoEmpresa, String cepEmpresa, String telEmpresa, Context ctx) throws Exception {
	    boolean processamntoOk = false;	  
/*	    Email from = new Email(fromStr);
	    String subject = subjectStr;
	    Email to = new Email(toStr);
	    Content content = new Content("text/plain", contentStr);
	    Mail mail = new Mail(from, subject, to, content);
	    Email email = new Email("test2@example.com");
	    mail.personalization.get(0).addTo(email);*/
	    
	    SendGrid sg = new SendGrid("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");	    
/*	    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));*/
	    sg.addRequestHeader("X-Mock", "true");

	    Request request = new Request();
/*	    Mail helloWorld = buildHelloEmail();*/
	    Mail helloWorld = buildHelloEmail(fromStr, toStr, accountIdStr, nameStr, dataStr, statusPagseguroStr, referStr, itemsStr, amountStr, address01, city, estado, cep, empresa, enderecoEmpresa, cidadeEmpresa, estadoEmpresa, cepEmpresa, telEmpresa, ctx);
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(helloWorld.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());	    

/*	    SendGrid sg = new SendGrid("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");*/
/*	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());*/
	    } catch (IOException ex) {
	      throw ex;
	    }
	    return processamntoOk;
  }
  public static Mail buildHelloEmail(String fromStr, String toStr, String accountIdStr, String nameStr, String dataStr, String statusPagseguroStr, String referStr, String itemsStr, String amountStr, String address01, String city, String estado, String cep, String empresa, String enderecoEmpresa, String cidadeEmpresa, String estadoEmpresa, String cepEmpresa, String telEmpresa, Context ctx) throws Exception {
		LambdaLogger logger = ctx.getLogger();	  
	    Mail mail = new Mail();	  
	    Email from = new Email(fromStr);
	    String subject = "compra realizada";
	    //Email to = new Email(toStr);
	    Content content = new Content();
	    content.setType("text/plain");
	    content.setValue("compra realizada");
	    mail.addContent(content);
	    content.setType("text/html");
	    content.setValue("<html><body>Compra realizada</body></html>");
	    mail.addContent(content);	    
	    mail.setTemplateId("d-74800237442e4d1aa26306bace19cf92");
	    mail.setSubject("Compra realizada.");
//	    Content content = new Content("text/plain", "corpo");
	    // Note that when you use this constructor an initial personalization object
	    // is created for you. It can be accessed via
	    // mail.personalization.get(0) as it is a List object

	    //Email email = new Email("test2@example.com");
	    //mail.personalization.get(0).addTo(email);
	    Email fromEmail = new Email();
	    //fromEmail.setName("Example User");
	    fromEmail.setEmail(fromStr);
	    mail.setFrom(fromEmail);	    	    

	    
	    /*	    Email from = new Email(fromStr);
	    String subject = subjectStr;
	    Email to = new Email(toStr);
	    Content content = new Content("text/plain", contentStr);
	    Mail mail = new Mail(from, subject, to, content);
	    Email email = new Email("test2@example.com");
	    mail.personalization.get(0).addTo(email);*/
	    
	    
	    //mail.addCustomArg("username", "xxxxx");
/*	    Personalization personalization = new Personalization();
	    Email to = new Email();
	    to.setName("Example User");
	    to.setEmail(toStr);
	    personalization.addTo(to);
	    //personalization.addBcc(bcc);
	    personalization.setSubject("Hello World from the Personalized SendGrid Java Library");
//	    personalization.addHeader("X-Test", "test");
//	    personalization.addHeader("X-Mock", "true");
	    //personalization.addSubstitution("username", "Example User");
	    personalization.add*/
	    //personalization.addSubstitution("%city%", "Riverside");
	    //personalization.addCustomArg("user_id", "343");
//	    personalization.addCustomArg("type", "marketing");
//	    personalization.setSendAt(1443636843);
        DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
        Email to = new Email();
        //to.setName("LUCIANO");
        to.setEmail(toStr);
	    //personalization.setSubject("Compra realizada.");        
        personalization.addTo(to);
		String[] primeiroNome = nameStr.trim().split(" "); 
		String primeiroNomeStr =  primeiroNome[0];			        
        personalization.addDynamicTemplateData("username", primeiroNomeStr);	    
        personalization.addDynamicTemplateData("customerId", "teste");
        //String dataConvert = Util.formataData(dataStr);
        //logger.log("DATA CONVERT=" + dataConvert);
        personalization.addDynamicTemplateData("data", dataStr);        
        String descStatusPagseguroStr = UtilOrderService.getStatusPagseguro(statusPagseguroStr);
        personalization.addDynamicTemplateData("descStatusPagseguro", descStatusPagseguroStr);        
        personalization.addDynamicTemplateData("refer", referStr);
        
        //DynamicTemplatePersonalization personalizationItems = new DynamicTemplatePersonalization();
        
		logger.log("CONTEUDO DO ITEMSTR=" + itemsStr);
		List<String> itemsList = new ArrayList<String>(Arrays.asList(itemsStr.split("}")));
		//String itemsContent = "";
        List<Map<String, String>> items = new ArrayList<>();		
		

		for (int i = 0; i < itemsList.size(); i++) {
			String itemStr = "{" + itemsList.get(i).toString().replace(",{", "").replace("{", "") + "}";
			JSONObject jsonObject = new JSONObject(itemStr);
			String idStr = jsonObject.getString("id");
			String descriptionStr = jsonObject.getString("description");
//			String quantityStr = jsonObject.getString("quantity");
			String amountItemStr = jsonObject.getString("amount");
	        Map<String, String> item = new HashMap<>();			
			//itemsContent = itemsContent + "|" + "item:" + i +  idStr + "-" + descriptionStr + " Qtd:" + quantityStr + " Total item:" + amountStr + " ";
	        item.put("item", idStr);			
	        item.put("text", descriptionStr);
	        //item.put("text", descriptionStr);
	        item.put("price", amountItemStr);
	        items.add(item);
		}	
		logger.log("ITEMS ENVIAR EMAIL =" + items.toString());
        //List<Map<String, String>> items = new ArrayList<>();        
		//String itemStr =
//        Map<String, String> item1 = new HashMap<>();
//        item1.put("text", "New Line Sneakers");
//        item1.put("price", "$ 79.95");
//        items.add(item1);
//        Map<String, String> item2 = new HashMap<>();
//        item2.put("text", "Old Line Sneakers");
//        item1.put("price", "$ 59.95");
//        items.add(item2);
		
		
        personalization.addDynamicTemplateData("items", items);        
        personalization.addDynamicTemplateData("total", amountStr);         
        personalization.addDynamicTemplateData("name", nameStr);        
        personalization.addDynamicTemplateData("address01", address01);
        //personalization.addDynamicTemplateData("address02", "teste");
        personalization.addDynamicTemplateData("city", city);        
        personalization.addDynamicTemplateData("state", estado);        
        personalization.addDynamicTemplateData("zip", cep);       
        
        personalization.addDynamicTemplateData("empresa", empresa);        
        personalization.addDynamicTemplateData("enderecoEmpresa", enderecoEmpresa);        
        personalization.addDynamicTemplateData("cidadeEmpresa", cidadeEmpresa);        
        personalization.addDynamicTemplateData("estadoEmpresa", estadoEmpresa);        
        personalization.addDynamicTemplateData("cepEmpresa", cepEmpresa);        
        personalization.addDynamicTemplateData("telEmpresa", telEmpresa);        
		
/*        personalization.addDynamicTemplateData("item", "1");  
        personalization.addDynamicTemplateData("text", "texto produto");
        personalization.addDynamicTemplateData("price", "100,00");
        
        personalization.addDynamicTemplateData("item", "2");  
        personalization.addDynamicTemplateData("text", "texto produto2");
        personalization.addDynamicTemplateData("price", "102,00");*/        
        
        //String idStr ="1";
        //String descriptionStr ="1";
        //String quantityStr ="1";
        //String amountStr ="1";
        //String itemsStr = "{\"id\":" + "\"" + idStr + "\",\"description\":" + "\"" + descriptionStr + "\",\"quantity\":" + "\"" + quantityStr + "\",\"amount\":" + "\"" + amountStr + "\"}";

        //personalization.addDynamicTemplateData("items", itemsStr.toString());
	    mail.addPersonalization(personalization);

	    return mail;
	  }
  
}