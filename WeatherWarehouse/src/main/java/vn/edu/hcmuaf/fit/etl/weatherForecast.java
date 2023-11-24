package vn.edu.hcmuaf.fit.etl;

import lombok.*;

import java.awt.*;

@ToString
@RequiredArgsConstructor
public class weatherForecast {

    @Getter
    @Setter
    @NonNull
    private String date;
    @NonNull
    @Getter
    @Setter
    private String location;
    @NonNull
    @Getter
    @Setter
    private String average_temp;
    @NonNull
    @Getter
    @Setter
    private String high;
    @NonNull
    @Getter
    @Setter
    private String low;
    @NonNull
    @Getter
    @Setter
    private String humidity;
    @NonNull
    @Getter
    @Setter
    private String precipitation;
    @NonNull
    @Getter
    @Setter
    private String status;
    @NonNull
    @Getter
    @Setter
    private String day;
    @NonNull
    @Getter
    @Setter
    private String night;
    @NonNull
    @Getter
    @Setter
    private String morning;
    @NonNull
    @Getter
    @Setter
    private String evening;
    @NonNull
    @Getter
    @Setter
    private String pressure;
    @NonNull
    @Getter
    @Setter
    private String wind;
    @NonNull
    @Getter
    @Setter
    private String sunrise;
    @NonNull
    @Getter
    @Setter
    private String sunset;

}

