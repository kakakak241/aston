package danila.zubov.aston.api;

import danila.zubov.aston.dto.UserCreateDto;
import danila.zubov.aston.dto.UserUpdateDto;
import danila.zubov.aston.dto.UserViewDto;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiITest {

  @LocalServerPort
  private int port;

  @Value("${openapi.astonBank.base-path}")
  private String basePathAstonBank;

  private final String USERS_ENDPOINT = "users";

  private final String USER_ENDPOINT = "users/{userUuid}";

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldCreateUser() {
    UserCreateDto createDto = new UserCreateDto("test");
    var response = restTemplate.postForEntity(getObjectsUri(), createDto, UserViewDto.class);
    assertCommonResponseChecks(response);

    var userViewDto = response.getBody();
    assertUserViewChecks(userViewDto, createDto.getName());

    deleteUser(userViewDto.getUuid());
  }

  @Test
  public void shouldGetUser() {
    UserCreateDto createDto = new UserCreateDto("test");
    var postResponse = restTemplate.postForEntity(getObjectsUri(), createDto, UserViewDto.class);
    assertCommonResponseChecks(postResponse);

    var getResponse = restTemplate.getForEntity(getObjectUri(postResponse.getBody().getUuid()), UserViewDto.class);
    assertCommonResponseChecks(getResponse);
    assertUserViewChecks(getResponse.getBody(), postResponse.getBody().getName());

    deleteUser(postResponse.getBody().getUuid());
  }

  @Test
  public void shouldGetUsers() {
    UserCreateDto createDto = new UserCreateDto("test");
    var postResponse = restTemplate.postForEntity(getObjectsUri(), createDto, UserViewDto.class);
    assertCommonResponseChecks(postResponse);

    var getResponse = restTemplate.getForEntity(getObjectsUri(), UserViewDto[].class);
    assertCommonArrayResponseChecks(getResponse, 1);

    deleteUser(postResponse.getBody().getUuid());
  }

  @Test
  public void shouldUpdateUser() {
    UserCreateDto createDto = new UserCreateDto("test");
    var postResponse = restTemplate.postForEntity(getObjectsUri(), createDto, UserViewDto.class);
    assertCommonResponseChecks(postResponse);

    UserUpdateDto updateDto = new UserUpdateDto("name");
    var updateResponse = restTemplate.exchange(getObjectUri(postResponse.getBody().getUuid()), HttpMethod.PUT, new HttpEntity<>(updateDto), UserViewDto.class);
    assertCommonResponseChecks(updateResponse);
    Assertions.assertEquals(updateDto.getName(), updateResponse.getBody().getName());

    deleteUser(postResponse.getBody().getUuid());
  }

  @Test
  public void shouldDeleteUser() {
    UserCreateDto createDto = new UserCreateDto("test");
    var postResponse = restTemplate.postForEntity(getObjectsUri(), createDto, UserViewDto.class);
    assertCommonResponseChecks(postResponse);

    var deleteResponse = restTemplate.exchange(getObjectUri(postResponse.getBody().getUuid()), HttpMethod.DELETE, null, Void.class);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(deleteResponse),
        () -> Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode())
    );
  }

  private void assertCommonResponseChecks(ResponseEntity<?> response) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(response),
        () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()),
        () -> Assertions.assertNotNull(response.getBody())
    );
  }

  private void assertCommonArrayResponseChecks(ResponseEntity<UserViewDto[]> response, int expectedLength) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(response),
        () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()),
        () -> Assertions.assertNotNull(response.getBody()),
        () -> Assertions.assertEquals(expectedLength, response.getBody().length)
    );
  }

  private void assertUserViewChecks(UserViewDto userView, String expectedName) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(userView.getUuid()),
        () -> Assertions.assertEquals(expectedName, userView.getName())
    );
  }

  private void deleteUser(UUID uuid) {
    restTemplate.exchange(getObjectUri(uuid), HttpMethod.DELETE, null, Void.class);
  }

  private String getUrl() {
    return String.format("http://localhost:%s%s", port, basePathAstonBank);
  }

  private URI getObjectsUri() {
    return UriComponentsBuilder.fromHttpUrl(getUrl()).pathSegment(USERS_ENDPOINT).build().encode()
        .toUri();
  }

  private URI getObjectUri(UUID uuid) {
    return UriComponentsBuilder.fromHttpUrl(getUrl()).pathSegment(USER_ENDPOINT)
        .buildAndExpand(uuid).toUri();
  }

}
