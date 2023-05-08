package com.sergeev.srp.common.model.marytts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaryTTSParameters {
    private List<Effect> audioEffects;
    private Set<Locale> locales;
    private Set<String> voices;
    private Set<String> inputTypes;
    private Set<String> outputTypes;

    @Override
    public String toString() {
        return "MaryTTSParameters{" +
                ", audioEffects=" + audioEffects +
                ", locales=" + locales +
                ", voices=" + voices +
                ", inputTypes=" + inputTypes +
                ", outputTypes=" + outputTypes +
                '}';
    }
}
