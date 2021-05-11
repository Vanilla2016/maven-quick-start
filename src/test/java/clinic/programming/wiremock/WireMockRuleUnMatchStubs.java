package clinic.programming.wiremock;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

/*
 * Subclass WireMockRule to prevent failure on attempted matching of stubs
 */
public class WireMockRuleUnMatchStubs extends WireMockRule {

	//Pass false to super constructor
	public WireMockRuleUnMatchStubs(Options options) {
        super(options, false);
    }

	public WireMockRuleUnMatchStubs(int port) {
		this(wireMockConfig().port(port));
	}

}
