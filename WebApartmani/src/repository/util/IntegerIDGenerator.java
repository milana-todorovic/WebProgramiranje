package repository.util;

public class IntegerIDGenerator {

	private Integer currentID;

	public IntegerIDGenerator() {
		super();
		this.currentID = 0;
	}

	public IntegerIDGenerator(Integer startID) {
		super();
		this.currentID = startID;
	}

	public IntegerIDGenerator(Iterable<Integer> existingIDs) {
		super();
		currentID = -1;
		for (Integer ID : existingIDs) {
			if (ID > currentID)
				currentID = ID;
		}
		currentID++;
	}

	public Integer generateID() {
		return currentID++;
	}

}
