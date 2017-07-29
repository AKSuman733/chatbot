package chatbot.lib.handlers;

import chatbot.Application;
import chatbot.lib.Utility;
import chatbot.lib.handlers.dbpedia.LanguageHandler;
import chatbot.lib.handlers.dbpedia.StatusCheckHandler;
import chatbot.lib.request.Request;
import chatbot.lib.response.ResponseData;
import chatbot.lib.response.ResponseGenerator;
import chatbot.rivescript.RiveScriptReplyType;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ramgathreya on 5/22/17.
 */
public class TextHandler {
    private static final Logger logger = LoggerFactory.getLogger(TextHandler.class);

    private Request request;
    private String textMessage;
    private Application.Helper helper;

    public TextHandler(Request request, String textMessage, Application.Helper helper) {
        this.request = request;
        this.textMessage = textMessage;
        this.helper = helper;
    }

    public ResponseGenerator handleTextMessage() throws Exception {
        ResponseGenerator responseGenerator = new ResponseGenerator();
        String[] rivescriptReply = helper.getRiveScriptBot().answer(request.getUserId(), textMessage);

        for(String reply : rivescriptReply) {
            if(Utility.isJSONObject(reply) == true) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(reply);
                switch (rootNode.get("type").getTextValue()) {
                    case RiveScriptReplyType.TEMPLATE_SCENARIO:
                        responseGenerator = new TemplateHandler(request, Utility.split(rootNode.get("name").getTextValue(), Utility.PARAMETER_SEPARATOR), helper)
                                .handleTemplateMessage();
                        break;
                    case RiveScriptReplyType.LANGUAGE_SCENARIO:
                        responseGenerator = new LanguageHandler(request, rootNode.get("name").getTextValue(), helper)
                                .handleLanguageAbout();
                        break;
                    case RiveScriptReplyType.STATUS_CHECK_SCENARIO:
                        responseGenerator = new StatusCheckHandler(request, rootNode.get("name").getTextValue(), helper).handleStatusCheck();
                        break;
                    case RiveScriptReplyType.LOCATION_SCENARIO:
                        responseGenerator = new LocationHandler(request, rootNode.get("query").getTextValue(), helper).getLocation();
                        break;
                    case RiveScriptReplyType.FALLBACK_SCENARIO:
                        // Eliza
                        if(textMessage.endsWith("!") || textMessage.endsWith(".")) {
                            responseGenerator.addTextResponse(new ResponseData(helper.getEliza().processInput(textMessage)));
                        }
                        else {
                            textMessage = rootNode.get("query").getTextValue(); // Use processed text message
                            responseGenerator = new NLHandler(request, textMessage, helper).answer();
                        }
                        break;
                }
            }
            else {
                responseGenerator.addTextResponse(new ResponseData(reply));
            }
        }

        // Fallback when everything else fails Eliza will answer
        if(responseGenerator.getResponse().size() == 0) {
            responseGenerator.addTextResponse(new ResponseData(helper.getEliza().processInput(textMessage)));
        }
        return responseGenerator;
    }
}
