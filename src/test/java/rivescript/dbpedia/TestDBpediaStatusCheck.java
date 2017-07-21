package rivescript.dbpedia;

import org.junit.Test;
import rivescript.RiveScriptBase;

/**
 * Created by ramgathreya on 6/20/17.
 */
public class TestDBpediaStatusCheck extends RiveScriptBase {
    @Test
    public void testDBpediaServiceCheck() {
        String[] testCases = new String[]{"dbpedia status", "dbpedia down", "dbpedia down right now", "DBPedia down?", "DbPedia is down", "DBpedia down?", "is dbpedia down right now", "check if dbpedia is down", "check if dbpedia is down right now", "How long is DBpedia going to be down ???"};
        String[] expectedAnswer = new String[]{"{\"type\": \"status_check\", \"name\": \"dbpedia\"}"};
        checkAnswers(testCases, expectedAnswer, true);
    }

    @Test
    public void testDBpediaSparqlServiceCheck() {
        String[] testCases = new String[]{"dbpedia sparql is running", "is dbpedia sparql running"};
        String[] expectedAnswer = new String[]{"{\"type\": \"status_check\", \"name\": \"dbpedia-sparql\"}"};
        checkAnswers(testCases, expectedAnswer, true);
    }

    @Test
    public void testDBpediaLookupServiceCheck() {
        String[] testCases = new String[]{"dbpedia lookup is running", "is dbpedia lookup running", "is lookup down"};
        String[] expectedAnswer = new String[]{"{\"type\": \"status_check\", \"name\": \"dbpedia-lookup\"}"};
        checkAnswers(testCases, expectedAnswer, true);
    }

    @Test
    public void testDBpediaMappingsServiceCheck() {
        String[] testCases = new String[]{"dbpedia mappings is running", "is dbpedia mapping running", "is mapping down"};
        String[] expectedAnswer = new String[]{"{\"type\": \"status_check\", \"name\": \"dbpedia-mappings\"}"};
        checkAnswers(testCases, expectedAnswer, true);
    }
}
