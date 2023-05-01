package com.sergeev.srp.common.model.marytts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Effect {

    String name;

    Map<String, Double> effect;

    @Override
    public String toString() {
        return "Effect{" +
                "name='" + name + '\'' +
                ", effect=" + effect +
                '}';
    }
}
