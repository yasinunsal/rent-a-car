package kodlama.io.rentacar.business.dto.responses.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarResponse {
    private int id;
    private int modelYear;
    private String plate;
    private int state;
    private double dailyPrice;
}
