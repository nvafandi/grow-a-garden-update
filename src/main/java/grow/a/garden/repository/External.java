package grow.a.garden.repository;

import grow.a.garden.dto.response.stok.UpdateStockResponse;
import grow.a.garden.dto.response.user.UsersResponse;

import java.util.List;

public interface External {

    UpdateStockResponse getStock();

    UsersResponse getUsers();

    void sendMessage(String message);

    void sendMessageV2(String message, List<String> users);

    boolean checkExistingMessage(String message);
}
