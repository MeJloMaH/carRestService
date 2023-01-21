package ua.com.foxminded.model.search;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;

@Slf4j
public enum Operator {

	EQUAL {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
			Object value = request.getValue();
			Expression<?> key = root.get(request.getKey());
			return cb.and(cb.equal(key, value), predicate);
		}
	},

	NOT_EQUAL {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
			Object value = request.getFieldType().parse(request.getValue().toString());
			Expression<?> key = root.get(request.getKey());
			return cb.and(cb.notEqual(key, value), predicate);
		}
	},

	LIKE {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
			Expression<String> key = root.get(request.getKey());
			return cb.and(cb.like(cb.upper(key), "%" + request.getValue().toString().toUpperCase() + "%"), predicate);
		}
	},

	IN {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
			List<Object> values = request.getValues();
			CriteriaBuilder.In<Object> inClause = cb.in(root.get(request.getKey()));
			for (Object value : values) {
				inClause.value(value);
			}
			return cb.and(inClause, predicate);
		}
	},

	BETWEEN {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
			Object value = request.getFieldType().parse(request.getValue().toString());
			Object valueTo = request.getFieldType().parse(request.getValueTo().toString());
			if (request.getFieldType() == FieldType.DATE) {
				LocalDate startDate = (LocalDate) value;
				LocalDate endDate = (LocalDate) valueTo;
				Expression<LocalDate> key = root.get(request.getKey());
				return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate)),
						predicate);
			}

			if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
				Number start = (Number) value;
				Number end = (Number) valueTo;
				Expression<Number> key = root.get(request.getKey());
				return cb.and(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
			}

			log.info("Can not use between for {} field type.", request.getFieldType());
			return predicate;
		}
	},

	GREATER {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
			Object value = request.getFieldType().parse(request.getValue().toString());
			if (request.getFieldType() == FieldType.DATE) {
				LocalDate date = (LocalDate) value;
				Expression<LocalDate> key = root.get(request.getKey());
				return cb.and(cb.and(cb.greaterThanOrEqualTo(key, date)), predicate);
			}

			if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
				Number start = (Number) value;
				Expression<Number> key = root.get(request.getKey());
				return cb.and(cb.and(cb.ge(key, start)), predicate);
			}

			log.info("Can not use greater for {} field type.", request.getFieldType());
			return predicate;
		}
	},

	LESS {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
			Object value = request.getFieldType().parse(request.getValue().toString());
			if (request.getFieldType() == FieldType.DATE) {
				LocalDateTime date = (LocalDateTime) value;
				Expression<LocalDateTime> key = root.get(request.getKey());
				return cb.and(cb.and(cb.lessThanOrEqualTo(key, date)), predicate);
			}

			if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
				Number start = (Number) value;
				Expression<Number> key = root.get(request.getKey());
				return cb.and(cb.and(cb.le(key, start)), predicate);
			}

			log.info("Can not use less for {} field type.", request.getFieldType());
			return predicate;
		}
	},

	CAR_CATEGORY_JOIN {
		public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {

			CriteriaQuery<Car> cq = cb.createQuery(Car.class);
			
			Category value = (Category) request.getValue();		
			
			Subquery<Long> subquery = cq.subquery(Long.class);
			
			Root<Car> subqueryCar = subquery.from(Car.class);
			Join<Category, Car> subqueryCategory = subqueryCar.join(request.getKey());

			subquery.select(subqueryCar.get("id")).where(
					cb.equal(subqueryCategory.get("id"), value.getId()));
			
			return cb.and(cb.in(root.get("id")).value(subquery), predicate);
		}
	};
	
	public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate);

}