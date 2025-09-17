import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdParser {
    
    /**
     * Parses an ad data string and returns a Map containing all key-value pairs.
     * Handles nested objects and arrays by converting them to string representations.
     * 
     * @param adDataString The raw ad data string to parse
     * @return Map<String, String> containing all parsed key-value pairs
     */
    public static Map<String, String> parseAdData(String adDataString) {
        Map<String, String> result = new HashMap<>();
        
        if (adDataString == null || adDataString.trim().isEmpty()) {
            return result;
        }
        
        // Pattern to match key=value pairs, handling various value types
        Pattern pattern = Pattern.compile("(\\w+)=([^,]+?)(?=,\\s*\\w+=|$)");
        Matcher matcher = pattern.matcher(adDataString);
        
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();
            
            // Remove surrounding brackets for arrays and objects
            if (value.startsWith("[") && value.endsWith("]")) {
                value = value.substring(1, value.length() - 1);
            }
            
            // Handle null values
            if ("null".equals(value)) {
                value = null;
            }
            
            result.put(key, value);
        }
        
        return result;
    }
    
    /**
     * Alternative method that handles more complex nested structures
     * by using a more sophisticated parsing approach
     */
    public static Map<String, String> parseAdDataAdvanced(String adDataString) {
        Map<String, String> result = new HashMap<>();
        
        if (adDataString == null || adDataString.trim().isEmpty()) {
            return result;
        }
        
        // Split by comma, but be careful with nested structures
        String[] parts = splitByComma(adDataString);
        
        for (String part : parts) {
            int equalsIndex = part.indexOf('=');
            if (equalsIndex > 0) {
                String key = part.substring(0, equalsIndex).trim();
                String value = part.substring(equalsIndex + 1).trim();
                
                // Clean up the value
                value = cleanValue(value);
                
                result.put(key, value);
            }
        }
        
        return result;
    }
    
    /**
     * Helper method to split string by comma while respecting nested structures
     */
    private static String[] splitByComma(String input) {
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        int bracketCount = 0;
        boolean inQuotes = false;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            if (c == '"' && (i == 0 || input.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                if (c == '[' || c == '{') {
                    bracketCount++;
                } else if (c == ']' || c == '}') {
                    bracketCount--;
                } else if (c == ',' && bracketCount == 0) {
                    result.add(current.toString().trim());
                    current = new StringBuilder();
                    continue;
                }
            }
            
            current.append(c);
        }
        
        if (current.length() > 0) {
            result.add(current.toString().trim());
        }
        
        return result.toArray(new String[0]);
    }
    
    /**
     * Helper method to clean up values
     */
    private static String cleanValue(String value) {
        if (value == null || "null".equals(value)) {
            return null;
        }
        
        // Remove surrounding brackets for arrays
        if (value.startsWith("[") && value.endsWith("]")) {
            value = value.substring(1, value.length() - 1);
        }
        
        // Remove surrounding quotes
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        
        return value;
    }
    
    // Test method
    public static void main(String[] args) {
        String adData = "Ad [adId=6627453075, creativeId=138467458357, creativeAdId=, universalAdIdValue=dZuS0ZKqeeM, universalAdIdRegistry=googlevideo, title=Tickets now streaming (copy), description=Tickets now streaming (copy) ad, contentType=application/x-mpegurl, adWrapperIds=[], adWrapperSystems=[], adWrapperCreativeIds=[], adSystem=GDFP, advertiserName=, surveyUrl=null, dealId=, linear=true, skippable=false, width=0, height=0, vastMediaHeight=144, vastMediaWidth=256, vastMediaBitrate=159, traffickingParameters=, clickThroughUrl=https://pubads.g.doubleclick.net/pcs/click?xai=AKAOjsvi7EJb4s4IruvGJJazsyV-9j5Uf1o7PqBEn8Hp0RgYn4TArqRpg-ZARHv28vjOkqckgJcif7eXUiJCmuXduPtV_kKK-ZhNyWSfbjpjERRWsoiRBhpjMPGdwoq8FFOEfKWdvkcXS__xz2t4N0szloUrLhDbGKOFU_tWyN72RiVjbvslwKy3pKX0zrLdn8s_qLlE_OmKg2yoCWk1lD-cl_SSzvpqCT0hQvtEoZNHlcEedKjqeDVhb9WLfJ7Xd9iBf0X0aYeWCGXo70eAHwX1fHdacGIkzKoO4XzqUbo838d2JZQVjqQC17t1xaykH2bBB4ZInrLsvrOxbAzqgHHH4PUy6aJnedS5QaqMrmzEz69wA85tuHv0uSU8qZXy8ZvpSq7HI6DtiBQZqsNPvRctgwntY5RfDCraWnHxNmc&sai=AMfl-YS3FHE44JpwE0fkkZNAe812_9EOM-_2CkhNos_i3SDGHNy9UIluKnTq7G0QBtU1l1hIJD4cfMU_7Fdo1B-287cn8vtXc_1bIy67rWqYhvrTg141XIqxKlD_0olF_M041c_vc6yZ1MFm0qiNxcz2TqJh8p9Re-j9bC8UtkF5169r-GJcykg_jbX1R8xaUI9gpmDD-cT7HRZK-xfKueaFxn99V4L54PSZFon_Mj2B8B5kWxa6lDcBUYOaoeIjA6UiXcjYoBEZQ7FiU2tlRQPszF5MfBBpXL0ehT-B0S1An0M3NlAhveQRTkdQ_zYYaSHvlEOFTMGq1Z9addhn3uewW0oz9EBX6b36a7vxM3T5sIDwrrv9AkJeyfh9ggyKLV-8nfHkzcCyvO5FErzzYkY&sig=Cg0ArKJSzB-WhtY_qVVR&fbs_aeid=%5Bgw_fbsaeid%5D&adurl=http://www.etv.co.za, duration=30.0, adPodInfo=AdPodInfo [totalAds=2, adPosition=1, isBumper=false, maxDuration=96.0, adsDurationsMs=[], podIndex=1, timeOffset=1800.0], uiElements=[], disableUi=false, skipTimeOffset=-1.0]";
        
        System.out.println("=== Simple Parser ===");
        Map<String, String> simpleResult = parseAdData(adData);
        for (Map.Entry<String, String> entry : simpleResult.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        
        System.out.println("\n=== Advanced Parser ===");
        Map<String, String> advancedResult = parseAdDataAdvanced(adData);
        for (Map.Entry<String, String> entry : advancedResult.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}