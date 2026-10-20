package school.sptech.apl_api_barbershop.dto.despesa;

import java.math.BigDecimal;
import java.util.List;

public class DespesaMesResponse {

    private List<DespesaResponse> itens;
    private BigDecimal totalMes;

    public DespesaMesResponse() {
    }

    public DespesaMesResponse(List<DespesaResponse> itens, BigDecimal totalMes) {
        this.itens = itens;
        this.totalMes = totalMes;
    }

    public List<DespesaResponse> getItens() { return itens; }
    public BigDecimal getTotalMes() { return totalMes; }
}