package mik.voice.siri.utils;

import java.util.Arrays;
import java.util.List;

public class tool_functions {
    public static String get_time(String input){
        Integer Hour = 0;
        Integer MIN = 0;
        Integer past = 0;
        if(input.indexOf("half") >= 0){
            MIN = 30;
        }
        else if(input.indexOf("quarter") >= 0)
        {
            MIN = 15;
        }
        if (input.indexOf("past") >= 0)
        {
            past = 1;
        }
        else if(input.indexOf("before") >= 0)
        {
            past = -1;
        }

        String[] working_data = input.split(" ");

        Hour = work_to_num(working_data[working_data.length-1]);
        if (working_data.length>2){
            MIN = work_to_num(working_data[0]);
        }
        String result = String.valueOf(Hour)+":"+String.valueOf(MIN);
        switch (past){
            case 1:
                result = String.valueOf(Hour)+":"+String.valueOf(MIN);
                break;
            case -1:
                Hour = Hour -1;
                MIN = 60-MIN;
                result = String.valueOf(Hour)+":"+String.valueOf(MIN);
                break;
        }
        return result;
    }

    public static Integer work_to_num(String str){

        if(str.equalsIgnoreCase("one")) {
            return 1;
        }
        else if(str.equalsIgnoreCase("two")) {
            return 2;
        }
        else if(str.equalsIgnoreCase("three")) {
            return 3;
        }
        else if(str.equalsIgnoreCase("four")) {
            return 4;
        }
        else if(str.equalsIgnoreCase("five")) {
            return 5;
        }
        else if(str.equalsIgnoreCase("six")) {
            return 6;
        }
        else if(str.equalsIgnoreCase("seven")) {
            return 7;
        }
        else if(str.equalsIgnoreCase("eight")) {
            return 8;
        }
        else if(str.equalsIgnoreCase("nine")) {
            return 9;
        }
        else if(str.equalsIgnoreCase("ten")) {
            return 10;
        }
        else if(str.equalsIgnoreCase("eleven")) {
            return 11;
        }
        else if(str.equalsIgnoreCase("twelve")) {
            return 12;
        }
        else if(str.equalsIgnoreCase("thirteen")) {
            return 13;
        }
        else if(str.equalsIgnoreCase("fourteen")) {
            return 14;
        }
        else if(str.equalsIgnoreCase("fifteen")) {
            return 15;
        }
        else if(str.equalsIgnoreCase("sixteen")) {
            return 16;
        }
        else if(str.equalsIgnoreCase("seventeen")) {
            return 17;
        }
        else if(str.equalsIgnoreCase("eighteen")) {
            return 18;
        }
        else if(str.equalsIgnoreCase("nineteen")) {
            return 19;
        }
        else if(str.equalsIgnoreCase("twenty")) {
            return 20;
        }
        else if(str.equalsIgnoreCase("thirty")) {
            return 30;
        }
        else if(str.equalsIgnoreCase("forty")) {
            return 40;
        }
        else if(str.equalsIgnoreCase("fifty")) {
            return 50;
        }
        return 0;
    }
}
