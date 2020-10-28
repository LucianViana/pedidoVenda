package pl.piomin.services.aws.order.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil implements Serializable{
    //TODO :FOI ALTERADO O TRATAMENTO DE ERRO DO lerJSONString  E DO readTextFile( DO JSONUTIL PARA NAO DAR ERRO QUANDO NAO ENCONTRAR O ARQUIVO FIXO JSON OLHAR SE SERA POSSIVEL MANTER O TRATAMENTO E ENCONTRAR OUTRA FORMA
    public static String readTextFile(String fileName) {
		String returnValue = "";
		FileReader file;
		String line = "";
		try {
			//file = new FileReader(fileName);
			//BufferedReader reader = new BufferedReader(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
						
			while ((line = reader.readLine()) != null) {
				returnValue += line + "\n";
			}
		} catch (FileNotFoundException e) {
			//throw new RuntimeException("File not found");
		} catch (IOException e) {
			//throw new RuntimeException("IO Error occured");
		}
		return returnValue;

	}
    public static Object toJSON(Object object) throws JSONException {
        if (object instanceof Map) {
            JSONObject json = new JSONObject();
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), toJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable)object)) {
                json.put(value);
            }
            return json;
        } else {
            return object;
        }
    }
 
    public static boolean isEmptyObject(JSONObject object) {
        return object.names() == null;
    }
 
    public static Map<String, Object> getMap(JSONObject object, String key) throws JSONException {
        return toMap(object.getJSONObject(key));
    }
 
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }
 
    public static List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }
 
    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }
    //TODO: FALTA COLOCAR NO JSONUTIL ESSE METODO E CRIAR O PATH ABSOLUTO.
    public static boolean deleteFile(String file) {  
  	  try {  
  	   File fileToDelete = new File(file);
  	   System.gc();
  	   //TODO :ESTA EXCLUINDO MAS ESTA EXIBINDO A MENSAGEM FILE DELETE OPARATION FAILED, OLHAR
  	   System.out.println("ESTA EXCLUINDO MAS ESTA EXIBINDO A MENSAGEM FILE DELETE OPARATION FAILED, OLHAR");
  	   if (fileToDelete.delete()) {  
  	    System.out.println("File deleted successfully !");  
  	   } else {  
  	    System.out.println("File delete operation failed !");  
  	   }  
  	  } catch (Exception e) {  
  	   e.printStackTrace();
  	   return false;
  	  }  
  	  return true;
 	 }  
    public static String buildJSON(List<Object> lista, String title) {
		StringBuffer returnJSON  = new StringBuffer();
		Object key = "";
		Object value = "";
		HashMap map = null;
        returnJSON.append("{ "+'"'+ title +'"'+ ":[");
	    for(int j = 0; j < lista.size(); j++){
	        HashMap Mapaux2 = (HashMap)lista.get(j);
			Iterator it = Mapaux2.entrySet().iterator();
			returnJSON.append("{");			
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				value = (Object) e.getValue();
				key = (Object) e.getKey();
				returnJSON.append("\"" + key + "\": \"" + value + "\",");
			}
	        int lastCharIndex	 = returnJSON.length();
	        returnJSON.deleteCharAt(lastCharIndex -1);
			returnJSON.append("},");			
	    };	
        int lastCharIndex	 = returnJSON.length();
        returnJSON.deleteCharAt(lastCharIndex -1);
        returnJSON.append("\r\n]}");  		
        return returnJSON.toString();
    }
    public static String buildJSONStr(String content, String title) {
		StringBuffer returnJSON  = new StringBuffer();
		Object key = "";
		Object value = "";
		HashMap map = null;
        returnJSON.append("{ "+'"'+ title +'"'+ ":[");
        returnJSON.append(content);        
	    //for(int j = 0; j < lista.size(); j++){
	    //    HashMap Mapaux2 = (HashMap)lista.get(j);
		//	Iterator it = Mapaux2.entrySet().iterator();
		//	returnJSON.append("{");			
		//	while (it.hasNext()) {
		//		Map.Entry e = (Map.Entry) it.next();
		//		value = (Object) e.getValue();
		//		key = (Object) e.getKey();
		//		returnJSON.append("\"" + key + "\": \"" + value + "\",");
		//	}
	     //   int lastCharIndex	 = returnJSON.length();
	      //  returnJSON.deleteCharAt(lastCharIndex -1);
		//	returnJSON.append("},");			
	    //};	
        //int lastCharIndex	 = returnJSON.length();
        //returnJSON.deleteCharAt(lastCharIndex -1);
        returnJSON.append("\r\n]}");  		
        return returnJSON.toString();
    }
    
    //TODO :FOI ALTERADO O TRATAMENTO DE ERRO DO lerJSONString  E DO readTextFile( DO JSONUTIL PARA NAO DAR ERRO QUANDO NAO ENCONTRAR O ARQUIVO FIXO JSON OLHAR SE SERA POSSIVEL MANTER O TRATAMENTO E ENCONTRAR OUTRA FORMA
    public static ArrayList<Object> lerJSONString(String title, String user, String versao) throws JSONException{
    	//System.out.println();
    	String readTextFile = readTextFile(title + "_" + user + "_" + versao +  ".json");
    	//readTextFile = readTextFile.replaceAll("\r", ""); 
    	//readTextFile = readTextFile.replaceAll("\t", "");
    	//readTextFile = readTextFile.replaceAll("\n", "");

    	
		ArrayList<Object> listaProductsArrayList = new ArrayList<Object>();
		int qtd = 0;
		try {
			//System.out.println();
			//readTextFile = readTextFile.replace("[", "").replace("]", "");
			if (!readTextFile.equals("")&&!readTextFile.equals(null)) {
				int i = readTextFile.indexOf("{");
				readTextFile = readTextFile.substring(i);			
	   			JSONObject jSONObject = new JSONObject(readTextFile.trim());
	   			JSONArray products = jSONObject.getJSONArray(title);
	   			List listaProducts = toList(products);
	   			qtd = listaProducts.size();
			    for(int j = 0; j < qtd; j++){
			        HashMap Mapaux2 = (HashMap)listaProducts.get(j);
					listaProductsArrayList.add(Mapaux2);
		        }
			}    
		} catch (org.json.JSONException e) {
			System.out.println("erro json=" + e);
		}
		return listaProductsArrayList;	
	}
    public static boolean criaJSONString(String title, List<Object> listaTagFis, String user, String versao) {
	  //Cria um Objeto JSON
	  try {    	
	    FileWriter writeFile = null; 
	    String responseString = null;
        responseString = buildJSON(listaTagFis, title);
        excluirArquivo(title + "_" + user + "_" + versao +  ".json");        
    	writeFile = new FileWriter(title + "_" + user + "_" + versao +  ".json");	    	
    	//Escreve no arquivo conteudo do Objeto JSON
    	writeFile.write(responseString); 
    	writeFile.close();
      } 
	  //TODO :ESTA RETORNANDO FALSE QUANDO EH CRIADO O ARQUIVO PROJETOS_TAG MAS CRIA CORRETAMENTE OLHAR
      catch(IOException e){
     	e.printStackTrace();
     	return false;
      } 
	  return true;
    }
    public static boolean criaJSONStringTemp(String fileTemp, String responseString, String user, String versao) {
	  //Cria um Objeto JSON
	  try {    	
	    //FileWriter writeFile = null; 
	    //String responseString = null;
        //responseString = buildJSON(listaTagFis, title);
        excluirArquivo(fileTemp + "_" + user + "_" + versao +  ".json");        
    	//writeFile = new FileWriter(fileTemp + "_" + user + "_" + versao +  ".json");
	    OutputStreamWriter writeFile = new OutputStreamWriter(new FileOutputStream(fileTemp + "_" + user + "_" + versao +  ".json"),"UTF-8");    	
    	//Escreve no arquivo conteudo do Objeto JSON
    	writeFile.write(responseString); 
    	writeFile.close();
      } 
	  //TODO :ESTA RETORNANDO FALSE QUANDO EH CRIADO O ARQUIVO PROJETOS_TAG MAS CRIA CORRETAMENTE OLHAR
      catch(IOException e){
     	e.printStackTrace();
     	return false;
      } 
	  return true;
    }    
    public static boolean criaJSONString(String file, String responseString, String user, String versao) {
	  //Cria um Objeto JSON
	  try {    	
	    //FileWriter writeFile = null; 
	    //String responseString = null;
        //responseString = buildJSON(listaTagFis, title);
        excluirArquivo(file + "_" + user + "_" + versao +  ".json");        
    	//writeFile = new FileWriter(fileTemp + "_" + user + "_" + versao +  ".json");
	    OutputStreamWriter writeFile = new OutputStreamWriter(new FileOutputStream(file + "_" + user + "_" + versao +  ".json"),"UTF-8");    	
    	//Escreve no arquivo conteudo do Objeto JSON
    	writeFile.write(responseString); 
    	writeFile.close();
      } 
	  //TODO :ESTA RETORNANDO FALSE QUANDO EH CRIADO O ARQUIVO PROJETOS_TAG MAS CRIA CORRETAMENTE OLHAR
      catch(IOException e){
     	e.printStackTrace();
     	return false;
      } 
	  return true;
    }    
    public static boolean criaJSONStringTemp(String fileTemp, String file, List<Object> listaTagFis, String user, String versao) {
	  //Cria um Objeto JSON
	  try {    	
	    //FileWriter writeFile = null; 
	    String responseString = null;
        responseString = buildJSON(listaTagFis, file);
        //NAO TEM TRATAMENTO POIS SOMENTE EXCLUI SE HOUVER ALGUM PROBLEMA ANTERIOR PARA SALVAR CORRETAMENTE O TEMP
        boolean excluirTemp = deleteFile(fileTemp + "_" + user + "_" + versao +  ".json");
	    OutputStreamWriter writeFile = new OutputStreamWriter(new FileOutputStream(fileTemp + "_" + user + "_" + versao +  ".json"),"UTF-8");        
    	//writeFile = new FileWriter(fileTemp + "_" + user + "_" + versao + ".json");	    	
    	//Escreve no arquivo conteudo do Objeto JSON
    	writeFile.write(responseString); 
    	writeFile.close();
      } 
      catch(IOException e){
     	e.printStackTrace();
   	    System.out.println("erro ao criar arquivo: " + fileTemp + "_" + user + "_" + versao + ".json");     	
     	return false;
      }
  	  System.out.println("arquivo criado com sucesso: " + fileTemp + "_" + user + "_" + versao + ".json");	  
	  return true;
	  
    }    
    public static boolean ifExistsArquivoJSON(String title, String user, String versao) {
    	String filePath = title + "_" + user + "_" + versao +  ".json";
		String returnValue = "";
		FileReader file;
		String line = "";
		try {
			file = new FileReader(filePath);
			BufferedReader reader = new BufferedReader(file);
				while ((line = reader.readLine()) != null) {
					returnValue += line + "\n";
				}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
		// TODO Auto-generated catch block
		    e.printStackTrace();
	    }
		
		return true;
	}
    public static void excluirArquivoRename(String arquivoRename) {
        boolean excluirTemp = deleteFile(arquivoRename);
        if (excluirTemp) {
        	System.out.println("arquivo excluido com sucesso: " + arquivoRename);
        } else
        {
        	System.out.println("arquivo n�o encontrado: " + arquivoRename);		            	
        }				
    }    
    public static void excluirArquivoBkp(String arquivoBkp) {
        boolean excluirTemp = deleteFile(arquivoBkp);
        if (excluirTemp) {
        	System.out.println("arquivo excluido com sucesso: " + arquivoBkp);
        } else
        {
        	System.out.println("arquivo n�o encontrado: " + arquivoBkp);		            	
        }				
    }
    public static void excluirArquivo(String arquivo) {
        boolean excluir = deleteFile(arquivo);
        if (excluir) {
        	System.out.println("arquivo excluido com sucesso: " + arquivo);
        } else
        {
        	System.out.println("arquivo n�o encontrado: " + arquivo);		            	
        }				
    }
    public static String retira_html(String html) {
        String noTagRegex = "<[^>]+>";
        //TODO : OLHAR PORQUE NAO ACEITA .replaceAll("\s", " ")
        return normalize(html);//html.replaceAll("&nbsp;", "").replaceAll("\n", " ").replaceAll(noTagRegex, "");
        
    }    
    
    /** Para a normaliza��o dos caracteres de 32 a 255, primeiro caracter */
