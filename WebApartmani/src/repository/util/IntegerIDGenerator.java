package repository.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IntegerIDGenerator {

	private AtomicInteger currentID;

	public IntegerIDGenerator() {
		super();
		this.currentID = new AtomicInteger(0);
	}

	public IntegerIDGenerator(Integer startID) {
		super();
		this.currentID = new AtomicInteger(startID);
	}

	public IntegerIDGenerator(Iterable<Integer> existingIDs) {
		super();
		int currentID = -1;
		for (Integer ID : existingIDs) {
			if (ID > currentID)
				currentID = ID;
		}
		currentID++;
		this.currentID = new AtomicInteger(currentID);
	}

	public Integer generateID() {
		return currentID.getAndIncrement();
	}

}
