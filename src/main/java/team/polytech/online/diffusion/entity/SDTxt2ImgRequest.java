package team.polytech.online.diffusion.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import team.polytech.automatic.webui.model.StableDiffusionProcessingTxt2Img;

import javax.annotation.Nonnull;

public class SDTxt2ImgRequest {
    private static final String JSON_PROPERTY_UUID = "UUID";
    private static final String JSON_PROPERTY_REQUEST = "request";

    private String UUID;
    private StableDiffusionProcessingTxt2Img request;

    public SDTxt2ImgRequest() {
    }

    public SDTxt2ImgRequest(String UUID, StableDiffusionProcessingTxt2Img request) {
        this.UUID = UUID;
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
}
