package ua.com.foxminded.generator;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import lombok.Data;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;

@Data
public class CarParser {

	private String rawData;

	private Make make;
	private Model model;
	private Set<Category> categories = new HashSet<>();
	private Car car;

	public CarParser(String rawData) {
		this.rawData = rawData;
		try {
			parse(rawData);
		} catch (CarParserException e) {
			e.printStackTrace();
		}
	}

	private void parse(String line) throws CarParserException {

		if (line.isEmpty() || line.length() < 5) {
			throw new CarParserException(String.format("Cannot parse line {%s} ", line));
		}

		try (Scanner scanner = new Scanner(line).useDelimiter(",")) {

			String objectId = scanner.next();

			make = new Make(scanner.next());
			Integer carYear = scanner.nextInt();
			model = new Model(scanner.next());

			while (scanner.hasNext()) {
				categories.add(new Category(scanner.next().replace("\"", "").trim()));
			}

			car = new Car(null, objectId,
					carYear, make,
					model, categories);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class CarParserException extends Exception {
	public CarParserException(String errorMessage) {
		super(errorMessage);
	}
}
