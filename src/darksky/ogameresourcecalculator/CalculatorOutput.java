package darksky.ogameresourcecalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for output of resource calculation
 * 
 * @author ogame.de Dark Sky
 * 
 */
public class CalculatorOutput {

	/**
	 * how many days needed including trades
	 */
	public double saveDays = 0;

	/**
	 * how many days need without trades
	 */
	public double maximumSaveDays = 0;

	/**
	 * produced resources after saveDays days
	 */
	public long[] producedRessources = { 0, 0, 0 };

	/**
	 * existing resources before calculation
	 */
	public long[] haveResources = { 0, 0, 0 };

	/**
	 * a list of performed trades
	 */
	public List<Trade> tradeList = new ArrayList<>();

	/**
	 * if alreadyDone = true, requirements where already met before calculation.
	 * no calculation performed
	 */
	public boolean alreadyDone = false;

	/**
	 * tells if requirements are met after calculation
	 */
	public boolean complete = false;

	public CalculatorOutput() {
	}

	@Override
	public String toString() {
		return "CalculatorOutput [saveDays=" + saveDays + ", maximumSaveDays=" + maximumSaveDays
				+ ", producedRessources=" + Arrays.toString(producedRessources) + ", haveResources="
				+ Arrays.toString(haveResources) + ", tradeList=" + tradeList + ", alreadyDone=" + alreadyDone
				+ ", complete=" + complete + "]";
	}

}
