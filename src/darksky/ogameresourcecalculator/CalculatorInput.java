package darksky.ogameresourcecalculator;

import java.util.Arrays;

/**
 * Input parameters for resource calculation
 * 
 * @author ogame.de Dark Sky
 * 
 */
public class CalculatorInput {
	/**
	 * daily production
	 */
	public long[] prod = { 0, 0, 0 };
	/**
	 * existing resources
	 */
	public long[] have = { 0, 0, 0 };
	/**
	 * required resources
	 */
	public long[] want = { 0, 0, 0 };
	/**
	 * exchange rates, normalized
	 */
	public double[][] rates = { { 3, 2, 1 }, { 3, 2, 1 }, { 3, 2, 1 } };
	/**
	 * exchange rates for using an intermediate resource type, normalized
	 */
	public double[][] ratesdoubletrade;

	/**
	 * step size for calculation
	 */
	public final double daySteps;

	/**
	 * use trades with an intermediate resource type if this gets more resources
	 */
	public final boolean useDoubleTrade;

	public CalculatorInput() {
		ratesdoubletrade = new double[][] { { 0, rates[0][2] * rates[2][1], rates[0][1] * rates[1][2] },
				{ rates[1][2] * rates[2][0], 0, rates[1][0] * rates[0][2] },
				{ rates[2][1] * rates[1][0], rates[2][0] * rates[0][1], 0 } };
		daySteps = 0.01;
		useDoubleTrade = false;
	}

	public CalculatorInput(long prodMet, long prodKris, long prodDeut, long haveMet, long haveKris, long haveDeut,
			long wantMet, long wantKris, long wantDeut, double[][] rates, double daySteps, boolean useDoubleTrade) {
		super();
		prod[0] = Math.abs(prodMet);
		prod[1] = Math.abs(prodKris);
		prod[2] = Math.abs(prodDeut);
		have[0] = Math.abs(haveMet);
		have[1] = Math.abs(haveKris);
		have[2] = Math.abs(haveDeut);
		want[0] = Math.abs(wantMet);
		want[1] = Math.abs(wantKris);
		want[2] = Math.abs(wantDeut);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.rates[i][j] = Math.abs(rates[i][i]) < 10E-4 ? 0 : rates[i][j] / rates[i][i];
			}
		}

		ratesdoubletrade = new double[][] {
				{ 0, this.rates[0][2] * this.rates[2][1], this.rates[0][1] * this.rates[1][2] },
				{ this.rates[1][2] * this.rates[2][0], 0, this.rates[1][0] * this.rates[0][2] },
				{ this.rates[2][1] * this.rates[1][0], this.rates[2][0] * this.rates[0][1], 0 } };

		this.daySteps = daySteps;
		this.useDoubleTrade = useDoubleTrade;
	}

	long[] getProd() {
		return prod;
	}

	long[] getHave() {
		return have;
	}

	long[] getWant() {
		return want;
	}

	double[][] getRates() {
		return rates;
	}

	double getDaySteps() {
		return daySteps;
	}

	boolean isUseDoubleTrade() {
		return useDoubleTrade;
	}

	@Override
	public String toString() {
		String rateString = "";
		String rateDoubleString = "";
		for(int i = 0; i < 3; i++){
			rateString += Arrays.toString(rates[i]) + " ";
			rateDoubleString += Arrays.toString(ratesdoubletrade[i]) + " ";
		}
		return "CalculatorInput [prod=" + Arrays.toString(prod) + ", have=" + Arrays.toString(have) + ", want="
				+ Arrays.toString(want) + ", rates=" + rateString + ", ratesdoubletrade="
				+ rateDoubleString + ", daySteps=" + daySteps + ", useDoubleTrade=" + useDoubleTrade
				+ "]";
	}
}
