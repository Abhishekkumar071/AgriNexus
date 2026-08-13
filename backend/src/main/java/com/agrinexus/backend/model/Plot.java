package com.agrinexus.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plot {

    private Integer plotNumber;

    private String cropType;

    private String soilType;

    private Double areaInAcres;

    private String irrigationType;

    @Builder.Default
    private PlotStatus status = PlotStatus.ACTIVE;
}