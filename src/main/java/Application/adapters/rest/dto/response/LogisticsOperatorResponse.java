package Application.adapters.rest.dto.response;

import Application.domain.model.user.LogisticsOperator;
import lombok.Getter;

@Getter
public class LogisticsOperatorResponse {

    private final Long id;
    private final String fullName;
    private final String email;

    private LogisticsOperatorResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public static LogisticsOperatorResponse from(LogisticsOperator operator) {
        return new LogisticsOperatorResponse(operator.getId(), operator.getFullName(), operator.getEmail());
    }
}
