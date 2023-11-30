package team.polytech.online.diffusion.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Информация по юзеру
 */

@Schema(name = "ProfileInfo", description = "Информация по юзеру")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-30T02:52:21.618441+03:00[Europe/Moscow]")
public class ProfileInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username;

  private String email;

  private String avatarUrl;

  private Long generated;

  private Long galleryImages;

  private Long posted;

  public ProfileInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProfileInfo(String username, String email, String avatarUrl, Long generated, Long galleryImages, Long posted) {
    this.username = username;
    this.email = email;
    this.avatarUrl = avatarUrl;
    this.generated = generated;
    this.galleryImages = galleryImages;
    this.posted = posted;
  }

  public ProfileInfo username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Имя пользователя
   * @return username
  */
  @NotNull 
  @Schema(name = "username", description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public ProfileInfo email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Адрес почты
   * @return email
  */
  @NotNull 
  @Schema(name = "email", description = "Адрес почты", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ProfileInfo avatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
    return this;
  }

  /**
   * Ссылка на аватар
   * @return avatarUrl
  */
  @NotNull 
  @Schema(name = "avatarUrl", description = "Ссылка на аватар", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("avatarUrl")
  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public ProfileInfo generated(Long generated) {
    this.generated = generated;
    return this;
  }

  /**
   * Количество сгенерированных фото
   * @return generated
  */
  @NotNull 
  @Schema(name = "generated", description = "Количество сгенерированных фото", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("generated")
  public Long getGenerated() {
    return generated;
  }

  public void setGenerated(Long generated) {
    this.generated = generated;
  }

  public ProfileInfo galleryImages(Long galleryImages) {
    this.galleryImages = galleryImages;
    return this;
  }

  /**
   * Количество фото в галерее
   * @return galleryImages
  */
  @NotNull 
  @Schema(name = "gallery_images", description = "Количество фото в галерее", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("gallery_images")
  public Long getGalleryImages() {
    return galleryImages;
  }

  public void setGalleryImages(Long galleryImages) {
    this.galleryImages = galleryImages;
  }

  public ProfileInfo posted(Long posted) {
    this.posted = posted;
    return this;
  }

  /**
   * Количество сделаных постов
   * @return posted
  */
  @NotNull 
  @Schema(name = "posted", description = "Количество сделаных постов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("posted")
  public Long getPosted() {
    return posted;
  }

  public void setPosted(Long posted) {
    this.posted = posted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfileInfo profileInfo = (ProfileInfo) o;
    return Objects.equals(this.username, profileInfo.username) &&
        Objects.equals(this.email, profileInfo.email) &&
        Objects.equals(this.avatarUrl, profileInfo.avatarUrl) &&
        Objects.equals(this.generated, profileInfo.generated) &&
        Objects.equals(this.galleryImages, profileInfo.galleryImages) &&
        Objects.equals(this.posted, profileInfo.posted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email, avatarUrl, generated, galleryImages, posted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfileInfo {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    avatarUrl: ").append(toIndentedString(avatarUrl)).append("\n");
    sb.append("    generated: ").append(toIndentedString(generated)).append("\n");
    sb.append("    galleryImages: ").append(toIndentedString(galleryImages)).append("\n");
    sb.append("    posted: ").append(toIndentedString(posted)).append("\n");
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

