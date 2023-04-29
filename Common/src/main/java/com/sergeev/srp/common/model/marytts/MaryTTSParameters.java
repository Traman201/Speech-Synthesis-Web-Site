package com.sergeev.srp.common.model.marytts;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;
import java.util.Set;

@Getter
@Setter
public class MaryTTSParameters {

    private String audioEffects;
    private String style;
    private Set<Locale> locales;
    private Set<String> voices;
    private Set<String> inputTypes;
    private Set<String> outputTypes;
}
