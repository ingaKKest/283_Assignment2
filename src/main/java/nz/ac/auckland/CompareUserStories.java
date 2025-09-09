package nz.ac.auckland;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class CompareUserStories {
    public static Integer userStoryComparison() {
        int matchCount = 0;
        try {
            // Read the JSON files
            String resultsJsonContent = new String(Files.readAllBytes(Paths.get("target/output/output.json")));
            String groundTruthJsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/ground_truth/ground_truth_labels.json")));

            // Parse JSON files
            JSONObject resultsJson = new JSONObject(resultsJsonContent);
            JSONArray resultsArray = resultsJson.getJSONArray("results"); // Extract the 'results' array
            JSONArray groundTruthArray = new JSONArray(groundTruthJsonContent);

            // Counter for matches
            matchCount = 0;

            // Iterate through results array
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);
                String resultUsId = result.getString("us_id").toLowerCase(); // Normalize to lowercase
                int estimatedProblemId = result.getInt("estimated_problem_id");

                // Find matching ground truth entry
                for (int j = 0; j < groundTruthArray.length(); j++) {
                    JSONObject groundTruth = groundTruthArray.getJSONObject(j);
                    String groundTruthUsId = groundTruth.getString("us_id").toLowerCase(); // Normalize to lowercase
                    int groundTruthProblemId = groundTruth.getInt("ground_truth_problem_id");

                    // Compare us_id and problem IDs
                    if (resultUsId.equals(groundTruthUsId)) {
                        if (estimatedProblemId == groundTruthProblemId) {
                            matchCount++;
                        }
                        break; // Found the match, no need to continue inner loop
                    }
                }
            }


        } catch (IOException e) {
            System.err.println("Error reading JSON files: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing JSON data: " + e.getMessage());
        }
        return matchCount;
    }
}