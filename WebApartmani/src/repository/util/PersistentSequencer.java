package repository.util;

import java.io.File;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import custom_exception.InternalException;

public class PersistentSequencer implements IntegerIDGenerator {

	private String filePath;

	public PersistentSequencer(String filePath) {
		this.filePath = filePath;
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				writeFile(0);
			} catch (Exception e) {
				throw new InternalException(e);
			}
		}
	}

	private Integer readFile() {
		ObjectMapper mapper = new ObjectMapper();
		Integer id = null;
		try {
			id = mapper.readValue(Paths.get(filePath).toFile(), Integer.class);
		} catch (Exception e) {
			throw new InternalException(e);
		}
		if (id == null)
			return 0;
		else
			return id;
	}

	private void writeFile(Integer id) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		try {
			mapper.writeValue(Paths.get(filePath).toFile(), id);
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	@Override
	public synchronized Integer generateID() {
		Integer id = readFile();
		writeFile(id + 1);
		return id;
	}

}
