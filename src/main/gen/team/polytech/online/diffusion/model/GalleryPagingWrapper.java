package team.polytech.online.diffusion.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import team.polytech.online.diffusion.model.Image;
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
 * Оболочка для пагинации
 */

@Schema(name = "GalleryPagingWrapper", description = "Оболочка для пагинации")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-27T00:11:07.629235+03:00[Europe/Moscow]")
public class GalleryPagingWrapper implements Serializable {

  private static final long serialVersionUID = 1L;

  private BigDecimal nextMarker;

  @Valid
  private List<@Valid Image> images;

  public GalleryPagingWrapper nextMarker(BigDecimal nextMarker) {
    this.nextMarker = nextMarker;
    return this;
  }

  /**
   * Маркер для следующего запроса
   * @return nextMarker
  */
  @Valid 
  @Schema(name = "nextMarker", description = "Маркер для следующего запроса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextMarker")
  public BigDecimal getNextMarker() {
    return nextMarker;
  }

  public void setNextMarker(BigDecimal nextMarker) {
    this.nextMarker = nextMarker;
  }

  public GalleryPagingWrapper images(List<@Valid Image> images) {
    this.images = images;
    return this;
  }

  public GalleryPagingWrapper addImagesItem(Image imagesItem) {
    if (this.images == null) {
      this.images = new ArrayList<>();
    }
    this.images.add(imagesItem);
    return this;
  }

  /**
   * Список присланных изображений в нужном порядке
   * @return images
  */
  @Valid 
  @Schema(name = "images", description = "Список присланных изображений в нужном порядке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("images")
  public List<@Valid Image> getImages() {
    return images;
  }

  public void setImages(List<@Valid Image> images) {
    this.images = images;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GalleryPagingWrapper galleryPagingWrapper = (GalleryPagingWrapper) o;
    return Objects.equals(this.nextMarker, galleryPagingWrapper.nextMarker) &&
        Objects.equals(this.images, galleryPagingWrapper.images);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextMarker, images);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GalleryPagingWrapper {\n");
    sb.append("    nextMarker: ").append(toIndentedString(nextMarker)).append("\n");
    sb.append("    images: ").append(toIndentedString(images)).append("\n");
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

