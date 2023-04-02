package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllMaintenancesResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {
    private final MaintenanceRepository repository;
    private final ModelMapper mapper;
    private final CarService service;

    @Override
    public List<GetAllMaintenancesResponse> getAll() {
        List<Maintenance> maintenances = repository.findAll();
        List<GetAllMaintenancesResponse> responses = maintenances.stream().map(maintenance -> mapper.map(maintenance, GetAllMaintenancesResponse.class)).toList();
        return responses;
    }

    @Override
    public GetMaintenanceResponse getById(int id) {
        return mapper.map(repository.findById(id).orElseThrow(() -> new RuntimeException("Böyle bir bakım kaydı bulunmamaktadır.")), GetMaintenanceResponse.class);
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) {
        Car car = mapper.map(service.getById(request.getCarId()), Car.class);
        checkIfCarStateAvailable(car);
        service.changeState(car.getId(), 3);
        Maintenance maintenance = mapper.map(request, Maintenance.class);
        maintenance.setId(0);
        maintenance.setCar(car);
        repository.save(maintenance);
        return mapper.map(maintenance, CreateMaintenanceResponse.class);
    }

    @Override
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) {
        existsById(id);
        Maintenance maintenance = mapper.map(request, Maintenance.class);
        maintenance.setId(id);
        checkIfMaintenanceState(maintenance.getCar().getId(), request.isFinished());
        repository.save(maintenance);
        return mapper.map(maintenance, UpdateMaintenanceResponse.class);
    }

    @Override
    public void delete(int id) {
        existsById(id);
        repository.deleteById(id);
    }

    private void checkIfCarStateAvailable(Car car){
        if(car.getState() != 1){
            throw new RuntimeException("Araç bakıma uygun değildir.");
        }
    }

    private void existsById(int id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Böyle bir bakım bulunmamaktadır.");
        }
    }

    private void checkIfMaintenanceState(int carId, boolean state){
        if(state){
            service.changeState(carId, 3);
        }
    }


}
