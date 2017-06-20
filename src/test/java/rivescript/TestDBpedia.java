package rivescript;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by ramgathreya on 6/12/17.
 */
public class TestDBpedia extends RiveScriptBase {

    @Test
    public void testDBpedia() {
        String[] testCases = new String[]{"dbpedia", "What is DBpedia", "Can you tell me about DBpedia", "Getting started with DBpedia", "Idiots guide to dbpedia", "dbpedia tutorial", "dbpedia tutorials", "can you give me tutorials on DBpedia"};
        String[] expectedAnswer = new String[]{"{\"type\": \"template\", \"name\": \"dbpedia-about\"}"};
        checkAnswers(testCases, expectedAnswer, true);
    }

    @Test
    public void testDBpediaContribute() {
        String[] testCases = new String[]{"how to contribute to dbpedia", "how to contribute to dbpedia project", "contribute to dbpedia project", "how can i contribute to dbpedia???", "how can i contribute"};
        String[] expectedAnswer = new String[]{"{\"type\": \"template\", \"name\": \"dbpedia-contribute\"}"};
        checkAnswers(testCases, expectedAnswer, true);
    }

}
