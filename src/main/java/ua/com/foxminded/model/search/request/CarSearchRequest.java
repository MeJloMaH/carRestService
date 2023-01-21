package ua.com.foxminded.model.search.request;

import static ua.com.foxminded.model.search.SearchRequest.DEFAULT_PAGE_SIZE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;
import ua.com.foxminded.model.search.FieldType;
import ua.com.foxminded.model.search.FilterRequest;
import ua.com.foxminded.model.search.Operator;
import ua.com.foxminded.model.search.SearchRequest;
import ua.com.foxminded.model.search.SortDirection;
import ua.com.foxminded.model.search.SortRequest;

@Data
public class CarSearchRequest {

	private int page = 0;
	private int size = DEFAULT_PAGE_SIZE;
	private int manufacturedAfter;
	private int manufacturedBefore;
	private SortDirection direction = SortDirection.ASC;

	private Make make;
	private Model model;
	private Set<Category> categories;

	private String makeName;
	private String modelName;
	private List<String> categoryNames = new ArrayList<>();

	private List<String> sortBy = new ArrayList<>();

	public SearchRequest asSearchRequest() {

		List<FilterRequest> filters = new ArrayList<>();

		if (getManufacturedBefore() != 0 && getManufacturedAfter() != 0) {
			filters.add(FilterRequest.builder()
					.fieldType(FieldType.INTEGER)
					.key("modelYear")
					.operator(Operator.BETWEEN)
					.value((getManufacturedAfter()))
					.valueTo((getManufacturedBefore()))
					.build());
		} else if (getManufacturedAfter() != 0) {
			filters.add(FilterRequest.builder()
					.fieldType(FieldType.INTEGER)
					.key("modelYear")
					.operator(Operator.GREATER)
					.value((getManufacturedAfter()))
					.build());
		} else if (getManufacturedBefore() != 0) {
			filters.add(FilterRequest.builder()
					.fieldType(FieldType.INTEGER)
					.key("modelYear")
					.operator(Operator.LESS)
					.value((getManufacturedBefore()))
					.build());
		}

		if (getMake() != null) {
			filters.add(FilterRequest.builder()
					.key("make")
					.operator(Operator.EQUAL)
					.value(getMake())
					.build());
		}

		if (getModel() != null) {
			filters.add(FilterRequest.builder()
					.key("model")
					.operator(Operator.EQUAL)
					.value(getModel())
					.build());
		}

		if (getCategories() != null) {
			for (Category category : getCategories()) {
				if (category != null) {
					filters.add(FilterRequest.builder()
							.key("categories")
							.operator(Operator.CAR_CATEGORY_JOIN)
							.value(category)
							.build());
				}
			}

		}

		List<SortRequest> sortRequests = getSortBy().stream().map(column -> SortRequest.builder()
				.key(column)
				.direction(getDirection())
				.build()).collect(Collectors.toList());

		return new SearchRequest(filters, sortRequests, getPage(), getSize());
	}
}
