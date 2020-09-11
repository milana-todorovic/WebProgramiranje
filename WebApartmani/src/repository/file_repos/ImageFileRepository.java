package repository.file_repos;

import java.io.File;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Base64Image;
import repository.interfaces.ImageRepository;
import repository.util.PersistentSequencer;
import repository.util.IntegerIDGenerator;

public class ImageFileRepository implements ImageRepository {

	private String directoryPath;
	private IntegerIDGenerator generator;

	protected ImageFileRepository(String directoryPath) {
		this.directoryPath = directoryPath;
		File file = new File(directoryPath);
		if (!file.exists()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.generator = new PersistentSequencer(makePath("id"));
	}

	private String makePath(String id) {
		return directoryPath + File.separator + id + ".json";
	}

	private String generateID() {
		return generator.generateID().toString();
	}

	@Override
	public synchronized void deleteByID(String id) {
		File file = new File(makePath(id));
		if (file.exists())
			file.delete();
	}

	@Override
	public synchronized Boolean existsByID(String id) {
		File file = new File(makePath(id));
		return file.exists();
	}

	@Override
	public synchronized Base64Image simpleGetByID(String id) {
		ObjectMapper mapper = new ObjectMapper();
		Base64Image image = null;
		try {
			image = mapper.readValue(Paths.get(makePath(id)).toFile(), Base64Image.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public Base64Image create(Base64Image entity) {
		entity.setID(generateID());
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		synchronized (this) {
			try {
				mapper.writeValue(Paths.get(makePath(entity.getID())).toFile(), entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

}
