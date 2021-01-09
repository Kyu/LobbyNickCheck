package me.preciouso.lobbynickcheck.MojangWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ning.http.client.AsyncHttpClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserCheck {
    ArrayList<String> validateUsernames(ArrayList<String> names) {
        ArrayList<String> valid = new ArrayList<>();
        for (String name: names) {
            if (name.length() < 16) {
                if (StandardCharsets.US_ASCII.newEncoder().canEncode(name)) {
                    if (name.replaceAll("[^_a-zA-Z0-9&]", "").equals(name)) {
                        valid.add(name);
                    }
                }
            }
        }
        return valid;
    }
    public List<List<String>> partition(ArrayList<String> source) {
        return partition(source, 10);
    }

    public List<List<String>> partition(ArrayList<String> source, int size) {
        List<String> listToBeSplit = Arrays.asList(source.toArray(new String[0]));
        return Lists.partition(listToBeSplit, size);
    }

    public void checkUsername(String name) {
        ArrayList<String> single = new ArrayList<>();
        single.add(name);

        try {
            checkUsername(single);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void checkUsername(ArrayList<String> names) throws ExecutionException, InterruptedException {
        ArrayList<String> validatedNames = validateUsernames(names);
        AsyncHttpClient c = new AsyncHttpClient();
        ArrayList<String> valid = new ArrayList<>();
        ArrayList<String> invalid = new ArrayList<>();

        List<List<String>> nameList = partition(validatedNames);

        for (List<String> namePart : nameList) {
            String jsonBody = new Gson().toJson(namePart);
            System.out.println("Checking as json: " + jsonBody);

            Future<ResponseHandler> g = c.preparePost("https://api.mojang.com/profiles/minecraft").setBody(jsonBody)
                    .setHeader("Content-Type", "application/json; charset=\"utf-8\"")
                    .execute(new RequestHandler<>());
            ResponseHandler response = g.get();
            String bodyResponse = response.getBody();
            int statusCode = response.getStatusCode();

            if (Strings.isNullOrEmpty(bodyResponse)) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText("Command failed with Mojang API Code: " + statusCode));
            } else {
                JsonBodyObject[] list = new Gson().fromJson(bodyResponse, JsonBodyObject[].class);
                for (JsonBodyObject bodyObject : list) {
                    valid.add(bodyObject.getName());
                }
            }

        }

        for (String name: names) {
            if (! valid.contains(name)) {
                invalid.add(name);
            }
        }

        String validStr = valid.toString().replace("[", "").replace("]", "");
        String invalidStr = invalid.toString().replace("[", "").replace("]", "");

        if (! Strings.isNullOrEmpty(validStr)) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Valid names on server"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(validStr));
        }

        if (! Strings.isNullOrEmpty(invalidStr)) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Invalid names on server"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(invalidStr));
        }

        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Players checked = " + names.size()));
    }
}
