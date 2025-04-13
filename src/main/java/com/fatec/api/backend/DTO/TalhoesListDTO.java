package com.fatec.api.backend.DTO;
import java.util.ArrayList;
import java.util.List;

public class TalhoesListDTO {


    public static List<Integer> parseStringToList(String input) {
        List<Integer> result = new ArrayList<>();
        if (input != null && input.startsWith("[") && input.endsWith("]")) {
            String[] numbers = input.substring(1, input.length() - 1).split(",");
            for (String number : numbers) {
                try {
                    result.add(Integer.parseInt(number.trim()));
                } catch (NumberFormatException e) {
                }
            }
        }
        return result;
    }
    
}
