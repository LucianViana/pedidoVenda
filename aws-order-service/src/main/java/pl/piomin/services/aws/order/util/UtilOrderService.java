package pl.piomin.services.aws.order.util;

public class UtilOrderService {
	public static String getStatusPagseguro(String statusPedido) {
		int status = Integer.parseInt(statusPedido);
		String descricaoStatus = "";
        switch (status) {
            case 1:
                descricaoStatus = "Aguardando pagamento";
                break;
            case 2:
                descricaoStatus = "Em análise";
                break;
            case 3:
                descricaoStatus = "Paga";
                break;
            case 4:
                descricaoStatus = "Disponível";
                break;
            case 5:
                descricaoStatus = "Em disputa";
                break;
            case 6:
                descricaoStatus = "Devolvida";
                break;
             case 7:
                 descricaoStatus = "Cancelada";
                break;
            default:
                 System.out.println("Status não definido!");
      }		
      return descricaoStatus;
	}
}
