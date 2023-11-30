package team.polytech.online.diffusion.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import team.polytech.online.diffusion.model.Post;
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

@Schema(name = "PostPagingWrapper", description = "Оболочка для пагинации")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-30T02:52:21.618441+03:00[Europe/Moscow]")
public class PostPagingWrapper implements Serializable {

  private static final long serialVersionUID = 1L;

  private BigDecimal nextMarker;

  @Valid
  private List<@Valid Post> posts;

  public PostPagingWrapper nextMarker(BigDecimal nextMarker) {
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

  public PostPagingWrapper posts(List<@Valid Post> posts) {
    this.posts = posts;
    return this;
  }

  public PostPagingWrapper addPostsItem(Post postsItem) {
    if (this.posts == null) {
      this.posts = new ArrayList<>();
    }
    this.posts.add(postsItem);
    return this;
  }

  /**
   * Список присланных постов в нужном порядке
   * @return posts
  */
  @Valid 
  @Schema(name = "posts", description = "Список присланных постов в нужном порядке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("posts")
  public List<@Valid Post> getPosts() {
    return posts;
  }

  public void setPosts(List<@Valid Post> posts) {
    this.posts = posts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostPagingWrapper postPagingWrapper = (PostPagingWrapper) o;
    return Objects.equals(this.nextMarker, postPagingWrapper.nextMarker) &&
        Objects.equals(this.posts, postPagingWrapper.posts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextMarker, posts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostPagingWrapper {\n");
    sb.append("    nextMarker: ").append(toIndentedString(nextMarker)).append("\n");
    sb.append("    posts: ").append(toIndentedString(posts)).append("\n");
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

