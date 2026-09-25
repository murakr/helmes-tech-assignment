package ee.helmes.backend.controller;

import ee.helmes.backend.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class SectorControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @Test
    void returnsAllSeededSectorsInDisplayOrderWithLevels() {
        MvcTestResult result = mvc.get().uri("/api/sectors").exchange();

        assertThat(result).hasStatusOk();
        assertThat(result).bodyJson().extractingPath("$.length()").isEqualTo(79);
        assertThat(result).bodyJson().extractingPath("$[0].name").isEqualTo("Manufacturing");
        assertThat(result).bodyJson().extractingPath("$[0].level").isEqualTo(0);
        assertThat(result).bodyJson().extractingPath("$[1].name").isEqualTo("Construction materials");
        assertThat(result).bodyJson().extractingPath("$[1].level").isEqualTo(1);
    }
}