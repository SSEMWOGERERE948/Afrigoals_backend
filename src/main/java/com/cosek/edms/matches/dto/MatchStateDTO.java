package com.cosek.edms.matches.dto;

import com.cosek.edms.matches.AddedTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MatchStateDTO {
    private String status;
    private int currentMinute;
    private String FormattedTime;
    private int elapsedSeconds;
    private String period;
    private Boolean running;
    private AddedTime addedTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date halfTimeStart;  // ✅ Added field

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date secondHalfStart; // ✅ Added field

    private Integer homeScore;
    private Integer awayScore;
}
