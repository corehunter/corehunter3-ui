package org.corehunter.ui.mock;

import java.time.Instant;

import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunStatus;
import uno.informatics.data.pojo.SimpleEntityPojo;

public class CoreHunterRunMock extends SimpleEntityPojo implements CoreHunterRun {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Instant startInstant;
	private Instant endInstant;
	private CoreHunterRunStatus status;

	public CoreHunterRunMock(String name, Instant startInstant, Instant endInstant, CoreHunterRunStatus status) {
		super(name);
		this.startInstant = startInstant;
		this.endInstant = endInstant;
		this.status = status;
	}

	@Override
	public Instant getStartInstant() {
		return startInstant;
	}

	@Override
	public Instant getEndInstant() {
		return endInstant;
	}

	@Override
	public CoreHunterRunStatus getStatus() {
		return status;
	}

}
