package grow.a.garden.dto.response.stok;

import lombok.Data;

import java.util.List;

@Data
public class Travelingmerchant {

    private List<Items> items;

    private String countdown;

    private String status;

    private String appearIn;

    private String merchantName;

}
