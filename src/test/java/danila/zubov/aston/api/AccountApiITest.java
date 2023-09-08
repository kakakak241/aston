package danila.zubov.aston.api;

import danila.zubov.aston.dto.AccountCreateDto;
import danila.zubov.aston.dto.AccountViewDto;
import danila.zubov.aston.dto.TransactionDepositDto;
import danila.zubov.aston.dto.UserCreateDto;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountApiITest {

  @LocalServerPort
  private int port;

  @Value("${openapi.astonBank.base-path}")
  private String basePathAstonBank;

  private final String USERS_ENDPOINT = "users";

  private final String USER_ENDPOINT = "users/{userUuid}";

  private final String USER_ACCOUNTS_ENDPOINT = "users/{userUuid}/accounts";

  private final String USER_ACCOUNT_ENDPOINT = "users/{userUuid}/accounts/{accountUuid}";

  private final String ACCOUNT_DEPOSIT_ENDPOINT = "accounts/transactions/deposit";

  @Test
  public void testDepositTransaction() {
    UserCreateDto userCreationRequest = new UserCreateDto("test");
    var userResponse = restTemplate.postForEntity(getUsersUri(), userCreationRequest,
        UserViewDto.class);
    assertCommonResponseChecks(userResponse);

    AccountCreateDto accountCreationRequest = new AccountCreateDto("1234");
    accountCreationRequest.setName("test");

    var accountResponse = restTemplate.postForEntity(
        getAccountsUri(userResponse.getBody().getUuid()), accountCreationRequest,
        AccountViewDto.class);
    assertCommonResponseChecks(accountResponse);

    TransactionDepositDto depositRequest = new TransactionDepositDto(
        userResponse.getBody().getUuid(), accountResponse.getBody().getUuid(), 10000.0);

    var depositTransactionResponse = restTemplate.postForEntity(getDepositUri(), depositRequest,
        Void.class);
    Assertions.assertEquals(depositTransactionResponse.getStatusCode(), HttpStatus.NO_CONTENT);

    var retrievedAccountResponse = restTemplate.getForEntity(
        getAccountUri(userResponse.getBody().getUuid(), accountResponse.getBody().getUuid()),
        AccountViewDto.class);
    assertCommonResponseChecks(retrievedAccountResponse);

    Assertions.assertEquals(retrievedAccountResponse.getBody().getBalance(), 10000.0);
    deleteUser(userResponse.getBody().getUuid());
  }

  @Autowired
  private TestRestTemplate restTemplate;

  private void assertCommonResponseChecks(ResponseEntity<?> response) {
    Assertions.assertAll(() -> Assertions.assertNotNull(response),
        () -> Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()),
        () -> Assertions.assertNotNull(response.getBody()));
  }

  private void deleteUser(UUID uuid) {
    restTemplate.exchange(getUserUri(uuid), HttpMethod.DELETE, null, Void.class);
  }

  private String getUrl() {
    return String.format("http://localhost:%s%s", port, basePathAstonBank);
  }

  private URI getUsersUri() {
    return UriComponentsBuilder.fromHttpUrl(getUrl()).pathSegment(USERS_ENDPOINT).build().toUri();
  }

  private URI getUserUri(UUID uuid) {
    return UriComponentsBuilder.fromHttpUrl(getUrl()).pathSegment(USER_ENDPOINT)
        .buildAndExpand(uuid).toUri();
  }

  private URI getDepositUri() {
    return UriComponentsBuilder.fromHttpUrl(getUrl()).pathSegment(ACCOUNT_DEPOSIT_ENDPOINT).build()
        .toUri();
  }

  private URI getAccountsUri(UUID uuid) {
    return UriComponentsBuilder.fromHttpUrl(getUrl()).pathSegment(USER_ACCOUNTS_ENDPOINT)
        .buildAndExpand(uuid).toUri();
  }

  private URI getAccountUri(UUID userUuid, UUID accountUuid) {
    return UriComponentsBuilder.fromHttpUrl(getUrl()).pathSegment(USER_ACCOUNT_ENDPOINT)
        .buildAndExpand(userUuid, accountUuid).toUri();
  }

}
