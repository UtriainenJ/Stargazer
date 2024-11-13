package ryhma5.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class AstronomySorter {
    public static ArrayList<AstronomyResponse> getBrightestBodies(ArrayList<AstronomyResponse> bodyList) {

        // Map to hold the brightest (greatest) magnitude response for each body
        Map<String, AstronomyResponse> brightestBodies = new HashMap<>();

        // Process each body and retain only the entry with the greatest magnitude
        for (AstronomyResponse body : bodyList) {
            String bodyId = body.getBodyId(); // or use getBodyName(), if preferred

            // If the body isn't in the map, or if it is and the current entry is brighter, update the map
            if (!brightestBodies.containsKey(bodyId) || body.getMagnitude() < brightestBodies.get(bodyId).getMagnitude()) {
                brightestBodies.put(bodyId, body);
            }
        }

        // Return brightest bodies in an ArrayList
        return new ArrayList<>(brightestBodies.values());
    }

    public static void removeBodies(ArrayList<AstronomyResponse> bodyList, String... bodiesToRemove) {
        // Use a HashSet for faster lookup and automatic uniqueness
        Set<String> bodiesToRemoveSet = new HashSet<>();

        // Add the bodies to be removed to the set (varargs allows you to pass multiple body names)
        for (String body : bodiesToRemove) {
            bodiesToRemoveSet.add(body);
        }

        // Use an iterator to safely remove items while iterating
        Iterator<AstronomyResponse> iterator = bodyList.iterator();

        while (iterator.hasNext()) {
            AstronomyResponse body = iterator.next();

            // Check if the body name is in the set of bodies to remove
            if (bodiesToRemoveSet.contains(body.getBodyName().toLowerCase())) {
                iterator.remove();  // Remove the body from the list
            }
        }
    }
}
