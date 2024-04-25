package quarkus.bank.stimulator.utils;

import jakarta.ws.rs.core.Response;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomResponse {
    private String message;
    private Response.Status status;
    private Object data;

    public CustomResponse(String message, Response.Status status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

}
