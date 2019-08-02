package darksky.ogameresourcecalculator;

import java.util.Arrays;
import java.util.Objects;

public class SynchronousCalculator {

	public CalculatorOutput calc(CalculatorInput input){
		
		Objects.requireNonNull(input, "input is null");

		final double[] productionDays = { 0, 0, 0 };
		final long[] centiTodo = { 0, 0, 0 };
		final long[] centiProd = { 0, 0, 0 };
		final long[] centiCurrent = { 0, 0, 0 };
		final long[] centiSurplus = { 0, 0, 0 };
		final long resourceThreshold = 1000;

		// minimum step size 0.01
		final long centiStepSize = (long) (input.daySteps * 100);

		final Trade[] tradeList = { new Trade(), new Trade(), new Trade(), new Trade(), new Trade(), new Trade(), };

		boolean isNoProduction = true;
		boolean alreadyDone = true;
		boolean complete = false;
		long centiDays = 0;

		for (int i = 0; i < 3; i++) {
			centiTodo[i] = (input.want[i] - input.have[i]) * 100;
			centiProd[i] = input.prod[i] * 100;
			productionDays[i] = Math.abs(centiProd[i] != 0 ? 1.0 * centiTodo[i] / centiProd[i] : Double.MAX_VALUE);
			isNoProduction &= productionDays[i] == Double.MAX_VALUE;
			alreadyDone &= centiTodo[i] <= 0;
		}


		while (!alreadyDone && centiDays >= 0 && !complete) {

			for (int i = 0; i < 6; i++) {
				tradeList[i].from = 0;
				tradeList[i].to = 0;
				tradeList[i].middle = -1;
				tradeList[i].fromAmount = 0;
				tradeList[i].toAmount = 0;
				tradeList[i].middleAmount = 0;
			}

			for (int i = 0; i < 3; i++) {
				centiCurrent[i] = input.prod[i] * centiDays;
				centiSurplus[i] = centiCurrent[i] - centiTodo[i];
			}

			if (centiSurplus[0] > 0) {
				if (centiSurplus[1] < 0) {
					Trade t = tradeList[0];
					t.from = 0;
					t.to = 1;
					// trade met -> kris
					if (input.rates[0][1] > input.ratesdoubletrade[0][1] - 10E-3 || !input.useDoubleTrade) {
						t.middle = -1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[1] / input.rates[0][1], centiSurplus[0]));
						t.middleAmount = (long) 0;
						t.toAmount = (long) (t.fromAmount * input.rates[0][1]);
					} else { // trade met -> deut -> kris
						t.middle = 2;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[1] / input.ratesdoubletrade[0][1],
								centiSurplus[0]));
						t.middleAmount = (long) (t.fromAmount * input.rates[0][2]);
						t.toAmount = (long) (t.middleAmount * input.rates[2][1]);
					}

