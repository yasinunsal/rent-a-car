package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.BrandService;
import kodlama.io.rentacar.business.dto.requests.create.CreateBrandRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateBrandRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateBrandResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllBrandsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetBrandResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateBrandResponse;
import kodlama.io.rentacar.entities.Brand;
import kodlama.io.rentacar.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandManager implements BrandService {
    private final BrandRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<GetAllBrandsResponse> getAll() {
        List<Brand> brands = repository.findAll();
        List<GetAllBrandsResponse> response = brands
                .stream()
                .map(brand -> mapper.map(brand, GetAllBrandsResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetBrandResponse getById(int id) {
        checkIfBrandExistsById(id);
        Brand brand = repository.findById(id).orElseThrow();
        GetBrandResponse response = mapper.map(brand, GetBrandResponse.class);

        return response;
    }

    @Override
    public CreateBrandResponse add(CreateBrandRequest request) {
        checkIfBrandExistsByName(request.getName());
        Brand brand = mapper.map(request, Brand.class);
        brand.setId(0);
        repository.save(brand);
        CreateBrandResponse response = mapper.map(brand, CreateBrandResponse.class);

        return response;
    }

    @Override
    public UpdateBrandResponse update(int id, UpdateBrandRequest request) {
        checkIfBrandExistsById(id);
        Brand brand = mapper.map(request, Brand.class);
        brand.setId(id);
        repository.save(brand);
        UpdateBrandResponse response = mapper.map(brand, UpdateBrandResponse.class);

        return response;
    }

    @Override
    public void delete(int id) {
        checkIfBrandExistsById(id);
        repository.deleteById(id);
    }

    // Business rules

    private void checkIfBrandExistsById(int id) {
        if (!repository.existsById(id)) throw new RuntimeException("Böyle bir marka mevcut değil.");
    }

    private void checkIfBrandExistsByName(String name) {
        if (repository.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Böyle bir marka sistemde kayıtlı!");
        }
    }
}
