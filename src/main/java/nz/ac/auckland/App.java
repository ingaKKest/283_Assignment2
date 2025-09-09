package nz.ac.auckland;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.se283.GptClient;
import nz.ac.auckland.se283.prompts.PromptEngineering;
public class App {

    public static void main(String[] args) throws Exception {
        
        GptClient client = new GptClient();

        String input_content =new String(Files.readAllBytes(Paths.get("src/main/resources/input/input.json")));

        String systemPrompt = PromptEngineering.getPrompt("prompt");

        System.out.println(systemPrompt);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", systemPrompt));
        messages.add(
            new ChatMessage("user", input_content));
        ChatCompletionResult result = client.runOnce(messages, 1, 0.2, 0.8, 2000);
        String response = result.getFirstChoice().getChatMessage().getContent();
        System.out.println(response);
        try (FileWriter file = new FileWriter("target/output/output.json")) {
            file.write(response);
        } catch (IOException e) {
        
        }

        int matches = CompareUserStories.userStoryComparison();
        System.out.println("Number of matches is");
        System.out.println(matches);

  
    }
}

// ./mvnw compile exec:java