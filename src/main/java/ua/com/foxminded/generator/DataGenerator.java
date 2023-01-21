package ua.com.foxminded.generator;

import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;
import ua.com.foxminded.service.CarService;
import ua.com.foxminded.service.CategoryService;
import ua.com.foxminded.service.MakeService;
import ua.com.foxminded.service.ModelService;

@Component
public class DataGenerator implements ApplicationRunner {

	private String filePath = "files/cars.csv";

	private DataReader dataReader;

	private final MakeService makeService;
	private final ModelService modelService;
	private final CategoryService categoryService;
	private final CarService carService;

	public DataGenerator(DataReader dataReader, MakeService makeService, ModelService modelService,
			CategoryService categoryService, CarService carService) {
		this.dataReader = dataReader;
		this.makeService = makeService;
		this.modelService = modelService;
		this.categoryService = categoryService;
		this.carService = carService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (makeService.findAll().isEmpty() && modelService.findAll().isEmpty()
				&& categoryService.findAll().isEmpty()) {
		this.generate(dataReader.getDataFromFileName(filePath));
	}
	}

	private void generate(List<String> allCars) {

		for (String line : allCars) {

			if (line.contains("Make") && line.contains("Year")) {
				continue;
			}

			if (line.isEmpty() || line.length() < 5) {
				continue;
			}

			CarParser parser = new CarParser(line);

			Make make = parser.getMake();
			Model model = parser.getModel();
			Set<Category> categories = parser.getCategories();
			
			Car car = parser.getCar();
			
			model.setMake(make);
			car.setMake(make);
			car.setModel(model);		
			car.setCategories(categories);
			
			carService.save(car);
		}
	}


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
