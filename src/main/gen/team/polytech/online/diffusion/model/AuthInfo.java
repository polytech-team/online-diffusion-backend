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
 * Ответная информация, содержащая JWT токены пользователя
 */

@Schema(name = "AuthInfo", description = "Ответная информация, содержащая JWT токены пользователя")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-30T02:52:21.618441+03:00[Europe/Moscow]")
public class AuthInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String refreshToken;

  private String accessToken;

  public AuthInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AuthInfo(String refreshToken, String accessToken) {
    this.refreshToken = refreshToken;
    this.accessToken = accessToken;
  }

  public AuthInfo refreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  /**
   * Токен, используемый для обновления авторизации, когда accessToken кончился
   * @return refreshToken
  */
  @NotNull 
  @Schema(name = "refreshToken", description = "Токен, используемый для обновления авторизации, когда accessToken кончился", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("refreshToken")
  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public AuthInfo accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * JWT токен авторизированного пользователя
   * @return accessToken
  */
  @NotNull 
  @Schema(name = "accessToken", description = "JWT токен авторизированного пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accessToken")
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthInfo authInfo = (AuthInfo) o;
    return Objects.equals(this.refreshToken, authInfo.refreshToken) &&
        Objects.equals(this.accessToken, authInfo.accessToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(refreshToken, accessToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthInfo {\n");
    sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
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

