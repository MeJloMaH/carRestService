package ua.com.foxminded.model.search.request;

import static ua.com.foxminded.model.search.SearchRequest.DEFAULT_PAGE_SIZE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageOptions {
	private int page = 0;
	private int size = DEFAULT_PAGE_SIZE;
	private String sort = "id";
	private boolean descent = false;
		
	public void setPage(int page) {
		if(page < 0) {
			page = 0;
		}
		this.page = page;
	}
			
}