/*	private static final char[] FIRST_CHAR =
			(" !'#$%&'()*+\\-./0123456789:;<->?@ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ E ,f'.++^%S<O Z  ''''.-"
				+ "-~Ts>o ZY !C#$Y|$'(a<--(_o+23'u .,1o>113?AAAAAAAEEEEIIIIDNOO"
				+ "OOOXOUUUUyTsaaaaaaaeeeeiiiidnooooo/ouuuuyty")
				.toCharArray();
		*//** Para a normaliza��o dos caracteres de 32 a 255, segundo caracter *//*
		private static final char[] SECOND_CHAR =
			("  '         ,                                               "
				+ "\\                                   $  r'. + o  E      ''  "
				+ "  M  e     #  =  'C.<  R .-..     ..>424     E E            "
				+ "   E E     hs    e e         h     e e     h    ")
				.toCharArray();
		*//**    
	/**
	 * Efetua as seguintes normaliza��es para obten��o de certificados:
	 * - Elimina acentos e cedilhas dos nomes;
	 * - Converte aspas duplas em simples;
	 * - Converte algumas letras estrangeiras para seus equivalentes ASCII
	 * (como �, convertido para ss) 
	 * - P�e um "\" antes de v�rgulas, permitindo nomes como
	 * "Verisign, Corp." e de "\", permitindo nomes como " a \ b ";
	 * - Converte os sinais de = para -
	 * - Alguns caracteres s�o removidos:
	 * -> os superiores a 255,
	 * mesmo que possam ser representados por letras latinas normais
	 * (como s, = U+015E = Latin Capital Letter S With Cedilla);
	 * -> os caracteres de controle (exceto tab, que � trocado por um espa�o)
	 * @param str A string a normalizar.
	 * @return A string normalizada.
	 */
	public static String normalize(String str) {
		String text = str;
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
		return text.replaceAll("[^\\p{ASCII}]", "");
	}    
}