					centiSurplus[0] -= t.fromAmount;
					centiSurplus[1] += t.toAmount;

				}
				if (centiSurplus[2] < 0) {
					Trade t = tradeList[1];
					t.from = 0;
					t.to = 2;
					// trade met -> deut
					if (input.rates[0][2] > input.ratesdoubletrade[0][2] - 10E-3 || !input.useDoubleTrade) {
						t.middle = -1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[2] / input.rates[0][2], centiSurplus[0]));
						t.middleAmount = (long) 0;
						t.toAmount = (long) (t.fromAmount * input.rates[0][2]);
					} else { // trade met -> kris -> deut
						t.middle = 1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[2] / input.ratesdoubletrade[0][2],
								centiSurplus[0]));
						t.middleAmount = (long) (t.fromAmount * input.rates[0][1]);
						t.toAmount = (long) (t.middleAmount * input.rates[1][2]);
					}

					centiSurplus[0] -= t.fromAmount;
					centiSurplus[2] += t.toAmount;
				}
			}

			if (centiSurplus[1] > 0) {
				if (centiSurplus[0] < 0) {
					Trade t = tradeList[2];
					t.from = 1;
					t.to = 0;
					// trade kris -> met
					if (input.rates[1][0] > input.ratesdoubletrade[1][0] - 10E-3 || !input.useDoubleTrade) {
						t.middle = -1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[0] / input.rates[1][0], centiSurplus[1]));
						t.middleAmount = (long) 0;
						t.toAmount = (long) (t.fromAmount * input.rates[1][0]);
					} else { // trade kris -> deut -> met
						t.middle = 2;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[0] / input.ratesdoubletrade[1][0],
								centiSurplus[1]));
						t.middleAmount = (long) (t.fromAmount * input.rates[1][2]);
						t.toAmount = (long) (t.middleAmount * input.rates[2][0]);
					}

					centiSurplus[1] -= t.fromAmount;
					centiSurplus[0] += t.toAmount;
				}
				if (centiSurplus[2] < 0) {
					Trade t = tradeList[3];
					t.from = 1;
					t.to = 2;
					// trade kris -> deut
					if (input.rates[1][2] > input.ratesdoubletrade[1][2] - 10E-3 || !input.useDoubleTrade) {
						t.middle = -1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[2] / input.rates[1][2], centiSurplus[1]));
						t.middleAmount = (long) 0;
						t.toAmount = (long) (t.fromAmount * input.rates[1][2]);
					} else { // trade kris -> met -> deut
						t.middle = 0;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[2] / input.ratesdoubletrade[1][2],
								centiSurplus[1]));
						t.middleAmount = (long) (t.fromAmount * input.rates[1][0]);
						t.toAmount = (long) (t.middleAmount * input.rates[0][2]);
					}

					centiSurplus[1] -= t.fromAmount;
					centiSurplus[2] += t.toAmount;
				}
			}

			if (centiSurplus[2] > 0) {
				if (centiSurplus[0] < 0) {
					Trade t = tradeList[4];
					t.from = 2;
					t.to = 0;
					// trade deut -> met
					if (input.rates[2][0] > input.ratesdoubletrade[2][0] - 10E-3 || !input.useDoubleTrade) {
						t.middle = -1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[0] / input.rates[2][0], centiSurplus[2]));
						t.middleAmount = (long) 0;
						t.toAmount = (long) (t.fromAmount * input.rates[2][0]);
					} else { // trade deut -> kris -> met
						t.middle = 1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[0] / input.ratesdoubletrade[2][0],
								centiSurplus[2]));
						t.middleAmount = (long) (t.fromAmount * input.rates[2][1]);
						t.toAmount = (long) (t.middleAmount * input.rates[1][0]);
					}

					centiSurplus[2] -= t.fromAmount;
					centiSurplus[0] += t.toAmount;
				}
				if (centiSurplus[1] < 0) {
					Trade t = tradeList[5];
					t.from = 2;
					t.to = 1;
					// trade deut -> kris
					if (input.rates[2][1] > input.ratesdoubletrade[2][1] - 10E-3 || !input.useDoubleTrade) {
						t.middle = -1;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[1] / input.rates[2][1], centiSurplus[2]));
						t.middleAmount = (long) 0;
						t.toAmount = (long) (t.fromAmount * input.rates[2][1]);
					} else { // trade deut -> met -> kris
						t.middle = 0;
						t.fromAmount = (long) (Math.min(-1 * centiSurplus[1] / input.ratesdoubletrade[2][1],
								centiSurplus[2]));
						t.middleAmount = (long) (t.fromAmount * input.rates[2][0]);
						t.toAmount = (long) (t.middleAmount * input.rates[0][1]);
					}

					centiSurplus[2] -= t.fromAmount;
					centiSurplus[1] += t.toAmount;
				}
			}

			if (centiSurplus[0] >= -resourceThreshold && centiSurplus[1] >= -resourceThreshold
					&& centiSurplus[2] >= -resourceThreshold) {
				complete = true;
			} else {
				centiDays += centiStepSize;
			}

			if (isNoProduction)
				break;
		}

		CalculatorOutput output = new CalculatorOutput();

		if (alreadyDone) {
			output.maximumSaveDays = 0;
			output.saveDays = 0;
			output.complete = true;
			output.alreadyDone = true;
		} else {
			output.complete = complete;
			if (isNoProduction) {
				output.maximumSaveDays = Double.MAX_VALUE;
				if (complete) {
					output.saveDays = 0;
				} else {
					output.saveDays = Double.MAX_VALUE;
				}
			} else {
				output.maximumSaveDays = Arrays.stream(productionDays)
					.filter(d -> d > 10E-3 && d != Double.MAX_VALUE).max().orElseGet(()->Double.MAX_VALUE);
				output.saveDays = centiDays / 100.0;
			}
		}

		output.producedRessources[0] = input.prod[0] * centiDays / 100;
		output.producedRessources[1] = input.prod[1] * centiDays / 100;
		output.producedRessources[2] = input.prod[2] * centiDays / 100;

		output.haveResources[0] = input.have[0];
		output.haveResources[1] = input.have[1];
		output.haveResources[2] = input.have[2];

		for (Trade t : tradeList) {
			if (t.fromAmount != 0) {
				t.fromAmount /= 100;
				t.middleAmount /= 100;
				t.toAmount /= 100;
				output.tradeList.add(t);
			}
		}

		return output;
	}
}
