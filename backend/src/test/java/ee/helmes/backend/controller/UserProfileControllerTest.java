package ee.helmes.backend.controller;

import ee.helmes.backend.IntegrationTest;
import ee.helmes.backend.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class UserProfileControllerTest {

    private static final String PROFILE_URL = "/api/profile";

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @BeforeEach
    void deleteAllProfiles() {
        userProfileRepository.deleteAll();
    }

    @Test
    void newSessionHasNoProfile() {
        assertThat(get(new MockHttpSession())).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void savedProfileIsReturnedToTheSameSession() {
        MockHttpSession session = new MockHttpSession();

        assertThat(save(session, """
                {"name": "  testUser ", "sectorIds": [1, 19], "agreedToTerms": true}
                """)).hasStatusOk();

        MvcTestResult refill = get(session);
        assertThat(refill).hasStatusOk();
        assertThat(refill).bodyJson().extractingPath("$.name").isEqualTo("testUser");
        assertThat(refill).bodyJson().extractingPath("$.sectorIds").asArray().containsExactlyInAnyOrder(1, 19);
        assertThat(refill).bodyJson().extractingPath("$.agreedToTerms").isEqualTo(true);
    }

    @Test
    void savingAgainInSameSessionUpdatesTheExistingProfile() {
        MockHttpSession session = new MockHttpSession();

        save(session, """
                {"name": "testUser 2", "sectorIds": [1, 19], "agreedToTerms": true}
                """);
        save(session, """
                {"name": "testUser 2", "sectorIds": [75], "agreedToTerms": true}
                """);

        assertThat(userProfileRepository.count()).isEqualTo(1);
        MvcTestResult refill = get(session);
        assertThat(refill).bodyJson().extractingPath("$.name").isEqualTo("testUser 2");
        assertThat(refill).bodyJson().extractingPath("$.sectorIds").asArray().containsExactly(75);
    }

    @Test
    void differentSessionsCannotSeeOrChangeEachOthersProfiles() {
        MockHttpSession firstUser = new MockHttpSession();
        MockHttpSession secondUser = new MockHttpSession();

        save(firstUser, """
                {"name": "First", "sectorIds": [1], "agreedToTerms": true}
                """);
        assertThat(get(secondUser)).hasStatus(HttpStatus.NO_CONTENT);

        save(secondUser, """
                {"name": "Second", "sectorIds": [2], "agreedToTerms": true}
                """);

        assertThat(userProfileRepository.count()).isEqualTo(2);
        assertThat(get(firstUser)).bodyJson().extractingPath("$.name").isEqualTo("First");
        assertThat(get(secondUser)).bodyJson().extractingPath("$.name").isEqualTo("Second");
    }

    @Test
    void invalidInputIsRejectedWithFieldErrorsAndNothingIsStored() {
        MvcTestResult result = save(new MockHttpSession(), """
                {"name": "   ", "sectorIds": [], "agreedToTerms": false}
                """);

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(result).bodyJson().extractingPath("$.errors").asMap()
                .containsOnlyKeys("name", "sectorIds", "agreedToTerms");
        assertThat(userProfileRepository.count()).isZero();
    }

    @Test
    void unknownSectorIsRejectedAndNothingIsStored() {
        MvcTestResult result = save(new MockHttpSession(), """
                {"name": "testUser", "sectorIds": [99999], "agreedToTerms": true}
                """);

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(result).bodyJson().extractingPath("$.detail").asString().contains("99999");
        assertThat(userProfileRepository.count()).isZero();
    }

    private MvcTestResult get(MockHttpSession session) {
        return mvc.get().uri(PROFILE_URL).session(session).exchange();
    }

    private MvcTestResult save(MockHttpSession session, String json) {
        return mvc.put().uri(PROFILE_URL)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .exchange();
    }
}