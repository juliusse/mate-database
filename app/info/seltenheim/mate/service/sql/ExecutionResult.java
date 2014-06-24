package info.seltenheim.mate.service.sql;

public class ExecutionResult {
	private final int affectedRows;
	private final Long generatedId;

	public ExecutionResult(int affectedRows, Long generatedId) {
		this.affectedRows = affectedRows;
		this.generatedId = generatedId;
	}

	public int getAffectedRows() {
		return affectedRows;
	}

	public Long getGeneratedId() {
		return generatedId;
	}

}