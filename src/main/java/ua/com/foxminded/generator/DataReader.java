package ua.com.foxminded.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DataReader {

	public List<String> getDataFromFileName(String fileName) {
		
		InputStream is = this.getFileFromResourceAsStream(fileName);
		return getRawDataInLine(is);
	}

	private InputStream getFileFromResourceAsStream(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}
	}

	private static List<String> getRawDataInLine(InputStream is) {
		
		ArrayList<String> data = new ArrayList<>();
		
		try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(streamReader)) {

			String line;
			while ((line = reader.readLine()) != null) {
				data.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;

	}


}
