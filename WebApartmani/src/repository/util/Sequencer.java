package repository.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Sequencer implements IntegerIDGenerator {

	private AtomicInteger currentID;

	public Sequencer() {
		super();
		this.currentID = new AtomicInteger(0);
	}

	public Sequencer(Integer startID) {
		super();
		this.currentID = new AtomicInteger(startID);
	}

	public Sequencer(Iterable<Integer> existingIDs) {
		super();
		int currentID = -1;
		for (Integer ID : existingIDs) {
			if (ID > currentID)
				currentID = ID;
		}
		currentID++;
		this.currentID = new AtomicInteger(currentID);
	}

	@Override
	public Integer generateID() {
		return currentID.getAndIncrement();
	}

}
