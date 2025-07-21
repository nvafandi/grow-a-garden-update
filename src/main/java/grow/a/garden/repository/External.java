package grow.a.garden.repository;

import grow.a.garden.dto.response.stok.UpdateResponse;

public interface External {

    UpdateResponse getStock();

    void sendMessage(String message);

    boolean isMessageSame(String message);
}
