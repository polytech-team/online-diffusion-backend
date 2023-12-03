package team.polytech.online.diffusion.service.auth.response;

import team.polytech.online.diffusion.model.InvalidData;

import java.util.List;

public class RegistrationResponse {
    private final String uuid;
    private final List<InvalidData> data;

    public RegistrationResponse(String uuid, List<InvalidData> data) {
        this.uuid = uuid;
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public List<InvalidData> getData() {
        return data;
    }
}
