package kodlama.io.rentacar.business.dto.responses.get;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCarResponse {
    private int id;
    private int modelYear;
    private String plate;
    private int state;
    private double dailyPrice;
}
