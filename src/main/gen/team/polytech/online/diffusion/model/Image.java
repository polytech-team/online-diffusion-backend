package team.polytech.online.diffusion.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Объект с информацией об фотографии
 */

@Schema(name = "Image", description = "Объект с информацией об фотографии")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T21:32:52.356722874+03:00[Europe/Moscow]")
public class Image implements Serializable, Post {

  private static final long serialVersionUID = 1L;

  private BigDecimal photoId;

  private String authorName;

  private String authorAvatarUrl;

  private String prompt;

  private String antiPrompt;

  private String seed;

  private String model;

  public Image() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Image(BigDecimal photoId, String authorName, String authorAvatarUrl, String prompt, String antiPrompt, String seed, String model) {
    this.photoId = photoId;
    this.authorName = authorName;
    this.authorAvatarUrl = authorAvatarUrl;
    this.prompt = prompt;
    this.antiPrompt = antiPrompt;
    this.seed = seed;
    this.model = model;
  }

  public Image photoId(BigDecimal photoId) {
    this.photoId = photoId;
    return this;
  }

  /**
   * Id фотографии
   * @return photoId
  */
  @NotNull @Valid 
  @Schema(name = "photoId", description = "Id фотографии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("photoId")
  public BigDecimal getPhotoId() {
    return photoId;
  }

  public void setPhotoId(BigDecimal photoId) {
    this.photoId = photoId;
  }

  public Image authorName(String authorName) {
    this.authorName = authorName;
    return this;
  }

  /**
   * Имя автора
   * @return authorName
  */
  @NotNull 
  @Schema(name = "authorName", description = "Имя автора", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("authorName")
  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public Image authorAvatarUrl(String authorAvatarUrl) {
    this.authorAvatarUrl = authorAvatarUrl;
    return this;
  }

  /**
   * URL аватара автора
   * @return authorAvatarUrl
  */
  @NotNull 
  @Schema(name = "authorAvatarUrl", description = "URL аватара автора", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("authorAvatarUrl")
  public String getAuthorAvatarUrl() {
    return authorAvatarUrl;
  }

  public void setAuthorAvatarUrl(String authorAvatarUrl) {
    this.authorAvatarUrl = authorAvatarUrl;
  }

  public Image prompt(String prompt) {
    this.prompt = prompt;
    return this;
  }

  /**
   * prompt использовшийся для создания изображения
   * @return prompt
  */
  @NotNull 
  @Schema(name = "prompt", description = "prompt использовшийся для создания изображения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("prompt")
  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  public Image antiPrompt(String antiPrompt) {
    this.antiPrompt = antiPrompt;
    return this;
  }

  /**
   * anti-prompt использовшийся для создания изображения
   * @return antiPrompt
  */
  @NotNull 
  @Schema(name = "anti-prompt", description = "anti-prompt использовшийся для создания изображения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("anti-prompt")
  public String getAntiPrompt() {
    return antiPrompt;
  }

  public void setAntiPrompt(String antiPrompt) {
    this.antiPrompt = antiPrompt;
  }

  public Image seed(String seed) {
    this.seed = seed;
    return this;
  }

  /**
   * seed, использовшийся для создания изображения
   * @return seed
  */
  @NotNull 
  @Schema(name = "seed", description = "seed, использовшийся для создания изображения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seed")
  public String getSeed() {
    return seed;
  }

  public void setSeed(String seed) {
    this.seed = seed;
  }

  public Image model(String model) {
    this.model = model;
    return this;
  }

  /**
   * Модель, использувшееся для создания изображения
   * @return model
  */
  @NotNull 
  @Schema(name = "model", description = "Модель, использувшееся для создания изображения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("model")
  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Image image = (Image) o;
    return Objects.equals(this.photoId, image.photoId) &&
        Objects.equals(this.authorName, image.authorName) &&
        Objects.equals(this.authorAvatarUrl, image.authorAvatarUrl) &&
        Objects.equals(this.prompt, image.prompt) &&
        Objects.equals(this.antiPrompt, image.antiPrompt) &&
        Objects.equals(this.seed, image.seed) &&
        Objects.equals(this.model, image.model);
  }

  @Override
  public int hashCode() {
    return Objects.hash(photoId, authorName, authorAvatarUrl, prompt, antiPrompt, seed, model);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Image {\n");
    sb.append("    photoId: ").append(toIndentedString(photoId)).append("\n");
    sb.append("    authorName: ").append(toIndentedString(authorName)).append("\n");
    sb.append("    authorAvatarUrl: ").append(toIndentedString(authorAvatarUrl)).append("\n");
    sb.append("    prompt: ").append(toIndentedString(prompt)).append("\n");
    sb.append("    antiPrompt: ").append(toIndentedString(antiPrompt)).append("\n");
    sb.append("    seed: ").append(toIndentedString(seed)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

