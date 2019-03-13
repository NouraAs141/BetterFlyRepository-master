package com.example.betterfly;

import android.graphics.Color;

import java.util.Random;

public class Color_Background_colors {

    private String[] mColors = {
            "#FFF0F5",//LavenderBlush
            "#FFFFFF",//white
            "#FFA07A",//LightSalmon
            "#FFA07A",//LightSalmon
    };

    public int getColor(){
        String color;
        Random randomGenerator = new Random();
        int randomnum = randomGenerator.nextInt(mColors.length);
        color = mColors[randomnum];
       int colorAsInt = Color.parseColor(color);
        return colorAsInt;
    }
}
