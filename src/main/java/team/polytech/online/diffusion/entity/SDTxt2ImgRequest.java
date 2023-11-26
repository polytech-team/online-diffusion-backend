package team.polytech.online.diffusion.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import team.polytech.automatic.webui.model.StableDiffusionProcessingTxt2Img;

import javax.annotation.Nonnull;

public class SDTxt2ImgRequest {
    private static final String JSON_PROPERTY_UUID = "UUID";
    private static final String JSON_PROPERTY_USER_ID = "user_id";
    private static final String JSON_PROPERTY_REQUEST = "request";
    private static final String JSON_PROPERTY_MODEL = "model";

    private String UUID;
    private long userId;
    private String model;
    private StableDiffusionProcessingTxt2Img request;

    public SDTxt2ImgRequest() {
    }

    public SDTxt2ImgRequest(String UUID, long userId, String model, StableDiffusionProcessingTxt2Img request) {
        this.UUID = UUID;
        this.userId = userId;
        this.model = model;
        this.request = request;
    }

    @Nonnull
    @JsonProperty(JSON_PROPERTY_UUID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getUUID() {
        return UUID;
    }

    @Nonnull
    @JsonProperty(JSON_PROPERTY_REQUEST)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public StableDiffusionProcessingTxt2Img getRequest() {
        return request;
    }

    @JsonProperty(JSON_PROPERTY_UUID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUUID(@Nonnull String UUID) {
        this.UUID = UUID;
    }

    @JsonProperty(JSON_PROPERTY_REQUEST)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRequest(@Nonnull StableDiffusionProcessingTxt2Img request) {
        this.request = request;
    }

    @JsonProperty(JSON_PROPERTY_USER_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public long getUserId() {
        return userId;
    }

    @JsonProperty(JSON_PROPERTY_USER_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @JsonProperty(JSON_PROPERTY_MODEL)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getModel() {
        return model;
    }

    @JsonProperty(JSON_PROPERTY_MODEL)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setModel(String model) {
        this.model = model;
    }
}
